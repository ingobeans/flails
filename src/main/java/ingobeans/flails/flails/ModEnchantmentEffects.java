package ingobeans.flails.flails;

import com.mojang.serialization.MapCodec;
import ingobeans.flails.flails.enchantments.CrushingEnchantmentEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {
    public static MapCodec<CrushingEnchantmentEffect> CRUSHING_EFFECT = register("crushing_effect", CrushingEnchantmentEffect.CODEC);

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(Main.MOD_ID, id), codec);
    }

    public static void registerModEnchantmentEffects() {
        Main.LOGGER.info("Registering EnchantmentEffects for" + Main.MOD_ID);
    }
}