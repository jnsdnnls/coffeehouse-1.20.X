package net.jdonthatrack.coffeehouse.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface DynamicModelItem {

    default boolean hasModel(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("model", 99);
    }

    default String getModel(ItemStack stack) {
        NbtCompound nbtData = stack.getOrCreateNbt();
        if (nbtData.contains("model", NbtElement.STRING_TYPE)) {
            return nbtData.getString("model");
        }
        return "default_model";  // Default model in case it's not set
    }

    default void setModel(ItemStack stack, String model) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString("model", model);
    }
}
