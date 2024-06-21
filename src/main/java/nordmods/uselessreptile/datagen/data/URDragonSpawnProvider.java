package nordmods.uselessreptile.datagen.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.UREntities;
import nordmods.uselessreptile.common.init.URTags;
import nordmods.uselessreptile.common.util.dragon_spawn.DragonSpawn;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class URDragonSpawnProvider implements DataProvider {

    protected final FabricDataOutput output;
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    public URDragonSpawnProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.output = output;
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "dragon_spawns");
        this.registryLookupFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture.thenCompose((registryLookupFuture) -> {
            addSpawnEntries(registryLookupFuture);
            List<CompletableFuture<?>> list = new ArrayList<>();
            DragonSpawn.getEntries().forEach(entry -> {
                String dragon = entry.getKey();
                List<DragonSpawn> spawns = entry.getValue();
                spawns.forEach(dragonSpawn -> {
                    Path path = this.pathResolver.resolveJson(getId(dragon, dragonSpawn.variant()));
                    list.add(DataProvider.writeCodecToPath(writer, registryLookupFuture, DragonSpawn.CODEC, dragonSpawn, path));
                });
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    protected Identifier getId(String dragon, String variant) {
        return UselessReptile.id(dragon + "/" + variant);
    }

    /**
     * @see DragonSpawn#addSpawn(EntityType, DragonSpawn)
     * @see DragonSpawn.Builder#builder()
     */
    public void addSpawnEntries(RegistryWrapper.WrapperLookup registryLookup) {
        addWyvernEntry("green");
        addWyvernEntry("brown");

        addMoleclawEntry("black", 13);
        addMoleclawEntry("brown", 13);
        addMoleclawEntry("grey", 13);
        addMoleclawEntry("albino", 1);

        addRiverPikehornEntry("blue");
        addRiverPikehornEntry("dark_blue");
        addRiverPikehornEntry("green");
        addRiverPikehornEntry("dark_green");
        addRiverPikehornEntry("purple");
        addRiverPikehornEntry("dark_purple");
        addRiverPikehornEntry("teal");
        addRiverPikehornEntry("dark_teal");

        DragonSpawn lightningChaserBlue = DragonSpawn.builder()
                .setVariant("blue")
                .setWeight(9)
                .addAllowedBiomeTag(ConventionalBiomeTags.IS_OCEAN)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserBrown = DragonSpawn.builder()
                .setVariant("brown")
                .setWeight(9)
                .addAllowedBiomeTag(ConventionalBiomeTags.IS_DRY)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserGrey = DragonSpawn.builder()
                .setVariant("grey")
                .setWeight(9)
                .addAllowedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_BLACKLIST)
                .addBannedBiomeTag(ConventionalBiomeTags.IS_OCEAN)
                .addBannedBiomeTag(ConventionalBiomeTags.IS_DRY)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserPurple = DragonSpawn.builder()
                .setVariant("purple")
                .setWeight(1)
                .addAllowedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_BLACKLIST)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn.addSpawn(UREntities.LIGHTNING_CHASER_ENTITY, lightningChaserGrey);
        DragonSpawn.addSpawn(UREntities.LIGHTNING_CHASER_ENTITY, lightningChaserBlue);
        DragonSpawn.addSpawn(UREntities.LIGHTNING_CHASER_ENTITY, lightningChaserBrown);
        DragonSpawn.addSpawn(UREntities.LIGHTNING_CHASER_ENTITY, lightningChaserPurple);

    }

    protected void addWyvernEntry(String variant) {
        DragonSpawn spawn = DragonSpawn.builder()
                .setVariant(variant)
                .setWeight(1)
                .addAllowedBiomeTag(URTags.WYVERN_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.WYVERN_SPAWN_BLACKLIST)
                .addAllowedBlockTag(URTags.WYVERN_SPAWNABLE_ON)
                .build();
        DragonSpawn.addSpawn(UREntities.WYVERN_ENTITY, spawn);
    }

    protected void addMoleclawEntry(String variant, int weight) {
        DragonSpawn spawn = DragonSpawn.builder()
                .setVariant(variant)
                .setWeight(weight)
                .addAllowedBiomeTag(URTags.MOLECLAW_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.MOLECLAW_SPAWN_BLACKLIST)
                .addAllowedBlockTag(URTags.MOLECLAW_SPAWNABLE_ON)
                .build();
        DragonSpawn.addSpawn(UREntities.MOLECLAW_ENTITY, spawn);
    }

    protected void addRiverPikehornEntry(String variant) {
        DragonSpawn spawn = DragonSpawn.builder()
                .setVariant(variant)
                .setWeight(1)
                .addAllowedBiomeTag(URTags.RIVER_PIKEHORN_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST)
                .addAllowedBlockTag(URTags.RIVER_PIKEHORN_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();
        DragonSpawn.addSpawn(UREntities.RIVER_PIKEHORN_ENTITY, spawn);
    }

    protected void addEntry(EntityType<? extends URDragonEntity> type, String variant, int weight, TagKey<Biome> allowedBiomes, TagKey<Biome> bannedBiomes, TagKey<Block> allowedBlocks){
        DragonSpawn spawn = DragonSpawn.builder()
                .setVariant(variant)
                .setWeight(weight)
                .addAllowedBiomeTag(allowedBiomes)
                .addBannedBiomeTag(bannedBiomes)
                .addAllowedBlockTag(allowedBlocks)
                .build();
        DragonSpawn.addSpawn(type, spawn);
    }

    @Override
    public String getName() {
        return "Dragon Spawns";
    }
}
