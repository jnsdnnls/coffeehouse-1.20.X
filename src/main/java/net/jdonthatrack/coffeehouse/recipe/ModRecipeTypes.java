package net.jdonthatrack.coffeehouse.recipe;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {

    public static final RecipeType<DefiningRecipe> DEFINING = register("defining");

    private static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return Registry.register(Registries.RECIPE_TYPE, new Identifier(CoffeeHouse.MOD_ID, id), new RecipeType<T>(){

            public String toString() {
                return id;
            }
        });
    }

    public static void registerModRecipeTypes() {
        CoffeeHouse.LOGGER.info("Registering ModRecipeTypes for " + CoffeeHouse.MOD_ID);
    }
}
