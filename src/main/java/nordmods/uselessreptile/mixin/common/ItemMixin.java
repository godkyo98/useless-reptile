package nordmods.uselessreptile.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Language;
import nordmods.uselessreptile.common.event.DragonEquipmentTooltipEntryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract Item asItem();

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void addDragonEquipmentEntries(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        List<EntityType<? extends Entity>> entries = new ArrayList<>(DragonEquipmentTooltipEntryEvent.EVENT.invoker().getEntries(asItem()));
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
