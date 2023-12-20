package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.DireWolfEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DireWolfModel extends DefaultedEntityGeoModel<DireWolfEntity> {
    public DireWolfModel() {
        super(new Identifier(CoffeeHouse.MOD_ID, "dire_wolf"));
    }

    @Override
    public Identifier getModelResource(DireWolfEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/wolf.geo.json");
    }

    @Override
    public Identifier getTextureResource(DireWolfEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/wolf.png");
    }

    @Override
    public Identifier getAnimationResource(DireWolfEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/wolf.animation.json");
    }

    @Override
    public void setCustomAnimations(DireWolfEntity animatable, long instanceId, AnimationState<DireWolfEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
