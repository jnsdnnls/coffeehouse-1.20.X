package net.jdonthatrack.coffeehouse.item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

public interface DynamicModelItem {

    static NbtCompound setModel(NbtCompound nbt, String model) {
        nbt.putString("model", model);
        return nbt;
    }

    static void setModel(ItemStack stack, String model) {
        setModel(stack.getOrCreateNbt(), model);
    }

    @Nullable
    static String getModel(NbtCompound nbt) {
        return nbt.contains("model", NbtElement.STRING_TYPE) ? nbt.getString("model") : null;
    }

    static boolean hasModel(NbtCompound nbt) {
        return nbt.contains("model", NbtElement.STRING_TYPE);
    }
}
