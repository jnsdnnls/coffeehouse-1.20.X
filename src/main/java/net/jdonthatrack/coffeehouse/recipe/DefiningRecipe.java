package net.jdonthatrack.coffeehouse.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class DefiningRecipe implements Recipe<Inventory> {
    protected final int price;
    protected final String model;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final String group;

    public DefiningRecipe(String group, int price, String model) {
        this(ModRecipeTypes.DEFINING, ModRecipeSerializers.DEFINING, group, price, model);
    }

    public DefiningRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, int price, String model) {
        this.type = type;
        this.serializer = serializer;
        this.group = group;
        this.price = price;
        this.model = model;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        ItemStack dynamicArmorItemStack = inventory.getStack(0);
        return dynamicArmorItemStack.getItem() instanceof DynamicArmorItem && inventory.getStack(1).getCount() >= price;
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

//    @Override
//    public ItemStack getResult(DynamicRegistryManager registryManager) {
//        return new ItemStack()
//        return this.modelName;
//    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return null;
    }

//    @Override
//    public DefaultedList<Ingredient> getIngredients() {
//        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
//        defaultedList.add(this.input);
//        return defaultedList;
//    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        NbtCompound nbt = inventory.getStack(0).getOrCreateNbt();
        nbt.putString("model", model);
        return inventory.getStack(0);
    }

    public static class Serializer<T extends DefiningRecipe>
            implements RecipeSerializer<T> {
        final RecipeFactory<T> recipeFactory;
        private final Codec<T> codec;

        protected Serializer(RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
            this.codec = RecordCodecBuilder.create(instance -> instance.group(
                    Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
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
            int price = packetByteBuf.readInt();
            String model = packetByteBuf.readString();
            return this.recipeFactory.create(group, price, model);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, T definingRecipe) {
            packetByteBuf.writeString(definingRecipe.group);
            packetByteBuf.writeInt(definingRecipe.price);
            packetByteBuf.writeString(definingRecipe.model);
        }

        public static interface RecipeFactory<T extends DefiningRecipe> {
            public T create(String group, int price, String model);
        }
    }
}
