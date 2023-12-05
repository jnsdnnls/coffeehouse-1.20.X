package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;


public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> UNDEFINIUM_SMELTABLES = List.of(ModBlocks.UNDEFINIUM_ORE, ModBlocks.DEEPSLATE_UNDEFINIUM_ORE);
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.UNDEFINIUM, RecipeCategory.DECORATIONS, ModBlocks.UNDEFINIUM_BLOCK);

        offerSmelting(exporter, UNDEFINIUM_SMELTABLES, RecipeCategory.MISC, ModItems.UNDEFINIUM_SHARD, 0.7f, 200, "undefinium");
        offerBlasting(exporter, UNDEFINIUM_SMELTABLES, RecipeCategory.MISC, ModItems.UNDEFINIUM_SHARD, 0.7f, 100, "undefinium");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.UNDEFINIUM, 1)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .input('S', ModItems.UNDEFINIUM_SHARD)
                .criterion(hasItem(ModItems.UNDEFINIUM_SHARD), conditionsFromItem(ModItems.UNDEFINIUM_SHARD))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.UNDEFINIUM_SHARD)));
    }
}
