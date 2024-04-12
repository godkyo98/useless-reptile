package nordmods.uselessreptile.client.renderer.layers;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class URDragonGlowingLayer<T extends URDragonEntity> extends URGlowingLayer<T> {
    public URDragonGlowingLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }
    @Override
    public void render(MatrixStack matrixStackIn, T animatable, BakedGeoModel model, RenderLayer renderType,
                       VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        if (URClientConfig.getConfig().disableEmissiveTextures) return;

        if (!ResourceUtil.isResourceReloadFinished) {
            animatable.getAssetCache().setGlowLayerLocationCache(null);
            return;
        }

        Identifier id = getGlowingTexture(animatable);
        if (!ResourceUtil.doesExist(id)) return;

        RenderLayer cameo =  RenderLayer.getEyes(id);
        getRenderer().reRender(getDefaultBakedModel(animatable), matrixStackIn, bufferSource, animatable, cameo,
                bufferSource.getBuffer(cameo), partialTick, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                1, 1, 1, 1);
    }

    protected Identifier getGlowingTexture(T animatable) {
        if (animatable.getAssetCache().getGlowLayerLocationCache() != null) return animatable.getAssetCache().getGlowLayerLocationCache();
        Identifier id = super.getGlowingTexture(animatable);
        animatable.getAssetCache().setGlowLayerLocationCache(id);
        return id;
    }
}
