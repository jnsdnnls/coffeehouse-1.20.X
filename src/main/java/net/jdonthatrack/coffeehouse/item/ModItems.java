package net.jdonthatrack.coffeehouse.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.jdonthatrack.coffeehouse.item.custom.DynamicSpawnEggItem;
import net.jdonthatrack.coffeehouse.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final List<String> VALID_MODELS = List.of("amethyst", "torch");

    public static final Item UNDEFINIUM = registerItem("undefinium", new Item(new FabricItemSettings()));
    public static final Item UNDEFINIUM_SHARD = registerItem("undefinium_shard", new Item(new FabricItemSettings()));

    public static final Item CUSTOM_SPAWN_EGG = registerItem("custom_spawn_egg", new DynamicSpawnEggItem(EntityType.HORSE, new FabricItemSettings()));

    public static final Item CUSTOM_HELMET = registerItem("custom_helmet", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item CUSTOM_CHESTPLATE = registerItem("custom_chestplate", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item CUSTOM_LEGGINGS = registerItem("custom_leggings", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item CUSTOM_BOOTS = registerItem("custom_boots", new DynamicArmorItem(ModArmorMaterials.CUSTOM, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    public static final Item JESTER_LULLABY_MUSIC_DISC = registerItem("jester_lullaby_music_disc", new MusicDiscItem(7, ModSounds.JESTER_LULLABY, new FabricItemSettings().maxCount(1), 251));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CoffeeHouse.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CoffeeHouse.LOGGER.info("Registering ModItems for " + CoffeeHouse.MOD_ID);
    }
}
