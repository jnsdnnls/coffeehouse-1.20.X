package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.EarthGolemEntity;
import net.jdonthatrack.coffeehouse.entity.custom.RaptorEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EarthGolemRenderer extends GeoEntityRenderer<EarthGolemEntity> {
    public EarthGolemRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new EarthGolemModel());
    }

    @Override
    public Identifier getTextureLocation(EarthGolemEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/earth_golem.png");
    }

    @Override
    public void render(EarthGolemEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
