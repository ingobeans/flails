package ingobeans.flails.flails;

import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerModelLayers();
        Main.LOGGER.info("client load");
    }
}