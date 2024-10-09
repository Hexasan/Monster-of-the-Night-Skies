package dev.cammiescorner.mob_b;

import dev.cammiescorner.mob_b.common.registries.MobBEntities;
import dev.cammiescorner.mob_b.common.registries.MobBMobEffects;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.resources.ResourceLocation;

public class MobB implements MainEntryPoint {
	public static final String MOD_ID = "mob-b";

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
