package nordmods.uselessreptile.client.util;

public class DragonAssetCache extends AssetCache {
    private DragonEquipmentAnimatable[] equipmentAnimatables = new DragonEquipmentAnimatable[4];

    @Override
    public void cleanCache() {
        super.cleanCache();
        for (DragonEquipmentAnimatable dragonEquipmentAnimatable : equipmentAnimatables) {
            if (dragonEquipmentAnimatable != null) {
                dragonEquipmentAnimatable.getAssetCache().cleanCache();
                dragonEquipmentAnimatable.equipmentBones.clear();
            }
        }
        equipmentAnimatables = new DragonEquipmentAnimatable[4];
    }

    public void setEquipmentAnimatable(int slot, DragonEquipmentAnimatable equipmentAnimatable) {
        if (slot < equipmentAnimatables.length) equipmentAnimatables[slot] = equipmentAnimatable;
    }

    public DragonEquipmentAnimatable getEquipmentAnimatable(int slot) {
        if (slot >= equipmentAnimatables.length) return null;
        return equipmentAnimatables[slot];
    }
}