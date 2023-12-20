package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.block.custom.UndefinedCandyCropBlock;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.UNDEFINIUM_BLOCK);
        addDrop(ModBlocks.DEFINING_TABLE);

        addDrop(ModBlocks.UNDEFINIUM_ORE, copperLikeOreDrops(ModBlocks.UNDEFINIUM_ORE, ModItems.UNDEFINIUM_SHARD, 1.0F, 2.0F));
        addDrop(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE, copperLikeOreDrops(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE, ModItems.UNDEFINIUM_SHARD, 1.0F, 2.0F));

        BlockStatePropertyLootCondition.Builder builder = BlockStatePropertyLootCondition.builder(ModBlocks.UNDEFINED_CANDY_CROP).properties(StatePredicate.Builder.create()
                .exactMatch(UndefinedCandyCropBlock.AGE, 4));
        addDrop(ModBlocks.UNDEFINED_CANDY_CROP, modCropDrops(ModBlocks.UNDEFINED_CANDY_CROP, ModItems.UNDEFINED_CANDY, ModItems.UNDEFINED_CANDY_SEEDS, builder));
    }

    public LootTable.Builder copperLikeOreDrops(Block drop, Item item, Float minItem, Float maxItem) {
        return dropsWithSilkTouch(drop, this.applyExplosionDecay(drop,
                ItemEntry.builder(item)
                        .apply(SetCountLootFunction
                                .builder(UniformLootNumberProvider
                                        .create(minItem, maxItem)))
                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

    public LootTable.Builder modCropDrops(Block crop, Item product, Item seeds, LootCondition.Builder condition) {
        return this.applyExplosionDecay(crop, LootTable.builder()
                .pool(LootPool.builder().with((ItemEntry.builder(product).conditionally(condition))
                        .alternatively(ItemEntry.builder(seeds)))).pool(LootPool.builder()
                        .conditionally(condition).with(ItemEntry.builder(seeds)
                        .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.05F, 1)))));
    }
}
