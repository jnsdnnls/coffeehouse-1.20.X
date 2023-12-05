package net.jdonthatrack.coffeehouse.item.custom;

import net.fabricmc.fabric.api.util.NbtType;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.item.DynamicModelItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static net.jdonthatrack.coffeehouse.item.ModItems.*;

public class DynamicArmorItem extends ArmorItem implements GeoItem, DynamicModelItem, ArmorItemCommonMethods {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // ARMOR ITEM
    public DynamicArmorItem(ArmorMaterial armorMaterial, Type type, Settings settings) {
        super(armorMaterial, type, settings);
    }


    // RENDERER
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private Map<String, DynamicArmorRenderer> renderers = new HashMap<>();

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                        EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {

                String currentModel = itemStack.getNbt().getString("model");
                if (checkValid(currentModel)) {

                    Identifier modelIdentifier = new Identifier(CoffeeHouse.MOD_ID, currentModel);
                    DynamicArmorRenderer renderer = renderers.computeIfAbsent(currentModel, model -> new DynamicArmorRenderer(modelIdentifier));

                    renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                    return renderer;
                } else {
                    currentModel = "question_armor";
                    Identifier modelIdentifier = new Identifier(CoffeeHouse.MOD_ID, currentModel);
                    DynamicArmorRenderer renderer = renderers.computeIfAbsent(currentModel, model -> new DynamicArmorRenderer(modelIdentifier));

                    renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                    return renderer;
                }
            }

            private boolean checkValid(String model) {
                return !model.isEmpty() && VALID_MODELS.contains(model);
            }
        });
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static void registerModelPredicates() {
        registerModelPredicateForArmorType(CUSTOM_HELMET);
        registerModelPredicateForArmorType(CUSTOM_CHESTPLATE);
        registerModelPredicateForArmorType(CUSTOM_LEGGINGS);
        registerModelPredicateForArmorType(CUSTOM_BOOTS);
    }

    private static void registerModelPredicateForArmorType(Item armorType) {
        ModelPredicateProviderRegistry.register(
                armorType, new Identifier("model"), (itemStack, clientWorld, livingEntity, seed) -> {
                    NbtCompound nbtData = itemStack.getNbt();
                    if (nbtData == null) {
                        return 0.0f;
                    }
                    if (nbtData.contains("model", NbtType.STRING)) {
                        String currentModel = nbtData.getString("model");
                        switch (currentModel) {
                            case "torch_armor":
                                return 0.1F;
                            case "amethyst_armor":
                                return 0.2F;
                            // Add cases for other armor types if needed
                            default:
                                return 0.0F;
                        }
                    }
                    return 0.0F; // Use a default value if the "model" tag is missing
                }
        );
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.fail(itemStack);
    }

    // APPEND TOOLTIP
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()) {
            String currentModel = stack.getNbt().getString("model");
            if (context.isAdvanced()) {
                tooltip.add(Text.literal("Model: " + currentModel).formatted(Formatting.GRAY));
            }
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        String currentModel = stack.getNbt().getString("model");

        if (currentModel == "") {
            currentModel = "Undefined";
        }

        currentModel = currentModel.replace("_armor", "");

        String modelType;

        switch (this.type) {
            case HELMET:
                modelType = "Helmet";
                break;
            case CHESTPLATE:
                modelType = "Chestplate";
                break;
            case LEGGINGS:
                modelType = "Leggings";
                break;
            case BOOTS:
                modelType = "Boots";
                break;
            default:
                modelType = "Model";
        }
        return Text.translatable("item.coffeehouse.custom_armor", capitalize(currentModel), modelType);
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }
}