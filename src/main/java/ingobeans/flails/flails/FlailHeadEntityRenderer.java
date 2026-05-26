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
            l.end = state.orbitPos.add(0.5f,0.7f,0.0f);
            l.offset = new Vec3(0.0f,1.0f,0.0f);
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