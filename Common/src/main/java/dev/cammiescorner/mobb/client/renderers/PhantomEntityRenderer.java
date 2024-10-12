package dev.cammiescorner.mobb.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.mobb.MobB;
import dev.cammiescorner.mobb.common.entities.PhantomEntity;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.resources.ResourceLocation;

public class PhantomEntityRenderer extends MobRenderer<PhantomEntity, PhantomModel<PhantomEntity>> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(MobB.id("phantom"), "main");
	private static final ResourceLocation PHANTOM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/phantom.png");

	public PhantomEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new PhantomModel<>(context.bakeLayer(ModelLayers.PHANTOM)), 0.75f);
		this.addLayer(new PhantomEyesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(PhantomEntity entity) {
		return PHANTOM_LOCATION;
	}

	@Override
	protected void scale(PhantomEntity livingEntity, PoseStack poseStack, float partialTickTime) {
		int i = livingEntity.getPhantomSize();
		float f = 1.0F + 0.15F * (float) i;
		poseStack.scale(f, f, f);
		poseStack.translate(0.0F, 1.3125F, 0.1875F);
	}

	@Override
	protected void setupRotations(PhantomEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
		super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
		poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
	}
}
