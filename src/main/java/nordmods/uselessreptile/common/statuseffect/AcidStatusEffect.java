package nordmods.uselessreptile.common.statuseffect;

import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import nordmods.uselessreptile.common.init.URDamageTypes;
import nordmods.uselessreptile.common.init.URSounds;
import nordmods.uselessreptile.common.network.URPacketHelper;

public class AcidStatusEffect extends URStatusEffect {
    public AcidStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 10085398);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {return true;}

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!CommonProtection.canDamageEntity(entity.getWorld(), entity, CommonProtection.UNKNOWN, null)) return false;

        int armorUnequipped = 0;
        if (entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) armorUnequipped++;
        if (entity.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) armorUnequipped++;
        if (entity.getEquippedStack(EquipmentSlot.LEGS).isEmpty()) armorUnequipped++;
        if (entity.getEquippedStack(EquipmentSlot.FEET).isEmpty()) armorUnequipped++;
        if (entity.getWorld().getTickOrder() % 10 == 0) {
            entity.damage(entity.getDamageSources().create(URDamageTypes.ACID),
                    amplifier * (1 + armorUnequipped) / 3f);
            URPacketHelper.playSound(entity, URSounds.ACID_BURN, SoundCategory.AMBIENT, 1, 1, 5);
        }
        if (entity.getWorld().getTickOrder() % 20 == 0) entity.damageArmor(
                entity.getDamageSources().create(URDamageTypes.ACID),
                amplifier * (1 + armorUnequipped) * 2);

        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        URPacketHelper.playSound(entity, URSounds.ACID_SPLASH, SoundCategory.AMBIENT, 1 ,1, 1);
    }
}
