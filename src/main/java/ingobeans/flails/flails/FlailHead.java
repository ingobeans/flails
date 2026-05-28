package ingobeans.flails.flails;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FlailHead extends Entity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER =
            SynchedEntityData.defineId(FlailHead.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    private static final EntityDataAccessor<Float> ROTATIONS_PER_SECOND =
            SynchedEntityData.defineId(FlailHead.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> ANGLE =
            SynchedEntityData.defineId(FlailHead.class, EntityDataSerializers.FLOAT);

    public FlailHead(EntityType<FlailHead> flailHeadEntityType, Level level) {
        super(flailHeadEntityType,level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(OWNER, Optional.empty());
        entityData.define(ROTATIONS_PER_SECOND, 1.0f);
        entityData.define(ANGLE, 1.0f);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putFloat("rotations_per_second", entityData.get(ROTATIONS_PER_SECOND));
        valueOutput.putFloat("angle", entityData.get(ANGLE));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        entityData.set(ROTATIONS_PER_SECOND,valueInput.getFloatOr("rotations_per_second",0.0f));
        entityData.set(ANGLE,valueInput.getFloatOr("angle",0.0f));
    }

    public void setOwner(EntityReference<LivingEntity> owner) {
        entityData.set(OWNER, Optional.of(owner));
    }
    public void setRotationsPerSecond(float rotationsPerSecond) {
        entityData.set(ROTATIONS_PER_SECOND, rotationsPerSecond);
    }
    public void setAngle(float angle) {
        entityData.set(ANGLE, angle);
    }
    public float getAngle() {
        return entityData.get(ANGLE);
    }
    public Optional<EntityReference<LivingEntity>> getOwner() {
        return entityData.get(OWNER);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 orbitPos;
        if (getOwner().isEmpty()) {
            this.discard();
            return;
        }
        Level l = level();
        Player owner = (Player)getOwner().get().getEntity(l, LivingEntity.class);
        if (owner == null) {
            this.discard();
            return;
        }
        if (owner.getUseItem().getItem() instanceof Flail flail) {
            if (!l.isClientSide()) {
                if (flail.activeHead != this) {
                    this.discard();
                    return;
                }
            }
        } else {
            this.discard();
            return;
        }
        orbitPos = owner.position();
        float range = 3.0f;
        float angle = entityData.get(ANGLE);
        angle -= 0.314f * entityData.get(ROTATIONS_PER_SECOND);
        Vec3 newPos = orbitPos.add(new Vec3(Math.cos(angle) * range,0.0f,Math.sin(angle) * range));
        entityData.set(ANGLE,angle);
        this.setPos(newPos);

        if (l instanceof ServerLevel level && this.isAlive()) {
            for (LivingEntity entity : level
                    .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3), target -> true)) {
                if (entity.isAlive()) {
                    Main.LOGGER.info("HIT ENTITY!!" + entity.toString());;
                    // damage source not working >:3

                    /*DamageSource damageSource = new DamageSource(
                            level.registryAccess()
                                    .lookupOrThrow(Registries.DAMAGE_TYPE)
                                    .get(Main.FLAIL_DAMAGE.identifier()).orElseThrow()
                    );

                    entity.hurtServer(level,damageSource,4.0f);*/
                }
            }
        }
    }

    public static class FlailHeadEntityModel extends EntityModel<FlailHeadRenderState> {
        public FlailHeadEntityModel(ModelPart root) {
            super(root);
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(1, 1).addBox(-7.0F, -14.0F, 0.0F, 13.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-6.5F, -14.0F, 0.0F, 13.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

            PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(1, 1).addBox(-6.5F, -14.0F, 0.0F, 13.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, 1).addBox(-6.5F, -14.0F, 0.0F, 13.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

            return LayerDefinition.create(meshdefinition, 32, 16);

        }
    }
}
