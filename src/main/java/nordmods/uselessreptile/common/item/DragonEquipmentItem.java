package nordmods.uselessreptile.common.item;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.event.DragonEquipmentTooltipEntryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DragonEquipmentItem extends Item {

    private final Supplier<AttributeModifiersComponent> dragonEquipmentAttributes;

    public DragonEquipmentItem(Supplier<AttributeModifiersComponent> dragonEquipmentAttributes, net.minecraft.item.Item.Settings settings) {
        super(settings);
        this.dragonEquipmentAttributes = dragonEquipmentAttributes;
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return dragonEquipmentAttributes.get();
    }

    public static Identifier equipmentModifierID(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HEAD -> UselessReptile.id("dragon_head_equipment");
            case LEGS -> UselessReptile.id("dragon_leg_equipment");
            case CHEST -> UselessReptile.id("dragon_chest_equipment");
            case FEET -> UselessReptile.id("dragon_feet_equipment");
            case BODY -> UselessReptile.id("dragon_body_equipment");
            case OFFHAND -> UselessReptile.id("dragon_offhand_equipment");
            case MAINHAND -> UselessReptile.id("dragon_mainhand_equipment");
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        List<EntityType<? extends Entity>> entries = new ArrayList<>(DragonEquipmentTooltipEntryEvent.EVENT.invoker().getEntries(this));
        if (entries.isEmpty()) return;

        String values = "";
        Language language = Language.getInstance();
        for (EntityType<?> entityType : entries) {
            String entry = language.get(entityType.getTranslationKey());
            values = values.concat(entry).concat(", ");
        }
        values = values.substring(0, values.length() - 2);

        tooltip.add(Text.translatable("tooltip.uselessreptile.can_be_equipped_by", values).withColor(Colors.LIGHT_GRAY));
    }
}
