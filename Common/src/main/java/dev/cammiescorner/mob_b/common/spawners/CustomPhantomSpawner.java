package dev.cammiescorner.mob_b.common.spawners;

import dev.cammiescorner.mob_b.common.entities.PhantomEntity;
import dev.cammiescorner.mob_b.common.registries.MobBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class CustomPhantomSpawner implements CustomSpawner {
	private int nextTick;

	@Override
	public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
		if(!spawnEnemies)
			return 0;
		else {
			RandomSource random = level.getRandom();
			nextTick--;

			if(nextTick > 0)
				return 0;
			else {
				nextTick += (5 + random.nextInt(5)) * 20;

				if(level.getSkyDarken() < 5 && level.dimensionType().hasSkyLight())
					return 0;

				for(ServerPlayer player : level.players()) {
					if(!player.isSpectator() && player.isFallFlying()) {
						BlockPos pos = player.blockPosition();

						if(!level.dimensionType().hasSkyLight() || (pos.getY() >= level.getSeaLevel() && level.canSeeSky(pos))) {
							DifficultyInstance difficulty = level.getCurrentDifficultyAt(pos);
							boolean noPhantomsTargetingPlayer = level.getEntities(EntityTypeTest.forClass(PhantomEntity.class), phantom -> player.equals(phantom.getTarget())).isEmpty();

							if(noPhantomsTargetingPlayer && difficulty.isHarderThan(random.nextFloat() * 3f) && random.nextInt(player.getFallFlyingTicks()) > 600) {
								Vec3 offset = player.getViewVector(1f).scale(-1).scale(12 + random.nextInt(4));
								BlockPos spawnPos = pos.offset((int) offset.x, (int) offset.y, (int) offset.z);
								BlockState blockstate = level.getBlockState(spawnPos);
								FluidState fluidstate = level.getFluidState(spawnPos);

								if(NaturalSpawner.isValidEmptySpawnBlock(level, spawnPos, blockstate, fluidstate, MobBEntities.PHANTOM.get())) {
									Phantom phantom = MobBEntities.PHANTOM.get().create(level);

									if(phantom != null) {
										phantom.moveTo(spawnPos, 0f, 0f);
										phantom.finalizeSpawn(level, difficulty, MobSpawnType.NATURAL, null);
										phantom.setTarget(player);
										((PhantomEntity.FlightMoveControl) phantom.getMoveControl()).targetSpeed = 3f;
										level.addFreshEntityWithPassengers(phantom);

										return 1;
									}
								}
							}
						}
					}
				}
			}
		}

		return 0;
	}
}
