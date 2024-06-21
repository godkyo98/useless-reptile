package nordmods.uselessreptile.datagen.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryWrapper;
import nordmods.uselessreptile.UselessReptile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class URDamageTypeProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private static final Set<DamageType> damageTypes = new HashSet<>();

    public URDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "damage_type");
        this.registryLookupFuture = registryLookupFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture.thenCompose((registryLookupFuture) -> {
            addEntries(registryLookupFuture);
            List<CompletableFuture<?>> list = new ArrayList<>();
            damageTypes.forEach(entry -> {
                Path path = this.pathResolver.resolveJson(UselessReptile.id(entry.msgId()));
                list.add(DataProvider.writeCodecToPath(writer, registryLookupFuture, DamageType.CODEC, entry, path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    private static void addEntries(RegistryWrapper.WrapperLookup registryLookupFuture) {
        damageTypes.add(new DamageType("acid", DamageScaling.NEVER, 0));
    }

    @Override
    public String getName() {
        return "Damage Type";
    }
}
