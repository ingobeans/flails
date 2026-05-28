package ingobeans.flails.flails;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@Environment(EnvType.CLIENT)
public class FlailHeadEntityRenderer
        extends EntityRenderer<FlailHead, FlailHeadRenderState>{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Main.MOD_ID, "textures/entity/flail_head.png");
    protected FlailHead.FlailHeadEntityModel model;

    public FlailHeadEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlailHead.FlailHeadEntityModel(context.bakeLayer(ModEntityModelLayers.FLAIL_HEAD));
    }

    public void submit(final FlailHeadRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
        poseStack.pushPose();
        RenderType renderType = this.model.renderType(TEXTURE);
        int tintedColor = -1;
        int overlayCoords = OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(false));
        submitNodeCollector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, overlayCoords, tintedColor, null, state.outlineColor,null);

        if (state.orbitPos != null) {
            EntityRenderState.LeashState l = new EntityRenderState.LeashState();
            l.start = new Vec3(state.x,state.y,state.z);
            l.offset = new Vec3(0.0f,1.0f,0.0f);

            Vec3 heightOffset = new Vec3(0.0f,1.1f,0.0f);
            float armOffsetLength = 0.5f;
            Vec3 armOffset = new Vec3(Math.cos(state.playerYaw + 3.14f) * armOffsetLength,0.0f,Math.sin(state.playerYaw + 3.14f) * armOffsetLength);
            /*float armAngleOffsetLength = 0.1f;
            Vec3 armAngleOffset = new Vec3(
                    Math.cos(state.playerYaw+state.angle-0.7854f+3.14f) * armAngleOffsetLength,
                    0.0f,
                    Math.sin(state.playerYaw+state.angle-0.7854f+3.14f) * armAngleOffsetLength
            );*/

            l.end = state.orbitPos.add(heightOffset).add(armOffset);//.add(armAngleOffset);
            submitNodeCollector.submitLeash(poseStack, l);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public FlailHeadRenderState createRenderState() {
        return new FlailHeadRenderState();
    }

}