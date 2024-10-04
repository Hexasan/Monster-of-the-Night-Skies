package dev.cammiescorner.mob_b.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PhantomEntity extends Phantom {
	private int despawnTimer = 300;

	public PhantomEntity(EntityType<? extends Phantom> entityType, Level level) {
		super(entityType, level);
		moveControl = new FlightMoveControl(this);
	}

	public static AttributeSupplier.Builder createPhantomAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.FOLLOW_RANGE, 64)
			.add(Attributes.MAX_HEALTH, 4)
			.add(Attributes.ATTACK_DAMAGE, 8);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new CustomPhantomAttackStrategyGoal());
		goalSelector.addGoal(2, new PhantomChaseElytraGoal());
		goalSelector.addGoal(3, new CustomPhantomCircleAroundAnchorGoal());
		targetSelector.addGoal(1, new Phantom.PhantomAttackPlayerTargetGoal());
	}

	@Override
	public void tick() {
		super.tick();

		if(!level().isClientSide()) {
			if(getTarget() != null && getTarget().isFallFlying())
				despawnTimer = 400;
			else if(despawnTimer-- <= 0)
				remove(RemovalReason.DISCARDED);

			if(attackPhase == AttackPhase.CIRCLE && getTarget() != null) {
				List<Entity> mobs = level().getEntities(this, getBoundingBox().inflate(64), EntitySelector.NO_SPECTATORS.and(entity -> entity instanceof Mob mob && mob.canAttack(getTarget()) && !getTarget().equals(mob.getTarget())));

				for(Entity entity : mobs)
					if(entity instanceof Mob mob)
						mob.setTarget(getTarget());
			}
		}
	}

	@Override
	public float getPickRadius() {
		return 0.25f;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if(source.is(DamageTypes.FLY_INTO_WALL) && amount > 0) {
			kill();
			return true;
		}

		return super.hurt(source, amount);
	}

	public class CustomPhantomAttackStrategyGoal extends PhantomAttackStrategyGoal {
		int offsetY = 10;

		@Override
		public void start() {
			offsetY += getRandom().nextInt(6);
		}

		@Override
		public void tick() {
			if(getTarget() != null && getTarget().isAlive() && getTarget().isFallFlying())
				attackPhase = AttackPhase.SWOOP;
			else
				attackPhase = AttackPhase.CIRCLE;

			if(getTarget() != null)
				anchorPoint = getTarget().blockPosition().above(offsetY);
			if(anchorPoint.getY() < level().getSeaLevel())
				anchorPoint = new BlockPos(anchorPoint.getX(), level().getSeaLevel() + 1, anchorPoint.getZ());
		}
	}

	public class PhantomChaseElytraGoal extends Phantom.PhantomMoveTargetGoal {
		@Override
		public boolean canUse() {
			return getTarget() != null && attackPhase == Phantom.AttackPhase.SWOOP;
		}

		@Override
		public boolean canContinueToUse() {
			LivingEntity target = getTarget();

			return target != null && target.isAlive() && (!(target instanceof Player player) || (!target.isSpectator() && !player.isCreative() && player.isFallFlying()));
		}

		@Override
		public void start() {
			playSound(SoundEvents.PHANTOM_SWOOP, 10f, 0.95f + getRandom().nextFloat() * 0.1f);
		}

		@Override
		public void stop() {
			attackPhase = Phantom.AttackPhase.CIRCLE;
		}

		@Override
		public void tick() {
			LivingEntity target = getTarget();

			if(target != null) {
				moveTargetPoint = new Vec3(target.getX(), target.getY(0.5), target.getZ());

				if(getBoundingBox().inflate(0.2f).intersects(target.getBoundingBox())) {
					doHurtTarget(target);

					if(!isSilent())
						level().levelEvent(1039, blockPosition(), 0);
				}
				else if(!target.isFallFlying())
					attackPhase = Phantom.AttackPhase.CIRCLE;
			}
		}
	}

	public class CustomPhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal {
		private int arc = 1;

		@Override
		public boolean canUse() {
			return getTarget() != null && attackPhase == Phantom.AttackPhase.CIRCLE;
		}

		@Override
		public boolean canContinueToUse() {
			return getTarget() == null || !getTarget().isFallFlying();
		}

		@Override
		public void start() {
			moveTargetPoint = Vec3.atCenterOf(anchorPoint);
		}

		@Override
		public void stop() {
			attackPhase = AttackPhase.SWOOP;
		}

		@Override
		public void tick() {
			final int radius = 12;
			final int maxArcs = 16;
			final float arcAngle = 360f / maxArcs;

			if(distanceToSqr(moveTargetPoint) < 3f) {
				float angle = (float) Math.toRadians(arcAngle * arc);
				float offsetX = Mth.cos(angle) * radius;
				float offsetZ = Mth.sin(angle) * radius;

				moveTargetPoint = Vec3.atCenterOf(anchorPoint.offset(Math.round(offsetX), 0, Math.round(offsetZ)));

				if(arc++ > maxArcs)
					arc = 1;
			}
		}
	}

	public class FlightMoveControl extends MoveControl {
		public float targetSpeed = 0.2f;

		public FlightMoveControl(final Mob owner) {
			super(owner);
		}

		@Override
		public void tick() {
			if(horizontalCollision) {
				setYRot(getYRot() + 180f);
				targetSpeed = 0.1f;
			}

			double distanceX = moveTargetPoint.x - getX();
			double distanceY = moveTargetPoint.y - getY();
			double distanceZ = moveTargetPoint.z - getZ();
			double horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

			if(Math.abs(horizontalDistance) > 0.00001) {
				double h = 1 - Math.abs(distanceY * 0.7) / horizontalDistance;

				distanceX *= h;
				distanceZ *= h;
				horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

				double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
				float yaw = getYRot();
				float arcTanXZ = (float) Mth.atan2(distanceZ, distanceX);
				float wrappedYaw = Mth.wrapDegrees(getYRot() + 90f);
				float wrappedArcTanXZ = (float) Mth.wrapDegrees(arcTanXZ * (180f / Math.PI));

				setYRot(Mth.approachDegrees(wrappedYaw, wrappedArcTanXZ, 4f) - 90f);
				yBodyRot = getYRot();

				if(Mth.degreesDifferenceAbs(yaw, getYRot()) < 3f) {
					if(getTarget() != null && getTarget().isFallFlying())
						targetSpeed = Mth.approach(targetSpeed, 4f, 0.01f * (3f / targetSpeed));
					else
						targetSpeed = Mth.approach(targetSpeed, 1f, 0.005f * (1f / targetSpeed));
				}
				else {
					targetSpeed = Mth.approach(targetSpeed, 0.2f, 0.001f);
				}

				float pitch = (float) -(Mth.atan2(-distanceY, horizontalDistance) * (180f / Math.PI));

				setXRot(pitch);

				float adjustedYaw = getYRot() + 90f;
				double accelerationX = (targetSpeed * Mth.cos(adjustedYaw * (float) (Math.PI / 180))) * Math.abs(distanceX / distance);
				double accelerationY = (targetSpeed * Mth.sin(pitch * (float) (Math.PI / 180))) * Math.abs(distanceY / distance);
				double accelerationZ = (targetSpeed * Mth.sin(adjustedYaw * (float) (Math.PI / 180))) * Math.abs(distanceZ / distance);
				Vec3 vec3d = getDeltaMovement();

				setDeltaMovement(vec3d.add((new Vec3(accelerationX, accelerationY, accelerationZ)).subtract(vec3d).scale(0.2)));
			}
		}
	}
}