package net.jdonthatrack.coffeehouse.entity.client;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.entity.custom.WindigoEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WindigoRenderer extends GeoEntityRenderer<WindigoEntity> {
    public WindigoRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WindigoModel());
    }

    @Override
    public Identifier getTextureLocation(WindigoEntity animatable) {
        return new Identifier(CoffeeHouse.MOD_ID, "textures/entity/windigo.png");
    }

    @Override
    public void render(WindigoEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
