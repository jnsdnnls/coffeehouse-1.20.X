package net.jdonthatrack.coffeehouse.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.block.custom.DefiningTableBlock;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block UNDEFINIUM_BLOCK = registerBlock("undefinium_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block UNDEFINIUM_ORE = registerBlock("undefinium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_UNDEFINIUM_ORE = registerBlock("deepslate_undefinium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(3, 7)));

    public static final Block DEFINING_TABLE = registerBlock("defining_table",
            new DefiningTableBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(CoffeeHouse.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(CoffeeHouse.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        CoffeeHouse.LOGGER.info("Registering ModBlocks for " + CoffeeHouse.MOD_ID);
    }
}
