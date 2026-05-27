package ingobeans.flails.flails;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
    public static final String MOD_ID = "flails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier USE_FLAIL_ANIMATION = Identifier.fromNamespaceAndPath(Main.MOD_ID, "use_flail");
    public static Identifier USING_FLAIL_ANIMATION = Identifier.fromNamespaceAndPath(Main.MOD_ID, "using_flail");

	@Override
	public void onInitialize() {
        ModItems.initialize();
        ModEntityTypes.registerModEntityTypes();
        LOGGER.info("flails loaded!!!!");

	}
}