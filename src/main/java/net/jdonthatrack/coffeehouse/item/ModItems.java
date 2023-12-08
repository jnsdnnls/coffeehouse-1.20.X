package net.jdonthatrack.coffeehouse.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final List<String> VALID_MODELS = List.of("amethyst_armor", "torch_armor");

    public static final Item UNDEFINIUM = registerItem("undefinium", new Item(new FabricItemSettings()));
    public static final Item UNDEFINIUM_SHARD = registerItem("undefinium_shard", new Item(new FabricItemSettings()));
    public static final Item RAPTOR_SPAWN_EGG = registerItem("raptor_spawn_egg", new SpawnEggItem(ModEntities.RAPTOR, 0x4BFDFB, 0xfddc4b, new FabricItemSettings()));
    public static final Item WINDIGO_SPAWN_EGG = registerItem("windigo_spawn_egg", new SpawnEggItem(ModEntities.WINDIGO, 0x6D1F1E, 0xFDEFD4, new FabricItemSettings()));
    public static final Item EARTH_GOLEM_SPAWN_EGG = registerItem("earth_golem_spawn_egg", new SpawnEggItem(ModEntities.EARTH_GOLEM, 0x6D1F1E, 0xFDEFD4, new FabricItemSettings()));
    public static final Item DIRE_WOLF_SPAWN_EGG = registerItem("dire_wolf_spawn_egg", new SpawnEggItem(ModEntities.DIRE_WOLF, 0x6D1F1E, 0xFDEFD4, new FabricItemSettings()));
    public static final Item CUSTOM_HELMET = registerItem("custom_helmet", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item CUSTOM_CHESTPLATE = registerItem("custom_chestplate", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item CUSTOM_LEGGINGS = registerItem("custom_leggings", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item CUSTOM_BOOTS = registerItem("custom_boots", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CoffeeHouse.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CoffeeHouse.LOGGER.info("Registering ModItems for " + CoffeeHouse.MOD_ID);
    }
}
