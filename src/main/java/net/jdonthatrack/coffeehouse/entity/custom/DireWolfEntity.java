package net.jdonthatrack.coffeehouse.entity.custom;

import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Objects;

public class DireWolfEntity extends AbstractHorseEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final TrackedData<Byte> HORSE_FLAGS;

    public DireWolfEntity(EntityType<? extends DireWolfEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.HORSE_JUMP_STRENGTH)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 53.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22499999403953552);
    }
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
        this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.2));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0, AbstractHorseEntity.class));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        if (this.shouldAmbientStand()) {
            this.goalSelector.add(9, new AmbientStandGoal(this));
        }

        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new TemptGoal(this, 1.25, Ingredient.ofItems(new ItemConvertible[]{Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}), false));
    }

    protected void initAttributes(Random random) {
        EntityAttributeInstance var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(random);
        var10000.setBaseValue((double)getChildHealthBonus(random::nextInt));
        var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        Objects.requireNonNull(random);
        var10000.setBaseValue(getChildMovementSpeedBonus(random::nextDouble));
        var10000 = this.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH);
        Objects.requireNonNull(random);
        var10000.setBaseValue(getChildJumpStrengthBonus(random::nextDouble));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HORSE_FLAGS, (byte)0);
    }

    public boolean getHorseFlag(int bitmask) {
        return ((Byte)this.dataTracker.get(HORSE_FLAGS) & bitmask) != 0;
    }

    protected void setHorseFlag(int bitmask, boolean flag) {
        byte b = (Byte)this.dataTracker.get(HORSE_FLAGS);
        if (flag) {
            this.dataTracker.set(HORSE_FLAGS, (byte)(b | bitmask));
        } else {
            this.dataTracker.set(HORSE_FLAGS, (byte)(b & ~bitmask));
        }

    }

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return super.getOwner();
    }

    @Override
    public int getJumpCooldown() {
        return super.getJumpCooldown();
    }

    @Override
    public SoundEvent getSaddleSound() {
        return super.getSaddleSound();
    }

    @Override
    public boolean cannotBeSilenced() {
        return super.cannotBeSilenced();
    }

    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.DIRE_WOLF.create(world);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.wolf.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(RawAnimation.begin().then("animation.wolf.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).bodyYaw = this.bodyYaw;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static {
        HORSE_FLAGS = DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.BYTE);
    }
}