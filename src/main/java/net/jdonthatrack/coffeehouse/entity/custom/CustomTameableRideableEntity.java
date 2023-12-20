package net.jdonthatrack.coffeehouse.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class CustomTameableRideableEntity extends TameableEntity implements JumpingMount, Saddleable {
    protected static final TrackedData<Integer> BOOST_TIME = DataTracker.registerData(CustomTameableRideableEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> SADDLED = DataTracker.registerData(CustomTameableRideableEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected final SaddledComponent saddledComponent;

    protected CustomTameableRideableEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.saddledComponent = new SaddledComponent(this.dataTracker, BOOST_TIME, SADDLED);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (BOOST_TIME.equals(data) && this.getWorld().isClient) {
            this.saddledComponent.boost();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SADDLED, false);
        this.dataTracker.startTracking(BOOST_TIME, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.saddledComponent.writeNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.saddledComponent.readNbt(nbt);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        boolean bl = this.isBreedingItem(player.getStackInHand(hand));
        if (!bl && this.isSaddled() && !this.hasPassengers() && !player.shouldCancelInteraction()) {
            if (!this.getWorld().isClient) {
                player.startRiding(this);
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        ActionResult actionResult = super.interactMob(player, hand);
        if (!actionResult.isAccepted()) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.isOf(Items.SADDLE)) {
                return itemStack.useOnEntity(player, this, hand);
            }
            return ActionResult.PASS;
        }
        return actionResult;
    }

    @Override
    public boolean canBeSaddled() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.isSaddled()) {
            this.dropItem(Items.SADDLE);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.saddledComponent.isSaddled();
    }

    @Override
    public void saddle(@Nullable SoundCategory sound) {
        this.saddledComponent.setSaddled(true);
        if (sound != null) {
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_PIG_SADDLE, sound, 0.5f, 1.0f);
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (EntityPose entityPose : passenger.getPoses()) {
            Box box = passenger.getBoundingBox(entityPose);
            for (int[] js : is) {
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.getWorld().getDismountHeight(mutable);
                if (!Dismounting.canDismountInBlock(d)) continue;
                Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                if (!Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5f);
        this.bodyYaw = this.headYaw = this.getYaw();
        this.prevYaw = this.headYaw;
        this.saddledComponent.tickBoost();
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        return new Vec3d(0.0, 0.0, 1.0);
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
        return (float)(this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 0.225 * (double)this.saddledComponent.getMovementSpeedMultiplier());
    }

    @Override
    public void setJumpStrength(int strength) {

    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void startJumping(int height) {

    }

    @Override
    public void stopJumping() {

    }
}
