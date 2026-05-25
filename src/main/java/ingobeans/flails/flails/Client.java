package ingobeans.flails.flails;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerModelLayers();
        EntityRenderers.register(ModEntityTypes.FLAIL_HEAD, FlailHeadEntityRenderer::new);
        Main.LOGGER.info("client load :3");
    }
}