package dev.cammiescorner.mob_b.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.mob_b.common.entities.PhantomEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique private final LivingEntity self = (LivingEntity) (Object) this;

	public LivingEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

	@ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z"))
	private boolean phantomGoSplat(boolean original) {
		return original || (self instanceof PhantomEntity phantom && phantom.attackPhase == Phantom.AttackPhase.SWOOP);
	}
}
