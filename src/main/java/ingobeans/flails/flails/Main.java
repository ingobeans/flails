package ingobeans.flails.flails;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
    public static final String MOD_ID = "flails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier USING_FLAIL_ANIMATION = Identifier.fromNamespaceAndPath(MOD_ID, "using_flail");
    public static final ResourceKey<DamageType> FLAIL_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Main.MOD_ID, "flail"));

	@Override
	public void onInitialize() {
        ModItems.initialize();
        ModEntityTypes.registerModEntityTypes();
        ModEnchantmentEffects.registerModEnchantmentEffects();
        LOGGER.info("flails loaded!!!!");
	}
}