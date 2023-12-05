package net.jdonthatrack.coffeehouse.item;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface DynamicModelItem {

    default boolean hasModel(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("model", 99);
    }

    default String getModel(ItemStack stack) {
        NbtCompound nbtData = stack.getOrCreateNbt();
        if (nbtData.contains("model", NbtType.STRING)) {
            return nbtData.getString("model");
        }
        return "default_model";  // Default model in case it's not set
    }

    default void setModel(ItemStack stack, String model) {
        NbtCompound nbtData = new NbtCompound();
        nbtData.putString("model", model);

        stack.setNbt(nbtData);;
    }
}
