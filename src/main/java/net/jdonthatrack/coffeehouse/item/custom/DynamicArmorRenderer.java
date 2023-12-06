package net.jdonthatrack.coffeehouse.item.custom;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class DynamicArmorRenderer extends GeoArmorRenderer<DynamicArmorItem> {
    public DynamicArmorRenderer(Identifier model) {
        super(new DynamicArmorModel(model));
    }
}
