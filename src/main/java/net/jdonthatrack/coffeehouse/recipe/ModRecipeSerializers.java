package net.jdonthatrack.coffeehouse.recipe;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {
    public static final RecipeSerializer<DefiningRecipe> DEFINING = register("defining", new DefiningRecipe.Serializer<>(DefiningRecipe::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(CoffeeHouse.MOD_ID, id), serializer);
    }
    public static void registerModRecipeSerializers() {
        CoffeeHouse.LOGGER.info("Registering ModRecipeSerializers for " + CoffeeHouse.MOD_ID);
    }
}