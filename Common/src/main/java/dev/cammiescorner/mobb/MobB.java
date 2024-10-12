package dev.cammiescorner.mobb;

import dev.cammiescorner.mobb.common.registries.MobBEntities;
import dev.cammiescorner.mobb.common.registries.MobBMobEffects;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.resources.ResourceLocation;

public class MobB implements MainEntryPoint {
	public static final String MOD_ID = "mobb";

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		RegistryService registryService = RegistryService.get();

		MobBEntities.ENTITY_TYPES.accept(registryService);
		MobBMobEffects.STATUS_EFFECTS.accept(registryService);
	}
}
