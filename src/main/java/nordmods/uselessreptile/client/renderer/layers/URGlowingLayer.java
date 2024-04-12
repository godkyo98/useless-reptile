package nordmods.uselessreptile.client.renderer.layers;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.client.util.ResourceUtil;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class URGlowingLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    public URGlowingLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }
    @Override
    public void render(MatrixStack matrixStackIn, T animatable, BakedGeoModel model, RenderLayer renderType,
                       VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        if (URClientConfig.getConfig().disableEmissiveTextures) return;

        Identifier id = getGlowingTexture(animatable);
        if (!ResourceUtil.doesExist(id)) return;

        RenderLayer cameo =  RenderLayer.getEyes(id);
        matrixStackIn.push();
        getRenderer().reRender(getDefaultBakedModel(animatable), matrixStackIn, bufferSource, animatable, cameo,
                bufferSource.getBuffer(cameo), partialTick, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                1, 1, 1, 1);
        matrixStackIn.pop();
    }

    protected Identifier getGlowingTexture(T animatable) {
        String namespace = getTextureResource(animatable).getNamespace();
        String path = getTextureResource(animatable).getPath().replace(".png", "_glowing.png");

        return new Identifier(namespace, path);
    }
}
