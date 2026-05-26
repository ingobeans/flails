package ingobeans.flails.flails.mixin;


import ingobeans.flails.flails.FlailHead;
import ingobeans.flails.flails.FlailHeadRenderState;
import ingobeans.flails.flails.Main;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class RenderStateMixin<T extends Entity, S extends EntityRenderState>{
    @Inject(method = "extractRenderState", at= @At("HEAD"))
    protected void extractRenderState(final T entity, final S state, final float partialTicks, CallbackInfo info) {
        if (state instanceof FlailHeadRenderState flailHeadRenderState) {
            if (entity instanceof FlailHead flailHead) {
                flailHeadRenderState.orbitPos = flailHead.getOwner().get().getEntity(entity.level(), LivingEntity.class).position();
            }
        }
    }
}