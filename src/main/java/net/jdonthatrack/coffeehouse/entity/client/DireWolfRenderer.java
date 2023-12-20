package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.DireWolfEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.example.client.model.entity.RaceCarModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DireWolfRenderer extends GeoEntityRenderer<DireWolfEntity> {
    public DireWolfRenderer(EntityRendererFactory.Context context) {
        super(context, new DireWolfModel());
    }

    @Override
    public Identifier getTextureLocation(DireWolfEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/wolf.png");
    }

    @Override
    public void render(DireWolfEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
