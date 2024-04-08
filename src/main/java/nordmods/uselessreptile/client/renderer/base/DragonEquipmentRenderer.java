package nordmods.uselessreptile.client.renderer.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import nordmods.uselessreptile.client.model.special.DragonEqupmentModel;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class DragonEquipmentRenderer extends GeoObjectRenderer<DragonEquipmentAnimatable> {
    public DragonEquipmentRenderer() {
        super(new DragonEqupmentModel());
    }

    //todo update old equipment models to have proper size
    public void render(MatrixStack poseStack, DragonEquipmentAnimatable animatable, @Nullable VertexConsumerProvider bufferSource, @Nullable RenderLayer renderType,
                       @Nullable VertexConsumer buffer, int packedLight) {
        poseStack.push();
        float partialTick = MinecraftClient.getInstance().getTickDelta();
        URDragonEntity owner = animatable.owner;
        float yaw = MathHelper.lerpAngleDegrees(partialTick, owner.prevBodyYaw, owner.bodyYaw);
        if (owner.isFrozen())
            yaw += (float)(Math.cos(owner.age * 3.25d) * Math.PI * 0.4d);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - yaw));
        if (owner.deathTime > 0) {
            float deathRotation = (owner.deathTime + partialTick - 1f) / 20f * 1.6f;
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(Math.min(MathHelper.sqrt(deathRotation), 1) * 90f));
        }
        poseStack.translate(-0.5, -0.5, -0.5); //for some weird reason matrix is offset by default
        defaultRender(poseStack, animatable, bufferSource, renderType, buffer, 0, partialTick, packedLight);
        poseStack.pop();
    }
}
