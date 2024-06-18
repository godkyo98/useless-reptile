package nordmods.uselessreptile.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import nordmods.uselessreptile.datagen.tag.URBiomeTagProvider;
import nordmods.uselessreptile.datagen.tag.URBlockTagProvider;
import nordmods.uselessreptile.datagen.tag.URItemTagProvider;

public class UselessReptileDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(URItemTagProvider::new);
        pack.addProvider(URBiomeTagProvider::new);
        pack.addProvider(URBlockTagProvider::new);
        pack.addProvider(URRecipeProvider::new);
        pack.addProvider(URAdvancementProvider::new);
        pack.addProvider(URDragonSpawnProvider::new);
    }
}
