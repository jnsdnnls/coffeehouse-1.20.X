package net.jdonthatrack.coffeehouse.item.custom;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.jdonthatrack.coffeehouse.CoffeeHouse;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DynamicSpawnEggItem extends Item implements DynamicModelItem {

    private static final Map<EntityType<? extends MobEntity>, DynamicSpawnEggItem> SPAWN_EGGS = Maps.newHashMap(); // Use a String key instead of EntityType
    private final EntityType<?> type;

    public DynamicSpawnEggItem(EntityType<? extends MobEntity> type, Settings settings) {
        super(settings);
        this.type = type;
        SPAWN_EGGS.put(type, this); // Use EntityType name as a key
    }

    @Nullable
    public static DynamicSpawnEggItem forEntity(@Nullable EntityType<? extends MobEntity> type) {
        return SPAWN_EGGS.get(type);
    }

    public static Iterable<DynamicSpawnEggItem> getAll() {
        return Iterables.unmodifiableIterable(SPAWN_EGGS.values());
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SPAWNER)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
                EntityType<?> entityType = this.getEntityType(itemStack.getNbt());
                mobSpawnerBlockEntity.setEntityType(entityType, world.getRandom());
                blockEntity.markDirty();
                world.updateListeners(blockPos, blockState, blockState, 3);
                world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                itemStack.decrement(1);
                return ActionResult.CONSUME;
            }
        }

        BlockPos blockPos2;
        if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
            blockPos2 = blockPos;
        } else {
            blockPos2 = blockPos.offset(direction);
        }

        EntityType<?> entityType2 = this.getEntityType(itemStack.getNbt());
        if (entityType2.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos2, SpawnReason.SPAWN_EGG, true, blockPos != blockPos2 && direction == Direction.UP) != null) {
            itemStack.decrement(1);
            world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else if (!(world instanceof ServerWorld)) {
            return TypedActionResult.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
                return TypedActionResult.pass(itemStack);
            } else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
                EntityType<?> entityType = this.getEntityType(itemStack.getNbt());
                Entity entity = entityType.spawnFromItemStack((ServerWorld) world, itemStack, user, blockPos, SpawnReason.SPAWN_EGG, false, false);
                if (entity == null) {
                    return TypedActionResult.pass(itemStack);
                } else {
                    if (!user.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
                    return TypedActionResult.consume(itemStack);
                }
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    public boolean isOfSameEntityType(@Nullable NbtCompound nbt, EntityType<?> type) {
        return Objects.equals(this.getEntityType(nbt), type);
    }

    public EntityType<?> getEntityType(@Nullable NbtCompound nbt) {
        if (DynamicModelItem.hasModel(nbt)) {
            String modelName = DynamicModelItem.getModel(nbt);
            // Example: modelName is "unicycle", "other_custom_mob", etc.
            EntityType<?> customType = EntityType.get(new Identifier(CoffeeHouse.MOD_ID, modelName).toString()).orElse(null);

            if (customType != null) {
                return customType;
            }
        }

        return this.type;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String currentModel;
        if (DynamicModelItem.hasModel(stack)) {
            currentModel = DynamicModelItem.getModel(stack);
        } else {
            currentModel = "Undefined";
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

        return Text.translatable("item.coffeehouse.custom_item", capitalize(currentModel), "Spawn Egg");
    }

    public FeatureSet getRequiredFeatures() {
        return this.type.getRequiredFeatures();
    }
}
