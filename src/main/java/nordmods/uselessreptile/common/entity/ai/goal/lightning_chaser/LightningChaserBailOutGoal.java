package nordmods.uselessreptile.common.entity.ai.goal.lightning_chaser;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;

import java.util.EnumSet;

public class LightningChaserBailOutGoal extends Goal {
    private final LightningChaserEntity entity;
    private BlockPos pointOfInterest;
    private int timeout = 0;

    public LightningChaserBailOutGoal(LightningChaserEntity entity) {
        this.entity = entity;
        setControls(EnumSet.allOf(Control.class));
    }

    @Override
    public boolean canStart() {
        if (entity.isTamed() || !entity.isChallenger()) return false;
        if (entity.getShouldBailOut()) {
            if (pointOfInterest == null) {
                Vec3d vec3d = entity.getRotationVector(0, entity.getYaw()).multiply(8192).add(entity.getPos());
                pointOfInterest = new BlockPos((int) vec3d.x, entity.getWorld().getHeight(), (int) vec3d.z);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (timeout > 240) return false;
        PlayerEntity closestPlayer = entity.getWorld().getClosestPlayer(entity, 8192);
        if (closestPlayer != null && pointOfInterest != null) pointOfInterest = new BlockPos(pointOfInterest.getX(), closestPlayer.getBlockY() + 40, pointOfInterest.getZ());
        else return false;
        return this.canStart();
    }

    @Override
    public void start() {
        entity.setSurrendered(false);
        entity.setIsSitting(false);
    }

    @Override
    public void stop() {
        if (canStart()) entity.discard();
    }

    @Override
    public void tick() {
        entity.getNavigation().startMovingTo(pointOfInterest.getX(), pointOfInterest.getY(), pointOfInterest.getZ(), 1);
        timeout++;
    }
}
