package net.jdonthatrack.coffeehouse.sound;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.client.sound.Sound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent JESTER_LULLABY = registerSoundEvent("jester_lullaby");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(CoffeeHouse.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        CoffeeHouse.LOGGER.info("Registering Sounds for " + CoffeeHouse.MOD_ID);
    }
}
