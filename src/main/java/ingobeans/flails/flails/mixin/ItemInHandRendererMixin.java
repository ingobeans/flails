package ingobeans.flails.flails.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow
    private void applyItemArmTransform(final PoseStack poseStack, final HumanoidArm arm, final float inverseArmHeight) {
    }


    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    void renderArmWithItem(
            final AbstractClientPlayer player,
            final float frameInterp,
            final float xRot,
            final InteractionHand hand,
            final float attack,
            final ItemStack itemStack,
            final float inverseArmHeight,
            final PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            CallbackInfo info
    ) {
        if (!player.isScoping()) {
            boolean isMainHand = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            if (itemStack.getItem() instanceof Flail) {
                boolean isRightArm = arm == HumanoidArm.RIGHT;
                int invert = isRightArm ? 1 : -1;
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                    poseStack.pushPose();
                    this.applyItemArmTransform(poseStack, arm, inverseArmHeight);

                    poseStack.translate(invert * -0.2F, 0.7F, 0.1F);
                    //poseStack.mulPose(Axis.XP.rotationDegrees(-55.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(invert * 30F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(invert * -9.785F));

                    float timeHeldxx = itemStack.getUseDuration(player) - (player.getUseItemRemainingTicks() - frameInterp + 1.0F);
                    float powerx = timeHeldxx / 10.0F;
                    if (powerx > 1.0F) {
                        powerx = 1.0F;
                    }

                    if (powerx > 0.1F) {
                        float shakeOffset = Mth.sin((timeHeldxx - 0.1F) * 1.3F);
                        float shakeIntensity = powerx - 0.1F;
                        float shake = shakeOffset * shakeIntensity;
                        poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
                    }

                    poseStack.translate(0.0F, 0.0F, powerx * 0.2F);
                    poseStack.scale(1.0F, 1.0F, 1.0F + powerx * 0.2F);
                    poseStack.mulPose(Axis.YN.rotationDegrees(invert * 45.0F));

                    ((ItemInHandRenderer)(Object)this).renderItem(
                            player,
                            itemStack,
                            isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                            poseStack,
                            submitNodeCollector,
                            lightCoords
                    );
                    poseStack.popPose();
                    info.cancel();
                }

            }

        }
    }
}
