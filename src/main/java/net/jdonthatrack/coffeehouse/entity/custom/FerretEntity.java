package net.jdonthatrack.coffeehouse.entity.custom;

import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class FerretEntity extends TameableEntity implements GeoEntity {
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(ModItems.UNDEFINED_CANDY);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FerretEntity(EntityType<? extends FerretEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
    }

    public static DefaultAttributeContainer.Builder createFerretAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));

        this.goalSelector.add(3, new TemptGoal(this, 1.25, TAMING_INGREDIENT, false));

    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(3) == 0) {
            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(20.0F);
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0);
        }

        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Nullable
    public FerretEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        FerretEntity ferretEntity = ModEntities.FERRET.create(serverWorld);
        if (ferretEntity != null) {
            UUID uuid = this.getOwnerUuid();
            if (uuid != null) {
                ferretEntity.setOwnerUuid(uuid);
                ferretEntity.setTamed(true);
            }
        }

        return ferretEntity;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return other != this && this.isTamed() && other instanceof FerretEntity otherFerretEntity && otherFerretEntity.isTamed() && otherFerretEntity.isInSittingPose() && this.isInLove() && otherFerretEntity.isInLove();
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isTamed() && this.age > 2400;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return super.canBeLeashedBy(player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.ferret.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.ferret.sit", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(RawAnimation.begin().then("animation.ferret.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (TAMING_INGREDIENT.test(stack) && !isTamed()) {
            if (this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            }
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            super.setOwner(player);
            this.navigation.recalculatePath();
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, (byte) 7);
            this.setSitting(true);

            return ActionResult.SUCCESS;
        }

        if (isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND) {
            this.setSitting(!isSitting());
            return ActionResult.SUCCESS;
        }

        if (TAMING_INGREDIENT.test(stack)) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }
}