package net.jdonthatrack.coffeehouse.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<RaptorEntity> RAPTOR = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "raptor"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RaptorEntity::new).dimensions(EntityDimensions.fixed(1.2f, 1.6f)).build());

    public static final EntityType<WindigoEntity> WINDIGO = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "windigo"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WindigoEntity::new).dimensions(EntityDimensions.fixed(1.5f, 2.2f)).build());

    public static final EntityType<EarthGolemEntity> EARTH_GOLEM = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "earth_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EarthGolemEntity::new).dimensions(EntityDimensions.fixed(1.5f, 2.6f)).build());

    public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "dire_wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DireWolfEntity::new).dimensions(EntityDimensions.fixed(1.4f, 1.5f)).build());

    public static final EntityType<UnicycleEntity> UNICYCLE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "unicycle"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, UnicycleEntity::new).dimensions(EntityDimensions.fixed(1f, 1.2f)).build());
}
