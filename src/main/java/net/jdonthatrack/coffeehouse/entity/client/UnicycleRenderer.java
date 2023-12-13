package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.UnicycleEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UnicycleRenderer extends GeoEntityRenderer<UnicycleEntity> {
    public UnicycleRenderer(EntityRendererFactory.Context renderManager) {super(renderManager, new UnicycleModel());}

    @Override
    public Identifier getTextureLocation(UnicycleEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/unicycle.png");
    }

    @Override
    public void render(UnicycleEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
