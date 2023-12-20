package net.jdonthatrack.coffeehouse.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent UNDEFINED_CANDY = new FoodComponent.Builder().hunger(8).saturationModifier(1.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 100, 1), 1.0f).build();
}
