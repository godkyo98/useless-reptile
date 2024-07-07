package nordmods.uselessreptile.datagen.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import nordmods.uselessreptile.common.init.URTags;

import java.util.concurrent.CompletableFuture;

public class URBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public URBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(URTags.DRAGON_UNBREAKABLE).addOptional(BlockTags.AIR.id());
    }
}
