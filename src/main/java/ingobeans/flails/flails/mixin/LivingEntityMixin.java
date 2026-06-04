package ingobeans.flails.flails.mixin;

import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ingobeans.flails.flails.ModEnchantmentEffects.CRUSHING_EFFECT;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "stopUsingItem", at = @At(value = "HEAD"))
    public void stopUsingItem(CallbackInfo info) {
        LivingEntity thisEntity = (LivingEntity)(Object)this;
        if (thisEntity.level().isClientSide() && thisEntity.isUsingItem()) {
            if (thisEntity.getUseItem().getItem() instanceof Flail flail) {
                flail.cancelUsing(thisEntity);
            }
        }
    }
    /*@Inject(method = "getSecondsToDisableBlocking", at = @At(value = "HEAD"))
    public void getSecondsToDisableBlocking(CallbackInfoReturnable<Float> cir) {
        LivingEntity thisEntity = (LivingEntity)(Object)this;
        ItemStack weaponItem = thisEntity.getWeaponItem();
        Main.LOGGER.info("WA");
        if (weaponItem.getItem() instanceof Flail flail) {

            ItemEnchantments itemEnchantments = weaponItem.get(DataComponents.ENCHANTMENTS);
            if (itemEnchantments != null && !itemEnchantments.isEmpty()) {
                for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                    Main.LOGGER.info(entry.toString());
                }
            }
        }
    }*/
}
