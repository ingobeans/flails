package ingobeans.flails.flails.mixin;

import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.player.LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "drop", at = @At(value = "HEAD"))
    public void drop(final boolean all, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer thisPlayer = (LocalPlayer)(Object)this;
        if (thisPlayer.isUsingItem()) {
            if (thisPlayer.getInventory().getSelectedItem().getItem() instanceof Flail flail) {
                flail.cancelUsing(thisPlayer);
            }
        }
    }
}
