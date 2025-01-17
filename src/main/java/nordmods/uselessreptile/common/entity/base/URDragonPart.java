package nordmods.uselessreptile.common.entity.base;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import org.joml.Vector3f;

public class URDragonPart extends EntityPart {
    public final URDragonEntity owner;
    private float heightMod = 1;
    private float widthMod = 1;
    private final float damageMultiplier;

    public URDragonPart(URDragonEntity owner) {
        this(owner, 1);
    }

    public URDragonPart(URDragonEntity owner, float damageMultiplier) {
        super(owner, 1, 1);
        this.owner = owner;
        this.damageMultiplier = damageMultiplier;
        calculateDimensions();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(HEIGHT_MODIFIER, 1f);
        builder.add(WIDTH_MODIFIER, 1f);
    }

    public static final TrackedData<Float> HEIGHT_MODIFIER = DataTracker.registerData(URDragonPart.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> WIDTH_MODIFIER = DataTracker.registerData(URDragonPart.class, TrackedDataHandlerRegistry.FLOAT);

    public float getHeightMod() {return dataTracker.get(HEIGHT_MODIFIER);}
    public void setHeightMod(float state) {dataTracker.set(HEIGHT_MODIFIER, state);}

    public float getWidthMod() {return dataTracker.get(WIDTH_MODIFIER);}
    public void setWidthMod(float state) {dataTracker.set(WIDTH_MODIFIER, state);}

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        boolean riderOwner = false;
        if (damageSource.getAttacker() instanceof PlayerEntity player)
            riderOwner = player.getVehicle() == owner && owner.getOwner() == player;
        return riderOwner || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return super.damage(source, amount * damageMultiplier);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(widthMod, heightMod);
    }

    @Override
    public boolean canHit() {
        return owner.canHit();
    }

    public void setScale(float destinationHeight, float destinationWidth) {
        destinationWidth *= owner.getScale();
        destinationHeight *= owner.getScale();
        float widthMod = getWidthMod();
        float heightMod = getHeightMod();
        float widthDiff = widthMod - destinationWidth;
        float heightDiff = heightMod - destinationHeight;

        if (widthDiff != 0) {
            if (widthDiff > owner.getWidthModTransSpeed()) widthMod -= owner.getWidthModTransSpeed();
            else if (widthDiff < -owner.getWidthModTransSpeed()) widthMod += owner.getWidthModTransSpeed();
            else widthMod = destinationWidth;
        }

        if (heightDiff != 0) {
            if (heightDiff > owner.getHeightModTransSpeed()) heightMod -= owner.getHeightModTransSpeed();
            else if (heightDiff < -owner.getHeightModTransSpeed()) heightMod += owner.getHeightModTransSpeed();
            else heightMod = destinationHeight;
        }

        setHeightMod(heightMod);
        setWidthMod(widthMod);

        this.heightMod = heightMod;
        this.widthMod = widthMod;
        calculateDimensions();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void setScale(Vec2f scale) {
        setScale(scale.x, scale.y);
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x * owner.getScale(), y * owner.getScale(), z * owner.getScale(), 0, owner.getYaw());
    }

    public void setRelativePos(Vector3f vector3f) {
        setRelativePos(vector3f.x, vector3f.y, vector3f.z);
    }
}
