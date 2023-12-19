package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.block.custom.UndefinedCandyCropBlock;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.UNDEFINIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.UNDEFINIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE);

        blockStateModelGenerator.registerCrop(ModBlocks.UNDEFINED_CANDY_CROP, UndefinedCandyCropBlock.AGE, 0, 1, 2, 3, 4);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.UNDEFINIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.UNDEFINED_CANDY, Models.GENERATED);
        itemModelGenerator.register(ModItems.UNDEFINIUM_SHARD, Models.GENERATED);
        itemModelGenerator.register(ModItems.CUSTOM_SPAWN_EGG, Models.GENERATED);

        itemModelGenerator.register(ModItems.JESTER_LULLABY_MUSIC_DISC, Models.GENERATED);
    }
}
