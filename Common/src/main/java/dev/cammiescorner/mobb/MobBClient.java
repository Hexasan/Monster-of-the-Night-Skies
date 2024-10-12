package dev.cammiescorner.mobb;

import dev.cammiescorner.mobb.client.renderers.PhantomEntityRenderer;
import dev.cammiescorner.mobb.common.registries.MobBEntities;
import dev.upcraft.sparkweave.api.client.event.RegisterEntityRenderersEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;

public class MobBClient implements ClientEntryPoint {
	@Override
	public void onInitializeClient(ModContainer mod) {
		RegisterEntityRenderersEvent.EVENT.register(event -> {
			event.registerRenderer(MobBEntities.PHANTOM, PhantomEntityRenderer::new);
		});
	}
}
