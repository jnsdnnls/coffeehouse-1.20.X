package net.jdonthatrack.coffeehouse.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;

public class PrevDefiningRecipe implements Recipe<Inventory> { // NEVER USED
    private final ItemStack output;
    private final List<Ingredient> recipeItems;

    public PrevDefiningRecipe(List<Ingredient> ingredients, ItemStack itemStack) {
        this.output = itemStack;
        this.recipeItems = ingredients;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        ItemStack armorStack = ItemStack.EMPTY;

        // Find the armor item in the crafting grid
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof DynamicArmorItem) {
                armorStack = stack;
                break;
            }
        }

        // Check if the armor item was found and has the "model" tag
        // Add additional conditions if needed
        return !armorStack.isEmpty() && armorStack.hasNbt() && armorStack.getNbt().contains("model");
    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack armorStack = ItemStack.EMPTY;

        // Find the armor item in the crafting grid
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof DynamicArmorItem) {
                armorStack = stack;
                break;
            }
        }

        // Check if the armor item was found and has the "model" tag
        if (!armorStack.isEmpty() && armorStack.hasNbt() && armorStack.getNbt().contains("model")) {
            // Modify NBT data as needed
            NbtCompound nbt = armorStack.getOrCreateNbt();
            nbt.putString("model", "torch_armor"); // Replace with the desired new model name
            // Add additional modifications if needed
            return armorStack;
        }

        return ItemStack.EMPTY;
    }


    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<PrevDefiningRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "defining";
    }

    public static class Serializer implements RecipeSerializer<PrevDefiningRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "defining";

        public static final Codec<PrevDefiningRecipe> CODEC = RecordCodecBuilder.create(in -> in.group(
                validateAmount(Ingredient.DISALLOW_EMPTY_CODEC, 9).fieldOf("ingredients").forGetter(PrevDefiningRecipe::getIngredients),
                RecipeCodecs.CRAFTING_RESULT.fieldOf("output").forGetter(r -> r.output)
        ).apply(in, PrevDefiningRecipe::new));

        private static Codec<List<Ingredient>> validateAmount(Codec<Ingredient> delegate, int max) {
            return Codecs.validate(Codecs.validate(
                    delegate.listOf(), list -> list.size() > max ? DataResult.error(() -> "Recipe has too many ingredients!") : DataResult.success(list)
            ), list -> list.isEmpty() ? DataResult.error(() -> "Recipe has no ingredients!") : DataResult.success(list));
        }

        @Override
        public Codec<PrevDefiningRecipe> codec() {
            return CODEC;
        }

        @Override
        public PrevDefiningRecipe read(PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

            ItemStack output = buf.readItemStack();
            return new PrevDefiningRecipe(inputs, output);
        }

        @Override
        public void write(PacketByteBuf buf, PrevDefiningRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            buf.writeItemStack(recipe.getResult(null));
        }
    }
}