package net.jdonthatrack.coffeehouse.item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

public interface DynamicModelItem {
    public static final String MODEL_KEY = "model";

    public static boolean hasModel(ItemStack stack) {
        return hasModel(stack.getNbt());
    }

    public static boolean hasModel(@Nullable NbtCompound nbt) {
        return nbt != null && nbt.contains(MODEL_KEY, NbtElement.STRING_TYPE);
    }

    public static String getModel(NbtCompound nbt) {
        return nbt.getString(MODEL_KEY);
    }

    public static String getModel(ItemStack stack) {
        return getModel(stack.getNbt());
    }

    public static void setModel(ItemStack stack, String model) {
        stack.getOrCreateNbt().putString(MODEL_KEY, model);
    }
}
