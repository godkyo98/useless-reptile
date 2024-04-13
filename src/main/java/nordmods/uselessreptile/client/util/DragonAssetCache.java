package nordmods.uselessreptile.client.util;

public class DragonAssetCache extends AssetCache {
    private DragonEquipmentAnimatable[] equipmentAnimatables = {null, null, null, null};
    private boolean nametagModel;

    @Override
    public void cleanCache() {
        super.cleanCache();
        for (DragonEquipmentAnimatable dragonEquipmentAnimatable : equipmentAnimatables) {
            if (dragonEquipmentAnimatable != null) dragonEquipmentAnimatable.equipmentBones.clear();
        }
        equipmentAnimatables = new DragonEquipmentAnimatable[] {null, null, null, null};
        nametagModel = false;
    }

    public void setEquipmentAnimatable(int slot, DragonEquipmentAnimatable equipmentAnimatable) {
        if (slot < equipmentAnimatables.length) equipmentAnimatables[slot] = equipmentAnimatable;
    }

    public DragonEquipmentAnimatable getEquipmentAnimatable(int slot) {
        if (slot >= equipmentAnimatables.length) return null;
        return equipmentAnimatables[slot];
    }


    public boolean isNametagModel() {
        return nametagModel;
    }

    public void setNametagModel(boolean state) {
        nametagModel = state;
    }

}
