package dev.cammiescorner.mob_b;

import dev.cammiescorner.mob_b.client.renderers.PhantomEntityRenderer;
import dev.cammiescorner.mob_b.common.registries.MobBEntities;
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
