package nordmods.uselessreptile.common.entity.ai.control;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.util.math.MathHelper;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;

public class DragonLookControl extends LookControl {
    protected final URDragonEntity entity;
    public DragonLookControl(URDragonEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    protected boolean shouldStayHorizontal() {
        return false;
    }

    public boolean isLookingAtTarget() {
        float pitch = getTargetPitch().orElse(0f);
        float yaw = getTargetYaw().orElse(0f);

        return Math.abs(entity.getPitch() - pitch) < entity.getPitchLimit()
                && Math.abs((entity.getYawWithProgress() - yaw) % 360) < entity.getRotationSpeed();
    }

    public boolean canLookAtTarget() {
        float pitch = getTargetPitch().orElse(0f);
        return Math.abs(pitch) < entity.getPitchLimit() / 1.25f;
    }

    public void tick() {
        if (lookAtTimer > 0) {
            --lookAtTimer;
            float pitch = getTargetPitch().orElse(0f);
            float yaw = getTargetYaw().orElse(0f);
            entity.setRotation(yaw, pitch);
        } else {
            entity.setHeadYaw(changeAngle(entity.getHeadYaw(),entity.getBodyYaw(), entity.getMaxHeadRotation()));
        }

        entity.setHeadYaw(MathHelper.clampAngle(entity.getHeadYaw(),entity.getBodyYaw(), entity.getMaxHeadRotation()));
    }
}
