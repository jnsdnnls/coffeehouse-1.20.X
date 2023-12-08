package net.jdonthatrack.coffeehouse.block.custom;

import net.jdonthatrack.coffeehouse.block.entity.DefiningTableBlockEntity;
import net.jdonthatrack.coffeehouse.block.entity.ModBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DefiningTableBlock extends BlockWithEntity {
    public static final Text TITLE = Text.translatable("container.coffeehouse.defining_table");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape SHAPE = makeShape();

    public DefiningTableBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.0625, 0.375, 0.625, 0.9375, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.5, 0.5625, 0.6875, 0.9375, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.5, 0.5625, 0.4375, 0.9375, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.5, 0.3125, 0.6875, 0.9375, 0.4375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.5, 0.3125, 0.4375, 0.9375, 0.4375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.0625, 0.6875));

        return shape;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.CONSUME;
    }

    @Override
    public MutableText getName() {
        return TITLE.copy();
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new DefiningTableBlockEntity(pos, state, world);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DefiningTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntityTypes.DEFINING_TABLE, DefiningTableBlockEntity::tick);
    }
}