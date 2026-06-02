package ingobeans.flails.flails.mixin;

import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
