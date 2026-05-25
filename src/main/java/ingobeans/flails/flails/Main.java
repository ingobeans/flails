package ingobeans.flails.flails;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
    public static final String MOD_ID = "flails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
        ModItems.initialize();
        ModEntityTypes.registerModEntityTypes();
        LOGGER.info("flails loaded!!!!");
	}
}