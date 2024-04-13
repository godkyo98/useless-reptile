package nordmods.uselessreptile.client.renderer.base;

import net.minecraft.client.render.entity.EntityRendererFactory;
import nordmods.uselessreptile.client.renderer.layers.DragonPassengerLayer;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import software.bernie.geckolib.model.GeoModel;

public abstract class URRideableDragonRenderer<T extends URRideableDragonEntity> extends URDragonRenderer<T> {
    protected final String riderBone;
    public URRideableDragonRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model, boolean hasBanner, boolean hasArmor, String riderBone) {
        super(renderManager, model, hasBanner);
        this.riderBone = riderBone;
        addRenderLayer(new DragonPassengerLayer<>(this, riderBone));
    }
}
