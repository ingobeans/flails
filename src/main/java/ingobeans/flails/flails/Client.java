package ingobeans.flails.flails;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

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
    }
}