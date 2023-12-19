package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.WindigoEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WindigoModel extends GeoModel<WindigoEntity> {

    @Override
    public Identifier getModelResource(WindigoEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/windigo.geo.json");
    }

    @Override
    public Identifier getTextureResource(WindigoEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/windigo.png");
    }

    @Override
    public Identifier getAnimationResource(WindigoEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/windigo.animation.json");
    }

    @Override
    public void setCustomAnimations(WindigoEntity animatable, long instanceId, AnimationState<WindigoEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("full_head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
