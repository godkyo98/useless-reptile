package nordmods.uselessreptile.client.model.special;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.client.util.model_data.ModelDataUtil;
import nordmods.uselessreptile.client.util.model_data.base.EquipmentModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class DragonEqupmentModel extends GeoModel<DragonEquipmentAnimatable> {

    @Override
    @Nullable
    public Identifier getModelResource(DragonEquipmentAnimatable entity) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null && ResourceUtil.doesExist(data.modelData().model())) return data.modelData().model();

        return null;
    }

    @Override
    @Nullable
    public Identifier getTextureResource(DragonEquipmentAnimatable entity){
        if (!ResourceUtil.isResourceReloadFinished) return null;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null && ResourceUtil.doesExist(data.modelData().texture())) return data.modelData().texture();

        return null;
    }

    @Override
    @Nullable
    public Identifier getAnimationResource(DragonEquipmentAnimatable entity) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null && ResourceUtil.doesExist(data.modelData().animation())) return data.modelData().animation();

        return new Identifier("uselessreptile:animations/entity/empty.animation.json");
    }

    @Override
    public RenderLayer getRenderType(DragonEquipmentAnimatable entity, Identifier texture) {
        EquipmentModelData data = ModelDataUtil.getEquipmentModelData(entity.owner, entity.item);
        if (data != null) return data.modelData().renderType();
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}
