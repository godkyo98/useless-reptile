package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.*;
import nordmods.uselessreptile.common.entity.special.ShockwaveSphereEntity;
import nordmods.uselessreptile.common.entity.special.WyvernProjectileEntity;


public class UREntities {

    public static final EntityType<WyvernEntity> WYVERN_ENTITY =
            register("wyvern", getBuilder(SpawnGroup.CREATURE, WyvernEntity::new, 1, 1));
    public static final EntityType<MoleclawEntity> MOLECLAW_ENTITY =
            register("moleclaw", getBuilder(SpawnGroup.CREATURE, MoleclawEntity::new, MoleclawEntity.defaultWidth, MoleclawEntity.defaultHeight));
    public static final EntityType<RiverPikehornEntity> RIVER_PIKEHORN_ENTITY =
            register("river_pikehorn", getBuilder(SpawnGroup.CREATURE, RiverPikehornEntity::new, 1, 1));
    public static final EntityType<LightningChaserEntity> LIGHTNING_CHASER_ENTITY =
            register("lightning_chaser", getBuilder(SpawnGroup.CREATURE, LightningChaserEntity::new, 1, 1));
    public static final EntityType<WyvernProjectileEntity> WYVERN_PROJECTILE_ENTITY =
            register("wyvern_projectile", getBuilder(SpawnGroup.MISC, WyvernProjectileEntity::new, 0.5f, 0.5f, true));
    public static final EntityType<ShockwaveSphereEntity> SHOCKWAVE_SPHERE_ENTITY =
            register("shockwave_sphere", getBuilder(SpawnGroup.MISC, ShockwaveSphereEntity::new, 1, 1, false));


    public static void init(){
        FabricDefaultAttributeRegistry.register(WYVERN_ENTITY, WyvernEntity.createWyvernAttributes());
        FabricDefaultAttributeRegistry.register(MOLECLAW_ENTITY, MoleclawEntity.createMoleclawAttributes());
        FabricDefaultAttributeRegistry.register(RIVER_PIKEHORN_ENTITY, RiverPikehornEntity.createPikehornAttributes());
        FabricDefaultAttributeRegistry.register(LIGHTNING_CHASER_ENTITY, LightningChaserEntity.createLightningChaserAttributes());
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(UselessReptile.MODID, id), builder.build());
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> getBuilder(SpawnGroup spawnGroup, EntityType.EntityFactory<T> entity, float width, float height, boolean disableSummon) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(spawnGroup, entity).dimensions(EntityDimensions.changing(width, height));
        return disableSummon ? builder.disableSummon() : builder;
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> getBuilder(SpawnGroup spawnGroup, EntityType.EntityFactory<T> entity, float width, float height) {
        return getBuilder(spawnGroup, entity, width, height, false);
    }
}

