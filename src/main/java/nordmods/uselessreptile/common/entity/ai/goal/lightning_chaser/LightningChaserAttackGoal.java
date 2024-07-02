package nordmods.uselessreptile.common.entity.ai.goal.lightning_chaser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.common.entity.LightningChaserEntity;
import nordmods.uselessreptile.common.entity.special.LightningBreathEntity;
import nordmods.uselessreptile.common.entity.special.ShockwaveSphereEntity;

import java.util.EnumSet;
import java.util.List;

public class LightningChaserAttackGoal extends Goal {

    private final LightningChaserEntity entity;
    private LivingEntity target;
    private int attackCooldown = 20;
    private static final int MIN_DISTANCE_SQUARED = 80;
    private static final int MAX_DISTANCE_SQUARED = (int) (LightningBreathEntity.MAX_LENGTH * LightningBreathEntity.MAX_LENGTH * 0.81f);

    public LightningChaserAttackGoal(LightningChaserEntity entity) {
        this.entity = entity;
        setControls(EnumSet.of(Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (entity.hasSurrendered() || entity.getShouldBailOut()) return false;

        if (entity.canBeControlledByRider()) return false;
        if (!entity.canTarget(entity.getTarget())) entity.setTarget(null);
        target = entity.getTarget();
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        if (target == null) return false;
        if (!target.isAlive()) return false;
        return canStart();
    }

    @Override
    public void stop() {
        target = null;
        entity.setTarget(null);
        entity.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (target == null || target.isRemoved()) {
            stop();
            return;
        }
        entity.setSprinting(true);
        entity.getLookControl().lookAt(target.getX(), target.getY(), target.getZ());

        double distance = entity.squaredDistanceTo(target);
        double yDiff = target.getY() - entity.getY();
        if (yDiff > 0 && !entity.isFlying()) entity.startToFly();
        boolean canDamage = !target.isInvulnerableTo(entity.getDamageSources().create(DamageTypes.LIGHTNING_BOLT, entity));
        if (distance < MIN_DISTANCE_SQUARED && canDamage) {
            if (!entity.isFlying()) entity.startToFly();
            entity.getMoveControl().moveBack();
            if (target instanceof PlayerEntity player && yDiff < player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE)) entity.getNavigation().startMovingTo(entity.getX(), entity.getY() - yDiff + 1, entity.getZ(), 1);
        } else if (distance < MAX_DISTANCE_SQUARED && canDamage) {
            if (!entity.getLookControl().canLookAtTarget()) {
                entity.getNavigation().startMovingTo(entity.getX(), entity.getY() + yDiff, entity.getZ(), 1);
            }
            else entity.getNavigation().stop();
        } else entity.getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), 1);

        if (--attackCooldown <= 0) {
            if (tryMeleeAttack()) return;
            if (canDamage) {
                if (tryRangedAttack()) return;
                if (tryShockwaveAttack()) return;
            }
        }
    }

    private boolean tryMeleeAttack() {
        if (entity.getSecondaryAttackCooldown() > 0) return false;
        if (entity.isFlying()) return false;
        boolean doesCollide = entity.doesCollide(entity.getAttackBox(), target.getBoundingBox());
        if (!doesCollide) return false;
        entity.meleeAttack(target);
        attackCooldown = 30;
        return true;
    }

    private boolean tryRangedAttack() {
        if (entity.getPrimaryAttackCooldown() > 0) return false;
        if (!entity.getLookControl().isLookingAtTarget()) return false;
        double distance = entity.squaredDistanceTo(target);
        if (distance > MAX_DISTANCE_SQUARED || distance < MIN_DISTANCE_SQUARED) return false;
        entity.triggerShoot();
        attackCooldown = 40;
        return true;
    }

    private boolean tryShockwaveAttack() {
        if (entity.getSpecialAttackCooldown() > 0) return false;
        if (!entity.isFlying()) return false;
        double attackDistance = ShockwaveSphereEntity.MAX_RADIUS * ShockwaveSphereEntity.MAX_RADIUS * 0.49;
        List<Entity> projectiles = entity.getWorld().getOtherEntities(entity, new Box(entity.getBlockPos()).expand(attackDistance * 2), c -> c instanceof ProjectileEntity projectile && projectile.getOwner() == target && !projectile.getVelocity().equals(Vec3d.ZERO));
        if (!projectiles.isEmpty()) {
            entity.triggerShockwave();
            return true;
        }
        double distance = entity.squaredDistanceTo(target);
        if (attackDistance < distance) return false;
        entity.triggerShockwave();
        attackCooldown = 40;
        return true;
    }
}
