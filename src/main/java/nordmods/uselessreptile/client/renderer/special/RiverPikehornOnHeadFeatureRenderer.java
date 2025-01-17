package nordmods.uselessreptile.client.renderer.special;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import nordmods.uselessreptile.client.model.special.RiverPikehornOnHeadFeatureModel;
import nordmods.uselessreptile.client.renderer.RiverPikehornEntityRenderer;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;

public class RiverPikehornOnHeadFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final RiverPikehornOnHeadFeatureModel model;
    public RiverPikehornOnHeadFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader) {
        super(context);
        model = new RiverPikehornOnHeadFeatureModel(loader.getModelPart(RiverPikehornOnHeadFeatureModel.PIKEHORN_ON_HEAD_MODEL));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getFirstPassenger() instanceof RiverPikehornEntity dragon) {
            if (dragon.isInvisible()) return;
            matrices.push();
            getContextModel().head.rotate(matrices);
            model.setAngles(dragon, limbAngle, limbDistance, dragon.age, yaw(dragon.headYaw, entity.headYaw), MathHelper.clamp(dragon.getPitch() % 360 - entity.getPitch() % 360, -20, 20));
            model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(getDragonTexture(dragon))), light, OverlayTexture.DEFAULT_UV, 16777215);
            matrices.pop();
        }
    }

    private Identifier getDragonTexture(RiverPikehornEntity dragon) {
        RiverPikehornEntityRenderer render;
        EntityRenderDispatcher manager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        render = (RiverPikehornEntityRenderer) manager.getRenderer(dragon);

        return render.getGeoModel().getTextureResource(dragon);
    }

    private float yaw(float entityYaw, float ownerYaw) {
        float a = entityYaw % 360;
        float b = ownerYaw % 360;
        if (b < 0) b += 360;
        return MathHelper.clamp(MathHelper.wrapDegrees(a - b), -45, 45);
    }
}
