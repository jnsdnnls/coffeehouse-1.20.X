package net.jdonthatrack.coffeehouse;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.block.entity.ModBlockEntityTypes;
import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.entity.custom.FerretEntity;
import net.jdonthatrack.coffeehouse.entity.custom.RaptorEntity;
import net.jdonthatrack.coffeehouse.entity.custom.UnicycleEntity;
import net.jdonthatrack.coffeehouse.entity.custom.WindigoEntity;
import net.jdonthatrack.coffeehouse.item.ModItemGroups;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.jdonthatrack.coffeehouse.recipe.ModRecipeSerializers;
import net.jdonthatrack.coffeehouse.recipe.ModRecipeTypes;
import net.jdonthatrack.coffeehouse.screen.ModScreenHandlerTypes;
import net.jdonthatrack.coffeehouse.sound.ModSounds;
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
		FabricDefaultAttributeRegistry.register(ModEntities.EARTH_GOLEM, WindigoEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DIRE_WOLF, WindigoEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.UNICYCLE, UnicycleEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FERRET, FerretEntity.createFerretAttributes());

		ModWorldGeneration.generateModWorldGen();
		ModBlockEntityTypes.registerModBlockEntityTypes();

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModSounds.registerSounds();
		ModBlocks.registerModBlocks();

		ModScreenHandlerTypes.registerModScreenHandlerTypes();
		ModRecipeTypes.registerModRecipeTypes();
		ModRecipeSerializers.registerModRecipeSerializers();
	}
}