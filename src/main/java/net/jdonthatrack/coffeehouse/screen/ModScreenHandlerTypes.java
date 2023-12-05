package net.jdonthatrack.coffeehouse.screen;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerTypes {
    public static final ScreenHandlerType<DefiningTableScreenHandler> DEFINING_TABLE = register("defining_table", DefiningTableScreenHandler::new);
    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(CoffeeHouse.MOD_ID, id), new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory, FeatureFlag... requiredFeatures) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(CoffeeHouse.MOD_ID, id), new ScreenHandlerType<T>(factory, FeatureFlags.FEATURE_MANAGER.featureSetOf(requiredFeatures)));
    }

    public static void registerScreenHandlers() {
        CoffeeHouse.LOGGER.info("Registering Screen Handlers " + CoffeeHouse.MOD_ID);
    }
}
