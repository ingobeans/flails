package ingobeans.flails.flails;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class FlailHeadEntityRenderer extends EntityRenderer<FlailHead, FlailHead.FlailHeadEntityRenderState> {
    public FlailHeadEntityRenderer(EntityRendererProvider.Context context) {
        super(context); // 0.375 shadow radius
    }

    @Override
    public FlailHead.FlailHeadEntityRenderState createRenderState() {
        return new FlailHead.FlailHeadEntityRenderState();
    }
}