package ingobeans.flails.flails.mixin;


import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.math.Vec3f;
import ingobeans.flails.flails.FlailHead;
import ingobeans.flails.flails.FlailHeadRenderState;
import ingobeans.flails.flails.Main;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class RenderStateMixin<T extends Entity, S extends EntityRenderState>{
    Vec3 vec3fToVec3(Vec3f input) {
        return new Vec3(input.x(),input.y(),input.z());
    }

    @Inject(method = "extractRenderState", at= @At("HEAD"))
    protected void extractRenderState(final T entity, final S state, final float partialTicks, CallbackInfo info) {
        if (state instanceof FlailHeadRenderState flailHeadRenderState) {
            if (entity instanceof FlailHead flailHead) {
                LivingEntity owner = flailHead.getOwner().get().getEntity(entity.level(), LivingEntity.class);
                flailHeadRenderState.orbitPos = owner.position();
                flailHeadRenderState.playerYaw = owner.yBodyRot * 0.017453f;
                flailHeadRenderState.angle = flailHead.getAngle();
            }
        }
    }
}