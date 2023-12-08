package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerParentedItemModel(ModItems.RAPTOR_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.WINDIGO_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.EARTH_GOLEM_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.DIRE_WOLF_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.UNDEFINIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.UNDEFINIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.UNDEFINIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.UNDEFINIUM_SHARD, Models.GENERATED);
    }
}
