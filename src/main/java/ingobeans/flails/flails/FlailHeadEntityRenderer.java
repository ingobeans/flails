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

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@Environment(EnvType.CLIENT)
public class FlailHeadEntityRenderer<T extends Entity, S extends EntityRenderState, M extends EntityModel<? super S>>
        extends EntityRenderer<T, S>
        implements RenderLayerParent<S, M> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Main.MOD_ID, "textures/entity/flail_head.png");
    protected FlailHead.FlailHeadEntityModel model;

    public FlailHeadEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlailHead.FlailHeadEntityModel(context.bakeLayer(ModEntityModelLayers.FLAIL_HEAD));
    }

    public void submit(final S state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
        poseStack.pushPose();
        RenderType renderType = this.model.renderType(TEXTURE);
        int tintedColor = -1;
        int overlayCoords = OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(false));
        submitNodeCollector.submitModel((M)this.model, state, poseStack, renderType, state.lightCoords, overlayCoords, tintedColor, null, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public M getModel() {
        return (M)this.model;
    }

    @Override
    public S createRenderState() {
        return (S)(new FlailHead.FlailHeadEntityRenderState());
    }

}