package net.jdonthatrack.coffeehouse.entity.custom;

import net.jdonthatrack.coffeehouse.entity.ModEntities;
import net.jdonthatrack.coffeehouse.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RaptorEntity extends AbstractHorseEntity implements GeoEntity {
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(ModItems.UNDEFINED_CANDY);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RaptorEntity(EntityType<? extends RaptorEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 53.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22499999403953552);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
        this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.2));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0, AbstractHorseEntity.class));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new TemptGoal(this, 1.25, TAMING_INGREDIENT, false));
    }


    @Override
    protected void initAttributes(Random random) {
        EntityAttributeInstance attributeInstance;
        attributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        attributeInstance.setBaseValue(getChildHealthBonus(random::nextInt));
        attributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        attributeInstance.setBaseValue(getChildMovementSpeedBonus(random::nextDouble));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    public RaptorEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.RAPTOR.create(world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.raptor.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(RawAnimation.begin().then("animation.raptor.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (passenger instanceof LivingEntity livingPassenger) {
            livingPassenger.bodyYaw = this.bodyYaw;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        boolean bl = !this.isBaby() && this.isTame() && player.shouldCancelInteraction();
        if (!this.hasPassengers() && !bl) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (!itemStack.isEmpty()) {
                if (this.isBreedingItem(itemStack)) {
                    return this.interactHorse(player, itemStack);
                }

                if (!this.isTame()) {
                    this.playAngrySound();
                    return ActionResult.success(this.getWorld().isClient);
                }
            }

        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        } else {
            return this.canBreed();
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}