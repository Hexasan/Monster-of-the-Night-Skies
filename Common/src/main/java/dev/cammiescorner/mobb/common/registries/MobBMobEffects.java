package dev.cammiescorner.mobb.common.registries;

import dev.cammiescorner.mobb.MobB;
import dev.cammiescorner.mobb.common.effects.WeightedMobEffect;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MobBMobEffects {
	public static final RegistryHandler<MobEffect> STATUS_EFFECTS = RegistryHandler.create(Registries.MOB_EFFECT, MobB.MOD_ID);

	public static final RegistrySupplier<MobEffect> WEIGHTED = STATUS_EFFECTS.register("weighted", () -> new WeightedMobEffect(MobEffectCategory.HARMFUL, 0x360454).addAttributeModifier(Attributes.GRAVITY, MobB.id("phantom.bite"), 0.04, AttributeModifier.Operation.ADD_VALUE));
}
