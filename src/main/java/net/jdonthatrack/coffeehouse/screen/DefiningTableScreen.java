package net.jdonthatrack.coffeehouse.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.item.custom.DynamicArmorItem;
import net.jdonthatrack.coffeehouse.recipe.DefiningRecipe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

import static net.minecraft.client.gui.screen.ingame.InventoryScreen.drawEntity;

@Environment(EnvType.CLIENT)
public class DefiningTableScreen
        extends HandledScreen<DefiningTableScreenHandler> {
    private static final Identifier SCROLLER_TEXTURE = new Identifier("container/stonecutter/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = new Identifier("container/stonecutter/scroller_disabled");
    private static final Identifier RECIPE_SELECTED_TEXTURE = new Identifier("container/stonecutter/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = new Identifier("container/stonecutter/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = new Identifier("container/stonecutter/recipe");
    private static final Identifier TEXTURE = new Identifier(CoffeeHouse.MOD_ID, "textures/gui/container/defining_table_gui.png");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int RECIPE_LIST_COLUMNS = 4;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 16;
    private static final int RECIPE_ENTRY_HEIGHT = 18;
    private static final int SCROLLBAR_AREA_HEIGHT = 54;
    private static final int RECIPE_LIST_OFFSET_X = 63;
    private static final int RECIPE_LIST_OFFSET_Y = 6;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;

    @Nullable
    private ArmorStandEntity armorStand;

    public DefiningTableScreen(DefiningTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        --this.titleY;
    }

    @Override
    protected void init() {
        super.init();
        titleY = 1000;
        this.armorStand = new ArmorStandEntity(this.client.world, 0.0, 0.0, 0.0);
        this.armorStand.setHideBasePlate(true);
        this.armorStand.setShowArms(true);
        this.equipArmorStand(this.handler.outputSlot.getStack());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        if (slotId == 1) {
            this.equipArmorStand(stack);
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = (int)(41.0f * this.scrollAmount);
        Identifier scrollBarTexture = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
//        context.drawGuiTexture(scrollBarTexture, x + 119, y + 15 + k, 12, 15);
        context.drawGuiTexture(scrollBarTexture, x + 156, y + 7 + k, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        int l = this.x + RECIPE_LIST_OFFSET_X;
        int m = this.y + RECIPE_LIST_OFFSET_Y;
        int n = this.scrollOffset + SCROLLBAR_WIDTH;
        this.renderRecipeBackground(context, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(context, l, m, n);
        drawEntity(context, x, y, x + 65, y + 75, 25, 0.0625F, mouseX, mouseY, this.armorStand);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        if (this.canCraft) {
            int i = this.x + RECIPE_LIST_OFFSET_X;
            int j = this.y + RECIPE_LIST_OFFSET_Y;
            int k = this.scrollOffset + SCROLLBAR_WIDTH;
            List<RecipeEntry<DefiningRecipe>> availableRecipes = this.handler.getAvailableRecipes();
            for (int l = this.scrollOffset; l < k && l < this.handler.getAvailableRecipeCount(); ++l) {
                int m = l - this.scrollOffset;
                int n = i + m % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
                int o = j + m / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;
                if (x < n || x >= n + RECIPE_ENTRY_WIDTH || y < o || y >= o + RECIPE_ENTRY_HEIGHT) continue;
                context.drawItemTooltip(this.textRenderer, availableRecipes.get(l).value().getOutput(this.handler.inputSlot.getStack()), x, y);
            }
        }
    }

    private void renderRecipeBackground(DrawContext context, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = j / RECIPE_LIST_COLUMNS;
            int m = y + l * RECIPE_ENTRY_HEIGHT + 2;
            Identifier identifier = i == this.handler.getSelectedRecipe() ?
                    RECIPE_SELECTED_TEXTURE :
                    (mouseX >= k && mouseY >= m && mouseX < k + RECIPE_ENTRY_WIDTH && mouseY < m + RECIPE_ENTRY_HEIGHT ?
                            RECIPE_HIGHLIGHTED_TEXTURE :
                            RECIPE_TEXTURE);
            context.drawGuiTexture(identifier, k, m - 1, RECIPE_ENTRY_WIDTH, RECIPE_ENTRY_HEIGHT);
        }
    }

    private void renderRecipeIcons(DrawContext context, int x, int y, int scrollOffset) {
        List<RecipeEntry<DefiningRecipe>> availableRecipes = this.handler.getAvailableRecipes();
        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = j / RECIPE_LIST_COLUMNS;
            int m = y + l * RECIPE_ENTRY_HEIGHT + 2;
            context.drawItem(availableRecipes.get(i).value().getOutput(this.handler.inputSlot.getStack()), k, m);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = this.x + RECIPE_LIST_OFFSET_X;
            int j = this.y + RECIPE_LIST_OFFSET_Y;
            int k = this.scrollOffset + SCROLLBAR_WIDTH;
            for (int l = this.scrollOffset; l < k; ++l) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double)(i + m % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH);
                double e = mouseY - (double)(j + m / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT);
                if (!(d >= 0.0) || !(e >= 0.0) || !(d < 16.0) || !(e < 18.0) || !this.handler.onButtonClick(this.client.player, l)) continue;
                this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                this.client.interactionManager.clickButton(this.handler.syncId, l);
                this.equipArmorStand(this.handler.outputSlot.getStack());
                return true;
            }
            i = this.x + 119;
            j = this.y + 9;
            if (mouseX >= i && mouseX < i + SCROLLBAR_WIDTH && mouseY >= j && mouseY < j + SCROLLBAR_AREA_HEIGHT) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + RECIPE_LIST_OFFSET_Y;
            int j = i + SCROLLBAR_AREA_HEIGHT;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5f) / ((float)(j - i) - 15.0f);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int)(this.scrollAmount * this.getMaxScroll() + 0.5) * RECIPE_LIST_COLUMNS;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollOffset = (int) ((this.scrollAmount * i + 0.5) * RECIPE_LIST_COLUMNS);
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && this.handler.getAvailableRecipeCount() > SCROLLBAR_WIDTH;
    }

    protected int getMaxScroll() {
        return (this.handler.getAvailableRecipeCount() + RECIPE_LIST_COLUMNS - 1) / RECIPE_LIST_COLUMNS - RECIPE_LIST_ROWS;
    }

    private void onInventoryChange() {
        this.canCraft = this.handler.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0f;
            this.scrollOffset = 0;
        }
    }

    private void equipArmorStand(ItemStack stack) {
        if (this.armorStand != null) {
            EquipmentSlot[] var2 = EquipmentSlot.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                EquipmentSlot equipmentSlot = var2[var4];
                this.armorStand.equipStack(equipmentSlot, ItemStack.EMPTY);
            }

            if (!stack.isEmpty()) {
                ItemStack itemStack = stack.copy();
                Item var8 = stack.getItem();
                if (var8 instanceof DynamicArmorItem armorItem) {
                    this.armorStand.equipStack(armorItem.getSlotType(), itemStack);
                } else {
                    this.armorStand.equipStack(EquipmentSlot.OFFHAND, itemStack);
                }
            }

        }
    }
}