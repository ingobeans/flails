package ingobeans.flails.flails;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
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
        PayloadTypeRegistry.clientboundPlay().register(UpdateFlailAnimationPacket.TYPE, UpdateFlailAnimationPacket.CODEC);
        LOGGER.info("flails loaded!!!!");
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, itemStack, enchantmentContext) -> {
            if (itemStack.getItem() instanceof Flail) {
               if (enchantment.is(Enchantments.KNOCKBACK)) {
                   return TriState.TRUE;
               }
            }
            return TriState.DEFAULT;
        });
	}
}