package dev.cammiescorner.mobb.neoforge.entrypoints;

import dev.cammiescorner.mobb.MobB;
import dev.cammiescorner.mobb.client.renderers.PhantomEntityRenderer;
import net.minecraft.client.model.PhantomModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = MobB.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PhantomEntityRenderer.MODEL_LAYER, PhantomModel::createBodyLayer);
	}
}
