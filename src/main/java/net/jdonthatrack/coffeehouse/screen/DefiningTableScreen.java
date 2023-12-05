package net.jdonthatrack.coffeehouse.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.jdonthatrack.coffeehouse.recipe.DefiningRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class DefiningTableScreen extends HandledScreen<DefiningTableScreenHandler> {
    private static final Identifier SCROLLER_TEXTURE = new Identifier("container/stonecutter/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = new Identifier("container/stonecutter/scroller_disabled");
    private static final Identifier RECIPE_SELECTED_TEXTURE = new Identifier("container/stonecutter/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = new Identifier("container/stonecutter/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = new Identifier("container/stonecutter/recipe");
    private static final Identifier TEXTURE = new Identifier(CoffeeHouse.MOD_ID,"textures/gui/defining_table_gui.png");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int RECIPE_LIST_COLUMNS = 4;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 18;
    private static final int RECIPE_ENTRY_HEIGHT = 16;
    private static final int SCROLLBAR_AREA_HEIGHT = 54;
    private static final int RECIPE_LIST_OFFSET_X = 63;
    private static final int RECIPE_LIST_OFFSET_Y = 16;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    public DefiningTableScreen(DefiningTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        --this.titleY;
    }

    @Override
    protected void init() {
        super.init();
        playerInventoryTitleY = 1000;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight + 10);
        int k = (int)(33.0F * this.scrollAmount);
        Identifier identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(identifier, i + 156, j + 17 + k, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        int l = this.x + RECIPE_LIST_OFFSET_X;
        int m = this.y + RECIPE_LIST_OFFSET_Y;
        int n = this.scrollOffset + 12;
        this.renderRecipeBackground(context, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(context, l, m, n);
    }

    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        if (this.canCraft) {
            int i = this.x + RECIPE_LIST_OFFSET_X;
            int j = this.y + RECIPE_LIST_OFFSET_Y;
            int k = this.scrollOffset + 12;
            List<RecipeEntry<StonecuttingRecipe>> list = ((DefiningTableScreenHandler)this.handler).getAvailableRecipes();

            for(int l = this.scrollOffset; l < k && l < ((DefiningTableScreenHandler)this.handler).getAvailableRecipeCount(); ++l) {
                int m = l - this.scrollOffset;
                int n = i + m % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH + 1;
                int o = j + m / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;
                if (x >= n && x < n + RECIPE_ENTRY_WIDTH && y >= o && y < o + RECIPE_ENTRY_HEIGHT) {
                    context.drawItemTooltip(this.textRenderer, ((DefiningRecipe)((RecipeEntry)list.get(l)).value()).getResult(this.client.world.getRegistryManager()), x, y);
                }
            }
        }

    }

    private void renderRecipeBackground(DrawContext context, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for(int i = this.scrollOffset; i < scrollOffset && i < ((DefiningTableScreenHandler)this.handler).getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = j / RECIPE_LIST_COLUMNS;
            int m = y + l * RECIPE_ENTRY_HEIGHT + 2;
            Identifier identifier;
            if (i == ((DefiningTableScreenHandler)this.handler).getSelectedRecipe()) {
                identifier = RECIPE_SELECTED_TEXTURE;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + RECIPE_ENTRY_WIDTH && mouseY < m + RECIPE_ENTRY_HEIGHT) {
                identifier = RECIPE_HIGHLIGHTED_TEXTURE;
            } else {
                identifier = RECIPE_TEXTURE;
            }

            context.drawGuiTexture(identifier, k, m - 1, RECIPE_ENTRY_WIDTH, RECIPE_ENTRY_HEIGHT);
        }

    }

    private void renderRecipeIcons(DrawContext context, int x, int y, int scrollOffset) {
        List<RecipeEntry<StonecuttingRecipe>> list = ((DefiningTableScreenHandler)this.handler).getAvailableRecipes();

        for(int i = this.scrollOffset; i < scrollOffset && i < ((DefiningTableScreenHandler)this.handler).getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = j / RECIPE_LIST_COLUMNS;
            int m = y + l * RECIPE_ENTRY_HEIGHT + 2;
            context.drawItem(((StonecuttingRecipe)((RecipeEntry)list.get(i)).value()).getResult(this.client.world.getRegistryManager()), k, m);
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = this.x + RECIPE_LIST_OFFSET_X;
            int j = this.y + RECIPE_LIST_OFFSET_Y;
            int k = this.scrollOffset + 12;

            for(int l = this.scrollOffset; l < k; ++l) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double)(i + m % 5 * 16);
                double e = mouseY - (double)(j + m / 5 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0 && ((DefiningTableScreenHandler)this.handler).onButtonClick(this.client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.client.interactionManager.clickButton(((DefiningTableScreenHandler)this.handler).syncId, l);
                    return true;
                }
            }

            i = this.x + 158;
            j = this.y + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + RECIPE_ENTRY_WIDTH;
            int j = i + SCROLLBAR_AREA_HEIGHT;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && ((DefiningTableScreenHandler)this.handler).getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (((DefiningTableScreenHandler)this.handler).getAvailableRecipeCount() + 4 - 1) / 4 - 3;
    }

    private void onInventoryChange() {
        this.canCraft = ((DefiningTableScreenHandler)this.handler).canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0F;
            this.scrollOffset = 0;
        }

    }
}
