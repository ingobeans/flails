package ingobeans.flails.flails;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import org.joml.Quaternionf;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@Environment(EnvType.CLIENT)
public class FlailHeadEntityRenderer
        extends EntityRenderer<FlailHead, FlailHeadRenderState>{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Main.MOD_ID, "textures/entity/flail_head.png");
    private static final Identifier CHAIN_TEXTURE = Identifier.withDefaultNamespace("textures/block/iron_chain.png");
    protected FlailHead.FlailHeadEntityModel model;
    protected ChainModel chainModel;

    public FlailHeadEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlailHead.FlailHeadEntityModel(context.bakeLayer(ModEntityModelLayers.FLAIL_HEAD));
        this.chainModel = new ChainModel(context.bakeLayer(ModEntityModelLayers.FLAIL_CHAIN));
    }

    public void submit(final FlailHeadRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
        poseStack.pushPose();
        RenderType renderType = this.model.renderType(TEXTURE);
        int tintedColor = -1;
        int overlayCoords = OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(false));
        submitNodeCollector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, overlayCoords, tintedColor, null, state.outlineColor,null);


        if (state.orbitPos != null) {
            Vec3 heightOffset = new Vec3(0.0f,1.9f,0.0f);
            float armOffsetLength = 0.5f;
            Vec3 armOffset = new Vec3(Math.cos(state.playerYaw + 3.14f) * armOffsetLength,0.0f,Math.sin(state.playerYaw + 3.14f) * armOffsetLength);
            /*float armAngleOffsetLength = 0.1f;
            Vec3 armAngleOffset = new Vec3(
                    Math.cos(state.playerYaw+state.angle-0.7854f+3.14f) * armAngleOffsetLength,
                    0.0f,
                    Math.sin(state.playerYaw+state.angle-0.7854f+3.14f) * armAngleOffsetLength
            );*/
            Vec3 start = new Vec3(state.x,state.y+1.0f,state.z);
            Vec3 end = state.orbitPos.add(heightOffset).add(armOffset);
            Vec3 offset = new Vec3(0.0f,1.0f,0.0f);

            // figure out all chain links that need to be added
            Vec3 delta = end.subtract(start);
            int stepAmount = (int)Math.ceil(state.orbitPos.add(heightOffset).subtract(start).length() / 0.375f);
            Vec3 step = delta.scale(1.0f / (float)stepAmount);
            Main.LOGGER.info("stepAmount:{}, stepVector:{}", stepAmount, step);

            RenderType chainRenderType = this.chainModel.renderType(CHAIN_TEXTURE);

            float angleY = (float) Math.atan2(delta.z(), delta.x()) - 1.57f;
            poseStack.popPose();
            for (int i = 3; i < stepAmount+3; i++) {
                Vec3 pos = step.scale(-i).add(offset).add(delta);
                poseStack.pushPose();

                // i have no clue why the pivot Y needs to be offset by 0.5, and it took me two days to figure it out (through trial and error).
                // don't blame me
                poseStack.rotateAround(new Quaternionf().rotateXYZ(1.57f, 0, angleY),(float)pos.x,(float)pos.y+0.5f,(float)pos.z);
                poseStack.translate(pos.x,pos.y,pos.z);
                submitNodeCollector.submitModel(this.chainModel, state, poseStack, chainRenderType, state.lightCoords, overlayCoords, tintedColor, null, state.outlineColor,null);
                poseStack.popPose();
            }
        }

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public FlailHeadRenderState createRenderState() {
        return new FlailHeadRenderState();
    }

}