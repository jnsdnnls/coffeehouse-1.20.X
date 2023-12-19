package net.jdonthatrack.coffeehouse.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class DefiningRecipe implements Recipe<Inventory> {
    public static final Item[] CUSTOM_ARMOR = { ModItems.CUSTOM_HELMET, ModItems.CUSTOM_CHESTPLATE, ModItems.CUSTOM_LEGGINGS, ModItems.CUSTOM_BOOTS };
    protected static final double FRAME_TIME = 0.5;
    protected final int price;
    protected final Ingredient ingredient;
    protected final String model;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final String group;

    public DefiningRecipe(String group, Ingredient ingredient, int price, String model) {
        this(ModRecipeTypes.DEFINING, ModRecipeSerializers.DEFINING, group, ingredient, price, model);
    }

    public DefiningRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, Ingredient ingredient, int price, String model) {
        this.type = type;
        this.serializer = serializer;
        this.group = group;
        this.ingredient = ingredient;
        this.price = price;
        this.model = model;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.ingredient.test(inventory.getStack(0));
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.DEFINING_TABLE);
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        int index = (int) ((((double) System.nanoTime() / 1e9) % (FRAME_TIME * 4)) / FRAME_TIME); // index 0,1,2,3 changes every FRAME_TIME seconds
        ItemStack input = new ItemStack(CUSTOM_ARMOR[index]);
        NbtCompound nbt = input.getOrCreateNbt();
        nbt.putString("model", model);
        return input;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getOutput(ItemStack inputStack) {
        inputStack = inputStack.copy();
        NbtCompound nbt = inputStack.getOrCreateNbt();
        nbt.putString("model", model);
        return inputStack;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return getOutput(inventory.getStack(0));
    }

    public static class Serializer<T extends DefiningRecipe>
            implements RecipeSerializer<T> {
        final RecipeFactory<T> recipeFactory;
        private final Codec<T> codec;

        protected Serializer(RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
            this.codec = RecordCodecBuilder.create(instance -> instance.group(
                    Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
                    Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    Codec.INT.fieldOf("price").forGetter(recipe -> recipe.price),
                    Codec.STRING.fieldOf("model").forGetter(recipe -> recipe.model)
            ).apply(instance, recipeFactory::create));
        }

        @Override
        public Codec<T> codec() {
            return this.codec;
        }

        @Override
        public T read(PacketByteBuf packetByteBuf) {
            String group = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            int price = packetByteBuf.readInt();
            String model = packetByteBuf.readString();
            return this.recipeFactory.create(group, ingredient, price, model);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, T definingRecipe) {
            packetByteBuf.writeString(definingRecipe.group);
            definingRecipe.ingredient.write(packetByteBuf);
            packetByteBuf.writeInt(definingRecipe.price);
            packetByteBuf.writeString(definingRecipe.model);
        }

        public interface RecipeFactory<T extends DefiningRecipe> {
            T create(String group, Ingredient ingredient, int price, String model);
        }
    }
}
