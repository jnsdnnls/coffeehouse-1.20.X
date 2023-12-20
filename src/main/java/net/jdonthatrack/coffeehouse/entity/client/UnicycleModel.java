package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.UnicycleEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class UnicycleModel extends GeoModel<UnicycleEntity> {

    @Override
    public Identifier getModelResource(UnicycleEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/unicycle.geo.json");
    }

    @Override
    public Identifier getTextureResource(UnicycleEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/unicycle.png");
    }

    @Override
    public Identifier getAnimationResource(UnicycleEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/unicycle.animation.json");
    }

    @Override
    public void setCustomAnimations(UnicycleEntity animatable, long instanceId, AnimationState<UnicycleEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("full_head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
