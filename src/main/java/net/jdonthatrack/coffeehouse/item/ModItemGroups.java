package net.jdonthatrack.coffeehouse.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.custom.DynamicModelItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static ItemGroup COFFEEHOUSE  = Registry.register(Registries.ITEM_GROUP, new Identifier(CoffeeHouse.MOD_ID, "coffeehouse"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.coffeehouse.creativetab"))
            .icon(() -> new ItemStack(ModItems.UNDEFINIUM)).entries((displayContext, entries) -> {

                    ItemStack[] armorPieces = {
                            new ItemStack(ModItems.CUSTOM_HELMET),
                            new ItemStack(ModItems.CUSTOM_CHESTPLATE),
                            new ItemStack(ModItems.CUSTOM_LEGGINGS),
                            new ItemStack(ModItems.CUSTOM_BOOTS)
                    };

                    for (ItemStack stack : armorPieces) {
                        DynamicModelItem.setModel(stack, "undefined"); // Replace with your actual model value
                        entries.add(stack);
                    }

                    entries.add(ModItems.UNDEFINIUM);
                    entries.add(ModItems.UNDEFINIUM_SHARD);

                    entries.add(ModBlocks.UNDEFINIUM_BLOCK);
                    entries.add(ModBlocks.UNDEFINIUM_ORE);
                    entries.add(ModBlocks.DEEPSLATE_UNDEFINIUM_ORE);

                    entries.add(ModBlocks.DEFINING_TABLE);

                    entries.add(ModItems.CUSTOM_SPAWN_EGG);

                    entries.add(ModItems.JESTER_LULLABY_MUSIC_DISC);

                }).build());

    public static void registerItemGroups() {
        // ADD ITEM TO EXISTING TAB
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {

        });
    }
}
