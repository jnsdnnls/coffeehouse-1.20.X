package net.jdonthatrack.coffeehouse.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static ItemGroup COFFEEHOUSE  = Registry.register(Registries.ITEM_GROUP, new Identifier(CoffeeHouse.MOD_ID, "coffeehouse"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.coffeehouse.creativetab"))
            .icon(() -> new ItemStack(ModItems.UNDEFINIUM)).entries((displayContext, entries) -> {
                    entries.add(ModItems.CUSTOM_HELMET);
                    entries.add(ModItems.CUSTOM_CHESTPLATE);
                    entries.add(ModItems.CUSTOM_LEGGINGS);
                    entries.add(ModItems.CUSTOM_BOOTS);

                    entries.add(ModItems.UNDEFINIUM);
                    entries.add(ModItems.UNDEFINIUM_SHARD);

                    entries.add(ModBlocks.UNDEFINIUM_BLOCK);
                    entries.add(ModBlocks.UNDEFINIUM_ORE);
                    entries.add(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE);

                    entries.add(ModBlocks.DEFINING_TABLE);


                }).build());

    public static void registerItemGroups() {
        // ADD ITEM TO EXISTING TAB
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {

        });
    }
}
