package ingobeans.flails.flails;

import com.mojang.serialization.Codec;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.level.Level;

import java.util.UUID;


public class Flail extends Item {
    public Flail(Properties properties) {
        super(properties);
    }

    public static final DataComponentType<String> ACTIVE_FLAIL_HEAD_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Main.MOD_ID, "active_flail_head_uuid"),
            DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    public float rotationsPerSecond = 0.8f;
    public float radius = 3.0f;


    public static Properties createFlailProperties() {
        return new Item.Properties().stacksTo(1)
                .component(DataComponents.USE_EFFECTS, new UseEffects(true, true, 0.4F))
                /*.attributes(ItemAttributeModifiers.builder()
                        .add(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        )
                .build())*/
                ;
    }

    public void cancelUsing(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            UpdateFlailAnimationPacket payload = new UpdateFlailAnimationPacket(0, EntityReference.of(entity), 0.0f);
            for (ServerPlayer player : PlayerLookup.level(serverLevel)) {
                ServerPlayNetworking.send(player, payload);
            }
        } else {
            if (entity instanceof Player user) {
                PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                        user, Main.USING_FLAIL_ANIMATION);
                controller.stop();
            }
        }
    }

    public Entity getFlailHead(ItemStack itemStack, Level level) {
        if (!itemStack.has(ACTIVE_FLAIL_HEAD_COMPONENT)) {
            return null;
        }
        String uuid = itemStack.get(ACTIVE_FLAIL_HEAD_COMPONENT);
        return level.getEntity(UUID.fromString(uuid));
    }

    @Override
    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        user.startUsingItem(hand);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, user.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.7f, 1f);
            FlailHead flailHead = new FlailHead(ModEntityTypes.FLAIL_HEAD, serverLevel);
            flailHead.setOwner(EntityReference.of(user));
            flailHead.setPos(user.position());
            flailHead.setRotationsPerSecond(rotationsPerSecond);
            flailHead.setAngle((user.getYRot()+45.0f) * 0.017453f);
            flailHead.setRadius(0.0f);
            flailHead.setTargetRadius(this.radius);
            serverLevel.addFreshEntity(flailHead);
            user.getItemInHand(hand).set(ACTIVE_FLAIL_HEAD_COMPONENT, flailHead.getStringUUID());

            UpdateFlailAnimationPacket payload = new UpdateFlailAnimationPacket(1, EntityReference.of(user), rotationsPerSecond);
            for (ServerPlayer player : PlayerLookup.level(serverLevel)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
        else {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    user, Main.USING_FLAIL_ANIMATION);

            controller.triggerAnimation(Main.USING_FLAIL_ANIMATION);
            controller.removeAllModifiers();
            controller.addModifierLast(new SpeedModifier(rotationsPerSecond));
        }

        return InteractionResult.CONSUME;
    }
}
