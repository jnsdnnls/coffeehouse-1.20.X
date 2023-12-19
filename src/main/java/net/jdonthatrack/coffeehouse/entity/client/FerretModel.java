package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.FerretEntity;
import net.jdonthatrack.coffeehouse.entity.custom.WindigoEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FerretModel extends GeoModel<FerretEntity> {

    @Override
    public Identifier getModelResource(FerretEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "geo/ferret.geo.json");
    }

    @Override
    public Identifier getTextureResource(FerretEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/ferret.png");
    }

    @Override
    public Identifier getAnimationResource(FerretEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "animations/ferret.animation.json");
    }

    @Override
    public void setCustomAnimations(FerretEntity animatable, long instanceId, AnimationState<FerretEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
