package net.jdonthatrack.coffeehouse.item.custom;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ArmorItemCommonMethods extends GeoItem {
    AnimatableInstanceCache getAnimatableInstanceCache();

    default Supplier<Object> getRenderProvider() {
        return GeoItem.makeRenderer(this);
    }

    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    default PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    void createRenderer(Consumer<Object> consumer);
}