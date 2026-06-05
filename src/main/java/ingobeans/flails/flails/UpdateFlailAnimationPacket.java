package ingobeans.flails.flails;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityReference;

public record UpdateFlailAnimationPacket(int state, EntityReference entityReference) implements CustomPacketPayload {
    public static final Identifier UPDATE_FLAIL_ANIMATION_ID = Identifier.fromNamespaceAndPath(Main.MOD_ID, "summon_lightning");

    public static final CustomPacketPayload.Type<UpdateFlailAnimationPacket> TYPE = new CustomPacketPayload.Type<>(UPDATE_FLAIL_ANIMATION_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFlailAnimationPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateFlailAnimationPacket::state,
            EntityReference.streamCodec(), UpdateFlailAnimationPacket::entityReference,
            UpdateFlailAnimationPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}