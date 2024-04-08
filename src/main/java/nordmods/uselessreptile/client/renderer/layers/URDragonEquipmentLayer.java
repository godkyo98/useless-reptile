package nordmods.uselessreptile.client.renderer.layers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.model.special.DragonEqupmentModel;
import nordmods.uselessreptile.client.renderer.base.DragonEquipmentRenderer;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreBakedGeoModel;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

public class URDragonEquipmentLayer<T extends URDragonEntity> extends GeoRenderLayer<T> {
    private final Map<String, CoreGeoBone> equipmentBones = new Object2ObjectOpenHashMap<>();
    private DragonEquipmentRenderer dragonEquipmentRenderer;
    private DragonEquipmentAnimatable dragonEquipmentAnimatable;
    private BakedGeoModel bakedEquipmentModel;
    private final EquipmentSlot equipmentSlot;

    public URDragonEquipmentLayer(GeoRenderer<T> entityRendererIn, EquipmentSlot equipmentSlot) {
        super(entityRendererIn);
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public void preRender(MatrixStack poseStack, T entity, BakedGeoModel bakedModel, RenderLayer renderType,
                          VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick,
                          int packedLight, int packedOverlay) {
        ItemStack itemStack = entity.getEquippedStack(equipmentSlot);
        if (itemStack.isEmpty()) {
            dragonEquipmentRenderer = null;
            bakedEquipmentModel = null;
            return;
        }

        dragonEquipmentAnimatable = new DragonEquipmentAnimatable(entity, itemStack.getItem());

        dragonEquipmentRenderer = new DragonEquipmentRenderer();
        Identifier id = dragonEquipmentRenderer.getGeoModel().getModelResource(dragonEquipmentAnimatable);
        if (!ResourceUtil.doesExist(id)) return;

        bakedEquipmentModel = dragonEquipmentRenderer.getGeoModel().getBakedModel(id);
        getSaddleBones(bakedEquipmentModel);
    }

    @Override
    public void render(MatrixStack matrixStackIn, T entity, BakedGeoModel bakedModel, RenderLayer renderType,
                       VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        if (bakedEquipmentModel == null) return;
        Identifier id = dragonEquipmentRenderer.getGeoModel().getTextureResource(dragonEquipmentAnimatable);
        if (!ResourceUtil.doesExist(id)) return;

        getGeoModel().getAnimationProcessor().getRegisteredBones().forEach(bone -> {
            GeoBone equipmentBone = (GeoBone) equipmentBones.get(bone.getName());
            if (equipmentBone != null) {
                equipmentBone.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                equipmentBone.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                equipmentBone.updatePosition(bone.getPosX(), bone.getPosY(), bone.getPosZ());
            }
        });

        RenderLayer cameo = dragonEquipmentRenderer.getGeoModel().getRenderType(dragonEquipmentAnimatable, id);
        buffer = bufferSource.getBuffer(cameo);

        dragonEquipmentRenderer.render(matrixStackIn, dragonEquipmentAnimatable, bufferSource, cameo, buffer, packedLight);
    }

    private void addChildren(CoreGeoBone bone) {
        equipmentBones.put(bone.getName(), bone);
        for (CoreGeoBone child : bone.getChildBones()) addChildren(child);
    }

    private void getSaddleBones(CoreBakedGeoModel model) {
        equipmentBones.clear();
        for (CoreGeoBone bone : model.getBones()) addChildren(bone);
    }
}
