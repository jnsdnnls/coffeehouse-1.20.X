package net.jdonthatrack.coffeehouse.recipe;

import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.recipe.Ingredient;

public enum DefiningType {
    ARMOR("armor", Ingredient.ofItems(ModItems.CUSTOM_HELMET, ModItems.CUSTOM_CHESTPLATE, ModItems.CUSTOM_LEGGINGS, ModItems.CUSTOM_BOOTS)),
    SPAWN_EGG("spawn_egg", Ingredient.ofItems(ModItems.CUSTOM_SPAWN_EGG));

    public final String name;
    public final Ingredient ingredient;

    DefiningType(String name, Ingredient ingredient) {
        this.name = name;
        this.ingredient = ingredient;
    }
}
