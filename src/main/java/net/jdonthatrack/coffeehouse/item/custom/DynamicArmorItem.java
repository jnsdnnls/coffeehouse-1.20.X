package net.jdonthatrack.coffeehouse.item.custom;

import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DynamicArmorItem extends ArmorItem implements GeoItem, ArmorItemCommonMethods, DynamicModelItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // ARMOR ITEM
    public DynamicArmorItem(ArmorMaterial armorMaterial, Type type, Settings settings) {
        super(armorMaterial, type, settings);
    }

    public static void registerModelPredicates() {
        registerModelPredicateForArmorItem(ModItems.CUSTOM_HELMET);
        registerModelPredicateForArmorItem(ModItems.CUSTOM_CHESTPLATE);
        registerModelPredicateForArmorItem(ModItems.CUSTOM_LEGGINGS);
        registerModelPredicateForArmorItem(ModItems.CUSTOM_BOOTS);
    }

    private static void registerModelPredicateForArmorItem(Item armorItem) {
        ModelPredicateProviderRegistry.register(armorItem, new Identifier("model"), (stack, clientWorld, livingEntity, seed) -> {
            if (!DynamicModelItem.hasModel(stack)) { // Use a default value if the "model" tag is missing
                return 0.0f;
            }
            String modelValue = DynamicModelItem.getModel(stack);
            return switch (modelValue) {
                case "undefined" -> 0.0f;
                case "torch" -> 0.1f;
                case "amethyst" -> 0.2f;
                // Add cases for other armor types if needed
                default -> 0.0f;
            };
        });
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // RENDERER
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final Map<String, DynamicArmorRenderer> renderers = new HashMap<>();

            @Override
            @SuppressWarnings("unchecked")
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack stack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {

                String currentModel = DynamicModelItem.getModel(stack);
                if (!isValid(currentModel)) {
                    currentModel = "undefined";
                }

                Identifier modelIdentifier = new Identifier(CoffeeHouse.MOD_ID, currentModel);
                DynamicArmorRenderer renderer = renderers.computeIfAbsent(currentModel, model -> new DynamicArmorRenderer(modelIdentifier));

                renderer.prepForRender(livingEntity, stack, equipmentSlot, original);
                return renderer;
            }

            private boolean isValid(String model) {
                return ModItems.VALID_MODELS.contains(model);
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.fail(itemStack);
    }

    // APPEND TOOLTIP
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String currentModel;
        if (DynamicModelItem.hasModel(stack)) {
            currentModel = DynamicModelItem.getModel(stack);
        } else {
            currentModel = "undefined";
        }
        if (context.isAdvanced()) {
            tooltip.add(Text.literal("Model: " + currentModel).formatted(Formatting.GRAY));
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        String currentModel;

        if (!DynamicModelItem.hasModel(stack)) {
            currentModel = "Undefined";
        } else {
            currentModel = DynamicModelItem.getModel(stack);
        }

        String armorType = switch (this.type) {
            case HELMET -> "Helmet";
            case CHESTPLATE -> "Chestplate";
            case LEGGINGS -> "Leggings";
            case BOOTS -> "Boots";
        };

        return Text.translatable("item.coffeehouse.custom_item", capitalize(currentModel), armorType);
    }
}