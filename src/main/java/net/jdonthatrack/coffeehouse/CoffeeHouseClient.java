package net.jdonthatrack.coffeehouse;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.entity.client.DireWolfRenderer;
import net.jdonthatrack.coffeehouse.entity.client.EarthGolemRenderer;
import net.jdonthatrack.coffeehouse.entity.client.RaptorRenderer;
import net.jdonthatrack.coffeehouse.entity.client.WindigoRenderer;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.jdonthatrack.coffeehouse.screen.DefiningTableScreen;
import net.jdonthatrack.coffeehouse.screen.ModScreenHandlerTypes;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import software.bernie.geckolib.GeckoLib;

public class CoffeeHouseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DynamicArmorItem.registerModelPredicates();

        GeckoLib.initialize();

        EntityRendererRegistry.register(ModEntities.RAPTOR, RaptorRenderer::new);
        EntityRendererRegistry.register(ModEntities.WINDIGO, WindigoRenderer::new);
        EntityRendererRegistry.register(ModEntities.EARTH_GOLEM, EarthGolemRenderer::new);
        EntityRendererRegistry.register(ModEntities.DIRE_WOLF, DireWolfRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEFINING_TABLE, RenderLayer.getCutout());
        HandledScreens.register(ModScreenHandlerTypes.DEFINING_TABLE, DefiningTableScreen::new);
    }
}
