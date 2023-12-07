package net.jdonthatrack.coffeehouse.block.entity;

import net.jdonthatrack.coffeehouse.block.custom.DefiningTableBlock;
import net.jdonthatrack.coffeehouse.screen.DefiningTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DefiningTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public DefiningTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DEFINING_TABLE, pos, state);
    }

    public DefiningTableBlockEntity(BlockPos pos, BlockState state, World world) {
        super(ModBlockEntityTypes.DEFINING_TABLE, pos, state);
        setWorld(world);
    }

    @Override
    public Text getDisplayName() {
        return DefiningTableBlock.TITLE;
    }

//    @Override
//    public DefaultedList<ItemStack> getItems() {
//        return inventory;
//    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DefiningTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }

    public static void tick(World world, BlockPos pos, BlockState state, DefiningTableBlockEntity blockEntity) {}
}
