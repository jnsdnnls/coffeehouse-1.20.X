package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.RaptorEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RaptorModel extends GeoModel<RaptorEntity> {

    @Override
    public Identifier getModelResource(RaptorEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/raptor.geo.json");
    }

    @Override
    public Identifier getTextureResource(RaptorEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/raptor.png");
    }

    @Override
    public Identifier getAnimationResource(RaptorEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/raptor.animation.json");
    }

    @Override
    public void setCustomAnimations(RaptorEntity animatable, long instanceId, AnimationState<RaptorEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("full_head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE - 19.2f);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

        CoreGeoBone saddle = this.getAnimationProcessor().getBone("saddle");
        CoreGeoBone rein = this.getAnimationProcessor().getBone("rein");
        CoreGeoBone headpiece = this.getAnimationProcessor().getBone("headpiece");
        if (saddle != null) {
            saddle.setHidden(!this.isSaddled(animatable));
            rein.setHidden(!this.isSaddled(animatable));
            headpiece.setHidden(!this.isSaddled(animatable));
            // add headpieces here with bones
        }
    }

    public boolean isSaddled(RaptorEntity raptor) {
        return raptor.getHorseFlag(4);
    }
}
