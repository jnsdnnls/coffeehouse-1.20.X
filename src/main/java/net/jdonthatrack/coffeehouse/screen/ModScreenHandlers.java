package net.jdonthatrack.coffeehouse.screen;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers<T extends ScreenHandler> {
    public static final ScreenHandlerType<DefiningTableScreenHandler> DEFINING_TABLE_SCREEN_HANDLER = register("defining_table", DefiningTableScreenHandler::new);
    private final FeatureSet requiredFeatures;
    private final ScreenHandlerType.Factory<T> factory;

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(CoffeeHouse.MOD_ID, id), new ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES));
    }

    private static <T extends ScreenHandler> ScreenHandlerType register(String id, ScreenHandlerType.Factory<T> factory, FeatureFlag... requiredFeatures) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(CoffeeHouse.MOD_ID, id), new ScreenHandlerType(factory, FeatureFlags.FEATURE_MANAGER.featureSetOf(requiredFeatures)));
    }

    public ModScreenHandlers(ScreenHandlerType.Factory<T> factory, FeatureSet requiredFeatures) {
        this.factory = factory;
        this.requiredFeatures = requiredFeatures;
    }

    public T create(int syncId, PlayerInventory playerInventory) {
        return this.factory.create(syncId, playerInventory);
    }

    public FeatureSet getRequiredFeatures() {
        return this.requiredFeatures;
    }

    public interface Factory<T extends ScreenHandler> {
        T create(int syncId, PlayerInventory playerInventory);
    }

    public static void registerScreenHandlers() {
        CoffeeHouse.LOGGER.info("Registering Screen Handlers " + CoffeeHouse.MOD_ID);
    }
}
