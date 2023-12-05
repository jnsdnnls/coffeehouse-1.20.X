package net.jdonthatrack.coffeehouse.item.custom;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;


public class DynamicArmorModel extends GeoModel<DynamicArmorItem> {
    private final Identifier modelResource;
    private final Identifier textureResource;
    private final Identifier animationResource;

    public DynamicArmorModel(Identifier model) {
        super();
        this.modelResource = model.withPrefixedPath("geo/").withSuffixedPath(".geo.json");
        this.textureResource = model.withPrefixedPath("textures/armor/").withSuffixedPath(".png");
        this.animationResource = model.withPrefixedPath("animations/").withSuffixedPath(".animation.json");
    }
    public Identifier getModelResource(DynamicArmorItem animatable) {
        return modelResource;
    }

    public Identifier getTextureResource(DynamicArmorItem animatable) {
        return textureResource;
    }

    public Identifier getAnimationResource(DynamicArmorItem animatable) {
        return animationResource;
    }
}