package net.jdonthatrack.coffeehouse;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.jdonthatrack.coffeehouse.block.entity.ModBlockEntities;
import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.entity.custom.RaptorEntity;
import net.jdonthatrack.coffeehouse.entity.custom.WindigoEntity;
import net.jdonthatrack.coffeehouse.recipe.ModRecipes;
import net.jdonthatrack.coffeehouse.screen.ModScreenHandlers;
import net.jdonthatrack.coffeehouse.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoffeeHouse implements ModInitializer {
	public static final String MOD_ID = "coffeehouse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(ModEntities.RAPTOR, RaptorEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.WINDIGO, WindigoEntity.setAttributes());

		ModWorldGeneration.generatModWorldGen();
		ModBlockEntities.registerBlockEntities();

		ModRecipes.registerRecipes();
		ModScreenHandlers.registerScreenHandlers();
	}
}