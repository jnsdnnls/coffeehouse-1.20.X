package net.jdonthatrack.coffeehouse.recipe;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(CoffeeHouse.MOD_ID, DefiningRecipe.Serializer.ID),
                DefiningRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(CoffeeHouse.MOD_ID, DefiningRecipe.Type.ID),
                DefiningRecipe.Type.INSTANCE);
    }
}
