package ingobeans.flails.flails;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
    public static final ModelLayerLocation FLAIL_HEAD = createMain("flail_head");
    public static final ModelLayerLocation FLAIL_CHAIN = createMain("flail_chain");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Main.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.FLAIL_HEAD, FlailHead.FlailHeadEntityModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.FLAIL_CHAIN, ChainModel::createBodyLayer);
    }
}
