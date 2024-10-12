package dev.cammiescorner.mobb.mixin;

import com.google.common.collect.ImmutableMap;
import dev.cammiescorner.mobb.common.entities.PhantomEntity;
import dev.cammiescorner.mobb.common.registries.MobBEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(DefaultAttributes.class)
public class DefaultAttributesMixin {
	@Shadow
	@Mutable
	@Final
	private static Map<EntityType<? extends LivingEntity>, AttributeSupplier> SUPPLIERS;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void gimmePhantomAttributesDamnIt(CallbackInfo info) {
		SUPPLIERS = new ImmutableMap.Builder<EntityType<? extends LivingEntity>, AttributeSupplier>().putAll(SUPPLIERS).put(MobBEntities.PHANTOM.get(), PhantomEntity.createPhantomAttributes().build()).build();
	}
}
