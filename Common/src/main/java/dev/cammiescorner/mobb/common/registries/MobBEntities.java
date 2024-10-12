package dev.cammiescorner.mobb.common.registries;

import dev.cammiescorner.mobb.MobB;
import dev.cammiescorner.mobb.common.entities.PhantomEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MobBEntities {
	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, MobB.MOD_ID);

	public static final RegistrySupplier<EntityType<PhantomEntity>> PHANTOM = ENTITY_TYPES.register("phantom", () -> EntityType.Builder.of(PhantomEntity::new, MobCategory.MONSTER).sized(0.9F, 0.5F).eyeHeight(0.175F).passengerAttachments(0.3375F).ridingOffset(-0.125F).clientTrackingRange(8).build(null));
}
