package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.EarthGolemEntity;
import net.jdonthatrack.coffeehouse.entity.custom.RaptorEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EarthGolemModel extends GeoModel<EarthGolemEntity> {

    @Override
    public Identifier getModelResource(EarthGolemEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/earth_golem.geo.json");
    }

    @Override
    public Identifier getTextureResource(EarthGolemEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/earth_golem.png");
    }

    @Override
    public Identifier getAnimationResource(EarthGolemEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/earth_golem.animation.json");
    }

    @Override
    public void setCustomAnimations(EarthGolemEntity animatable, long instanceId, AnimationState<EarthGolemEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }

    public boolean isSaddled(EarthGolemEntity earthGolem) {
        return (earthGolem.getHorseFlag(4));
    }
}
