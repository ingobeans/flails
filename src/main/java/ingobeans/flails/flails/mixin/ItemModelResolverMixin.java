package ingobeans.flails.flails.mixin;


import ingobeans.flails.flails.Flail;
import ingobeans.flails.flails.Main;
import ingobeans.flails.flails.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @Shadow
    ItemModel getItemModel(final Identifier modelId) {
        throw new AssertionError();
    }

    @Shadow
    ClientItem.Properties getItemProperties(final Identifier modelId) {
        throw new AssertionError();
    }

    @Inject(method = "appendItemLayers", at = @At(value = "HEAD"), cancellable = true)
    public void appendItemLayers(
            final ItemStackRenderState output,
            final ItemStack item,
            final ItemDisplayContext displayContext,
            @Nullable final Level level,
            @Nullable final ItemOwner owner,
            final int seed,
            CallbackInfo info
    ) {
        if (displayContext != ItemDisplayContext.GUI && displayContext != ItemDisplayContext.GROUND) {
            if (item.getItem() instanceof Flail flail) {
                ItemStack is = new ItemStack(ModItems.FLAIL_3D);
                Identifier modelId = is.get(DataComponents.ITEM_MODEL);
                this.getItemModel(modelId).update(output, item, (ItemModelResolver)(Object)this, displayContext, level instanceof ClientLevel clientLevel ? clientLevel : null, owner, seed);
                info.cancel();
            }
        }
    }
}