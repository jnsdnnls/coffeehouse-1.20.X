package net.jdonthatrack.coffeehouse;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.item.ModItemGroup;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.jdonthatrack.coffeehouse.screen.DefiningTableScreen;
import net.jdonthatrack.coffeehouse.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import software.bernie.geckolib.GeckoLib;
import net.jdonthatrack.coffeehouse.entity.client.RaptorRenderer;
import net.jdonthatrack.coffeehouse.entity.client.WindigoRenderer;

public class CoffeeHouseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModItemGroup.registerItemGroups();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        DynamicArmorItem.registerModelPredicates();

        GeckoLib.initialize();

        EntityRendererRegistry.register(ModEntities.RAPTOR, RaptorRenderer::new);
        EntityRendererRegistry.register(ModEntities.WINDIGO, WindigoRenderer::new);

        HandledScreens.register(ModScreenHandlers.DEFINING_TABLE_SCREEN_HANDLER, DefiningTableScreen::new);
    }
}
