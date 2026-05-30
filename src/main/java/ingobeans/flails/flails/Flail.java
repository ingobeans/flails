package ingobeans.flails.flails;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.level.Level;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

public class Flail extends Item {
    public Flail(Properties properties) {
        super(properties);
    }
    public FlailHead activeHead;
    public float rotationsPerSecond = 0.8f;
    public float radius = 3.0f;

    public static Properties createFlailProperties() {
        return new Item.Properties().stacksTo(1)
                .component(DataComponents.USE_EFFECTS, new UseEffects(true, true, 0.4F))
                .attributes(ItemAttributeModifiers.builder()
                        .add(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        )
                .build());
    }

    @Override
    public boolean releaseUsing(final ItemStack itemStack, final Level level, final LivingEntity entity, final int remainingTime) {
        if (level instanceof ServerLevel serverLevel) {
            this.activeHead.discard();
        } else {
            if (entity instanceof Player user) {
                PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                        user, Main.USING_FLAIL_ANIMATION);
                controller.stop();
            }
        }
        return false;
    }

    @Override
    public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
        /*
        if (livingEntity instanceof Player user) {

            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    user, Main.USE_FLAIL_ANIMATION);
            if (controller.hasAnimatoinFinished()) {
                controller.triggerAnimation(Main.USING_FLAIL_ANIMATION);
        }
        }*/
        /*if (level instanceof ServerLevel serverLevel) {
            float range = 3.0f;
            this.headAngle -= 0.16f;
            Vec3 newPos = livingEntity.position().add(new Vec3(Math.cos(this.headAngle) * range,0.0f,Math.sin(this.headAngle) * range));
            this.activeHead.setPos(newPos);
        }*/

    }


    @Override
    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        user.startUsingItem(hand);


        if (level instanceof ServerLevel serverLevel) {
            FlailHead flailHead = new FlailHead(ModEntityTypes.FLAIL_HEAD, serverLevel);
            flailHead.setOwner(EntityReference.of(user));
            flailHead.setPos(user.position());
            flailHead.setRotationsPerSecond(rotationsPerSecond);
            flailHead.setAngle((user.getYRot()+45.0f) * 0.017453f);
            flailHead.setRadius(0.0f);
            flailHead.setTargetRadius(this.radius);
            serverLevel.addFreshEntity(flailHead);
            this.activeHead = flailHead;
        }
        else {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    user, Main.USING_FLAIL_ANIMATION);

            controller.triggerAnimation(Main.USING_FLAIL_ANIMATION);
            controller.removeAllModifiers();
            controller.addModifierLast(new SpeedModifier(rotationsPerSecond));
        }

        return InteractionResult.CONSUME;

        /*
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            FlailHead flailHead = new FlailHead(ModEntityTypes.FLAIL_HEAD, serverLevel);
            flailHead.owner = user;
            flailHead.setPos(user.position());
            serverLevel.addFreshEntity(flailHead);
        }

        return InteractionResult.SUCCESS;*/
    }
}
