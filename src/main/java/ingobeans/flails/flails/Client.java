package ingobeans.flails.flails;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerModelLayers();
        EntityRenderers.register(ModEntityTypes.FLAIL_HEAD, FlailHeadEntityRenderer::new);
        Main.LOGGER.info("client load :3");
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Main.USING_FLAIL_ANIMATION, 1000,
                player -> new PlayerAnimationController(player,
                        (controller, state, animSetter) -> PlayState.STOP
                )
        );

        ClientPlayNetworking.registerGlobalReceiver(UpdateFlailAnimationPacket.TYPE, (payload, context) -> {
            ClientLevel level = context.client().level;

            if (level == null) {
                return;
            }

            Integer state = payload.state();

            EntityReference reference = payload.entityReference();
            LivingEntity entity = (LivingEntity) reference.getEntity(level, LivingEntity.class);

            Float speed = payload.speed();

            if (entity instanceof Player user) {
                PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                        user, Main.USING_FLAIL_ANIMATION);

                if (state == 0) {
                    controller.stop();
                } else if (state == 1) {
                    controller.triggerAnimation(Main.USING_FLAIL_ANIMATION);
                    controller.removeAllModifiers();
                    controller.addModifierLast(new SpeedModifier(speed));
                }
            }
        });
    }
}