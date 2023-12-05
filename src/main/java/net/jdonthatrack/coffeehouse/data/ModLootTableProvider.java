package net.jdonthatrack.coffeehouse.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.UNDEFINIUM_BLOCK);

        addDrop(ModBlocks.UNDEFINIUM_ORE, copperLikeOreDrops(ModBlocks.UNDEFINIUM_ORE, ModItems.UNDEFINIUM_SHARD, 1.0F, 2.0F));
        addDrop(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE, copperLikeOreDrops(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE, ModItems.UNDEFINIUM_SHARD, 1.0F, 2.0F));
    }

    public LootTable.Builder copperLikeOreDrops(Block drop, Item item, Float minItem, Float maxItem) {
        return dropsWithSilkTouch(drop,(LootPoolEntry.Builder)this.applyExplosionDecay(drop,
                ItemEntry.builder(item)
                        .apply(SetCountLootFunction
                                .builder(UniformLootNumberProvider
                                        .create(minItem, maxItem)))
                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }
}
