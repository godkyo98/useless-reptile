package nordmods.uselessreptile.datagen;

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
     * @see DragonSpawn.Builder#create()
     */
    public void addSpawnEntries(RegistryWrapper.WrapperLookup registryLookup) {
        addEntry(UREntities.WYVERN_ENTITY, "green", 1, URTags.WYVERN_SPAWN_WHITELIST, URTags.WYVERN_SPAWN_BLACKLIST, URTags.WYVERN_SPAWNABLE_ON);
        addEntry(UREntities.WYVERN_ENTITY, "brown", 1, URTags.WYVERN_SPAWN_WHITELIST, URTags.WYVERN_SPAWN_BLACKLIST, URTags.WYVERN_SPAWNABLE_ON);

        addEntry(UREntities.MOLECLAW_ENTITY, "black", 13, URTags.MOLECLAW_SPAWN_WHITELIST, URTags.MOLECLAW_SPAWN_BLACKLIST, URTags.MOLECLAW_SPAWNABLE_ON);
        addEntry(UREntities.MOLECLAW_ENTITY, "brown", 13, URTags.MOLECLAW_SPAWN_WHITELIST, URTags.MOLECLAW_SPAWN_BLACKLIST, URTags.MOLECLAW_SPAWNABLE_ON);
        addEntry(UREntities.MOLECLAW_ENTITY, "grey", 13, URTags.MOLECLAW_SPAWN_WHITELIST, URTags.MOLECLAW_SPAWN_BLACKLIST, URTags.MOLECLAW_SPAWNABLE_ON);
        addEntry(UREntities.MOLECLAW_ENTITY, "albino", 1, URTags.MOLECLAW_SPAWN_WHITELIST, URTags.MOLECLAW_SPAWN_BLACKLIST, URTags.MOLECLAW_SPAWNABLE_ON);

        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "blue", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_blue", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "green", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_green", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "purple", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_purple", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "teal", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_teal", 1, URTags.RIVER_PIKEHORN_SPAWN_WHITELIST, URTags.RIVER_PIKEHORN_SPAWN_BLACKLIST, URTags.RIVER_PIKEHORN_SPAWNABLE_ON);

        DragonSpawn lightningChaserBlue = DragonSpawn.Builder.create()
                .setVariant("blue")
                .setWeight(9)
                .addAllowedBiomeTag(ConventionalBiomeTags.IS_OCEAN)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserBrown = DragonSpawn.Builder.create()
                .setVariant("brown")
                .setWeight(9)
                .addAllowedBiomeTag(ConventionalBiomeTags.IS_DRY)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserGrey = DragonSpawn.Builder.create()
                .setVariant("brown")
                .setWeight(9)
                .addAllowedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_WHITELIST)
                .addBannedBiomeTag(URTags.LIGHTNING_CHASER_SPAWN_BLACKLIST)
                .addBannedBiomeTag(ConventionalBiomeTags.IS_OCEAN)
                .addBannedBiomeTag(ConventionalBiomeTags.IS_DRY)
                .addAllowedBlockTag(URTags.LIGHTNING_CHASER_SPAWNABLE_ON)
                .setMinAltitude(64)
                .build();

        DragonSpawn lightningChaserPurple = DragonSpawn.Builder.create()
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

    protected void addEntry(EntityType<? extends URDragonEntity> type, String variant, int weight, TagKey<Biome> allowedBiomes, TagKey<Biome> bannedBiomes, TagKey<Block> allowedBlocks) {
        DragonSpawn spawn = DragonSpawn.Builder.create()
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
