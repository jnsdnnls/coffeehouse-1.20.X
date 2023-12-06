package net.jdonthatrack.coffeehouse.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    
    AMETHYST("amethyst", 25, new int[] {3, 8, 6, 3}, 19, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2.0F, 0.1F, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)),

    TORCH("torch", 25, new int[] {3, 8, 6, 3}, 19, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 2.0F, 0.1F, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)),

    CUSTOM("custom", 25, new int[] {3, 8, 6, 3}, 19, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.1F, () -> Ingredient.ofItems(Items.NETHERITE_INGOT));

    private static final int[] BASE_DURABILITY = { 11, 16, 15, 13 }; // HELMET, CHESTPLATE, LEGGINGS, BOOTS
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = repairIngredientSupplier;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
