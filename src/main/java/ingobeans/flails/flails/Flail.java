package ingobeans.flails.flails;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class Flail extends Item {
    public Flail(Properties properties) {
        super(properties);
    }
    public FlailHead activeHead;
    public float headAngle = 0.0f;

    @Override
    public boolean releaseUsing(final ItemStack itemStack, final Level level, final LivingEntity entity, final int remainingTime) {
        if (level instanceof ServerLevel serverLevel) {
            this.activeHead.discard();
        }
        return false;
    }
    @Override
    public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
        if (level instanceof ServerLevel serverLevel) {
            float range = 3.0f;
            this.headAngle -= 0.16f;
            Vec3 newPos = livingEntity.position().add(new Vec3(Math.cos(this.headAngle) * range,0.0f,Math.sin(this.headAngle) * range));
            this.activeHead.setPos(newPos);
        }
    }
    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        user.startUsingItem(hand);


        if (level instanceof ServerLevel serverLevel) {
            FlailHead flailHead = new FlailHead(ModEntityTypes.FLAIL_HEAD, serverLevel);
            flailHead.owner = user;
            flailHead.setPos(user.position());
            serverLevel.addFreshEntity(flailHead);
            this.activeHead = flailHead;
        }

        return InteractionResult.CONSUME;

        /*
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            FlailHead flailHead = new FlailHead(ModEntityTypes.FLAIL_HEAD, serverLevel);
            flailHead.owner = user;
            flailHead.setPos(user.position());
            serverLevel.addFreshEntity(flailHead);
        }

        return InteractionResult.SUCCESS;*/
    }
}
