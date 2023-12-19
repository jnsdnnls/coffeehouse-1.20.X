package net.jdonthatrack.coffeehouse.screen;

import net.jdonthatrack.coffeehouse.block.ModBlocks;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.jdonthatrack.coffeehouse.item.custom.DynamicSpawnEggItem;
import net.jdonthatrack.coffeehouse.recipe.DefiningRecipe;
import net.jdonthatrack.coffeehouse.recipe.ModRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefiningTableScreenHandler extends ScreenHandler {
    public static final int INPUT_ID = 0;
    public static final int OUTPUT_ID = 1;
    public static final int CURRENCY_ID = 2;
    private static final int INVENTORY_START = 2;
    private static final int INVENTORY_END = 29;
    private static final int HOTBAR_START = 29;
    private static final int HOTBAR_END = 38;
    final Slot inputSlot;
    final Slot currencySlot;
    final Slot outputSlot;
    final CraftingResultInventory output = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final Property selectedRecipe = Property.create();
    private final World world;
    long lastTakeTime;
    Runnable contentsChangedListener = () -> {
    };
    private List<RecipeEntry<DefiningRecipe>> availableRecipes = new ArrayList<>();
    private ItemStack inputStack = ItemStack.EMPTY;
    private ItemStack currencyStack = ItemStack.EMPTY;
    public final Inventory input = new SimpleInventory(1) {

        @Override
        public void markDirty() {
            super.markDirty();
            DefiningTableScreenHandler.this.onContentChanged(this);
            DefiningTableScreenHandler.this.contentsChangedListener.run();
        }
    };
    public final Inventory currency = new SimpleInventory(1) {

        @Override
        public void markDirty() {
            super.markDirty();
            DefiningTableScreenHandler.this.onContentChanged(this);
            DefiningTableScreenHandler.this.contentsChangedListener.run();
        }
    };

    public DefiningTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public DefiningTableScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ModScreenHandlerTypes.DEFINING_TABLE, syncId);
        int i;
        this.context = context;
        this.world = playerInventory.player.getWorld();
        this.currencySlot = this.addSlot(new Slot(this.currency, 0, 98, 61) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.UNDEFINIUM);
            }
        });
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 69, 61) {

            @Override
            public boolean canInsert(ItemStack stack) {
                if (stack.getItem() instanceof DynamicArmorItem) {
                    return stack.getItem() instanceof DynamicArmorItem;
                } else if (stack.getItem() instanceof DynamicSpawnEggItem) {
                    return stack.getItem() instanceof DynamicSpawnEggItem;
                }
                return false;
            }

            @Override
            public int getMaxItemCount(ItemStack stack) {
                return 1;
            }
        });
        this.outputSlot = this.addSlot(new Slot(this.output, 0, 152, 61) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.getWorld(), player, stack.getCount());
                ItemStack currencyStackAfterRemove = DefiningTableScreenHandler.this.currencySlot.takeStack(((DefiningRecipe) DefiningTableScreenHandler.this.output.getLastRecipe().value()).getPrice());
                ItemStack inputStackAfterRemove = DefiningTableScreenHandler.this.inputSlot.takeStack(1);
                DefiningTableScreenHandler.this.output.unlockLastRecipe(player, this.getInputStacks());
                if (!inputStackAfterRemove.isEmpty() && !currencyStackAfterRemove.isEmpty()) {
                    DefiningTableScreenHandler.this.populateResult();
                }
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (DefiningTableScreenHandler.this.lastTakeTime != l) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        DefiningTableScreenHandler.this.lastTakeTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(DefiningTableScreenHandler.this.inputSlot.getStack(), DefiningTableScreenHandler.this.currencySlot.getStack());
            }
        });
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperty(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<RecipeEntry<DefiningRecipe>> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && this.currencySlot.hasStack() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.DEFINING_TABLE);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack inputSlotStack = this.inputSlot.getStack();
        ItemStack currencySlotStack = this.currencySlot.getStack();

        if (selectedRecipe.get() == -1) {
            this.availableRecipes.clear();
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            if (!inputSlotStack.isEmpty() && !currencySlotStack.isEmpty()) {
                this.availableRecipes = getSortedRecipesById(new SimpleInventory(inputSlotStack, this.outputSlot.getStack(), currencySlotStack), this.world);
            }
        } else {
            if (inputSlotStack.isEmpty() || currencySlotStack.isEmpty()) {
                this.selectedRecipe.set(-1);
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
                this.availableRecipes.clear();
            } else if (currencySlotStack.getCount() != this.currencyStack.getCount()) {
                RecipeEntry<DefiningRecipe> prevRecipe = this.availableRecipes.get(this.selectedRecipe.get());
                this.availableRecipes = getSortedRecipesById(new SimpleInventory(inputSlotStack, this.outputSlot.getStack(), currencySlotStack), this.world);
                int indexOfPrevRecipe = this.availableRecipes.indexOf(prevRecipe);
                if (indexOfPrevRecipe == -1) { // PREVIOUS RECIPE NOT FOUND IN CURRENT availableRecipes
                    this.selectedRecipe.set(-1);
                    this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
                } else {
                    this.selectedRecipe.set(indexOfPrevRecipe);
                }
            } else if (!inputSlotStack.isOf(this.inputStack.getItem())) {
                this.outputSlot.setStackNoCallbacks(this.availableRecipes.get(selectedRecipe.get()).value().getOutput(inputSlotStack));
            }
        }

        if (!inputSlotStack.isOf(inputStack.getItem()) || currencySlotStack.getCount() != currencyStack.getCount()) {
            this.inputStack = inputSlotStack.copy();
            this.currencyStack = currencySlotStack.copy();
        }
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            RecipeEntry<DefiningRecipe> recipeEntry = this.availableRecipes.get(this.selectedRecipe.get());
            ItemStack itemStack = recipeEntry.value().craft(this.input, this.world.getRegistryManager());
            if (itemStack.isItemEnabled(this.world.getEnabledFeatures())) {
                this.output.setLastRecipe(recipeEntry);
                this.outputSlot.setStackNoCallbacks(itemStack);
            } else {
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            }
        } else {
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlerTypes.DEFINING_TABLE;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {
            ItemStack stackToMove = slot.getStack();
            Item item = stackToMove.getItem();
            itemStack = stackToMove.copy();
            if (slotIndex == OUTPUT_ID) { // TAKE FROM OUTPUT
                item.onCraft(stackToMove, player.getWorld(), player);
                if (!this.insertItem(stackToMove, INVENTORY_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(stackToMove, itemStack);
            } else if (slotIndex == INPUT_ID || slotIndex == CURRENCY_ID) { // TAKE FROM INPUT OR CURRENCY SLOT
                if (!this.insertItem(stackToMove, INVENTORY_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (item instanceof DynamicArmorItem) {
                if (!this.insertItem(stackToMove, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stackToMove.isOf(ModItems.UNDEFINIUM)) {
                if (!this.insertItem(stackToMove, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (INVENTORY_START <= slotIndex && slotIndex < INVENTORY_END) { // TAKE FROM INVENTORY
                if (!this.insertItem(stackToMove, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (HOTBAR_START <= slotIndex && slotIndex < HOTBAR_END) { // TAKE FROM HOTBAR
                if (!this.insertItem(stackToMove, INVENTORY_START, INVENTORY_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stackToMove.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            slot.markDirty();
            if (stackToMove.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, stackToMove);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    public List<RecipeEntry<DefiningRecipe>> getAllValidRecipes(Inventory inventory, World world) {

        return world.getRecipeManager().getAllMatches(ModRecipeTypes.DEFINING, inventory, world);
    }

    public List<RecipeEntry<DefiningRecipe>> getSortedRecipesById(Inventory inventory, World world) {
        List<RecipeEntry<DefiningRecipe>> validRecipes = getAllValidRecipes(inventory, world);

        // Sort the valid recipes based on the recipe ID
        validRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.id().toString()));

        return validRecipes;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
        this.context.run((world, pos) -> this.dropInventory(player, this.currency));
    }
}