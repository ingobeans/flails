package ingobeans.flails.flails.mixin;

import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    /*@Inject(method = "drop", at = @At(value = "HEAD"))
    public void drop(final ItemStack itemStack, final boolean randomly, final boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayer thisPlayer = (ServerPlayer)(Object)this;
        Main.LOGGER.info("hmm" + thisPlayer.getUseItem() + " -  wa  - " + itemStack.toString());

        if (thisPlayer.isUsingItem()) {
            if (itemStack.getItem() instanceof Flail flail) {
                flail.cancelUsing(thisPlayer);
            }
        }
    }*/

    @Inject(method = "Lnet/minecraft/server/level/ServerPlayer;drop(Z)V", at = @At(value = "HEAD"))
    public void drop(boolean all, CallbackInfo info) {
        ServerPlayer thisPlayer = (ServerPlayer)(Object)this;
        Main.LOGGER.info("hmm" + thisPlayer.getUseItem() + " -  wa  - ");

        if (thisPlayer.isUsingItem()) {
            if (thisPlayer.getInventory().getSelectedItem().getItem() instanceof Flail flail) {
                flail.cancelUsing(thisPlayer);
            }
        }
    }
}
