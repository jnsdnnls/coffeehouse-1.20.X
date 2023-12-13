package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.DEFINING_TABLE, 1)
                .pattern("SSS")
                .pattern("SCS")
                .pattern("NNN")
                .input('S', ModItems.UNDEFINIUM)
                .input('C', Blocks.CRAFTING_TABLE)
                .input('N', Items.NETHERITE_INGOT)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.DEFINING_TABLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CUSTOM_SPAWN_EGG, 1)
                .pattern("SAS")
                .pattern("UEU")
                .pattern("SHS")
                .input('S', ModItems.UNDEFINIUM_SHARD)
                .input('U', ModItems.UNDEFINIUM)
                .input('E', Items.EGG)
                .input('A', Items.GOLDEN_APPLE)
                .input('H', Items.RABBIT_HIDE)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CUSTOM_SPAWN_EGG)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CUSTOM_HELMET, 1)
                .pattern("UBU")
                .pattern("EGE")
                .pattern("UXU")
                .input('U', ModItems.UNDEFINIUM)
                .input('X', ModBlocks.UNDEFINIUM_BLOCK)
                .input('B', Items.DRAGON_BREATH)
                .input('E', Items.ENDER_PEARL)
                .input('G', Items.NETHERITE_HELMET)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CUSTOM_HELMET)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CUSTOM_CHESTPLATE, 1)
                .pattern("UBU")
                .pattern("EGE")
                .pattern("UXU")
                .input('U', ModItems.UNDEFINIUM)
                .input('X', ModBlocks.UNDEFINIUM_BLOCK)
                .input('B', Items.DRAGON_BREATH)
                .input('E', Items.ENDER_PEARL)
                .input('G', Items.NETHERITE_CHESTPLATE)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CUSTOM_CHESTPLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CUSTOM_LEGGINGS, 1)
                .pattern("UBU")
                .pattern("EGE")
                .pattern("UXU")
                .input('U', ModItems.UNDEFINIUM)
                .input('X', ModBlocks.UNDEFINIUM_BLOCK)
                .input('B', Items.DRAGON_BREATH)
                .input('E', Items.ENDER_PEARL)
                .input('G', Items.NETHERITE_LEGGINGS)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CUSTOM_LEGGINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CUSTOM_BOOTS, 1)
                .pattern("UBU")
                .pattern("EGE")
                .pattern("UXU")
                .input('U', ModItems.UNDEFINIUM)
                .input('X', ModBlocks.UNDEFINIUM_BLOCK)
                .input('B', Items.DRAGON_BREATH)
                .input('E', Items.ENDER_PEARL)
                .input('G', Items.NETHERITE_BOOTS)
                .criterion(hasItem(ModItems.UNDEFINIUM), conditionsFromItem(ModItems.UNDEFINIUM))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CUSTOM_BOOTS)));
    }
}
