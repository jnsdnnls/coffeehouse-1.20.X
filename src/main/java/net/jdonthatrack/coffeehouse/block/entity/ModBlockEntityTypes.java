package net.jdonthatrack.coffeehouse.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {
    public static final BlockEntityType<DefiningTableBlockEntity> DEFINING_TABLE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CoffeeHouse.MOD_ID, "defining_table"),
                    FabricBlockEntityTypeBuilder.create(DefiningTableBlockEntity::new,
                            ModBlocks.DEFINING_TABLE).build());

    public static void registerModBlockEntityTypes() {
        CoffeeHouse.LOGGER.info("Registering ModBlockEntityTypes for " + CoffeeHouse.MOD_ID);
    }
}