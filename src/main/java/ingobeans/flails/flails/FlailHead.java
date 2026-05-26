package ingobeans.flails.flails;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FlailHead extends Entity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER =
            SynchedEntityData.defineId(FlailHead.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public float angle;

    public FlailHead(EntityType<FlailHead> flailHeadEntityType, Level level) {
        super(flailHeadEntityType,level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(OWNER, Optional.empty());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    public void setOwner(EntityReference<LivingEntity> owner) {
        entityData.set(OWNER, Optional.of(owner));
    }
    public Optional<EntityReference<LivingEntity>> getOwner() {
        return entityData.get(OWNER);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

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
        orbitPos = getOwner().get().getEntity(l, LivingEntity.class).position();
        float range = 3.0f;
        this.angle -= 0.3f;
        Vec3 newPos = orbitPos.add(new Vec3(Math.cos(this.angle) * range,0.0f,Math.sin(this.angle) * range));
        this.setPos(newPos);
    }

    public static class FlailHeadEntityRenderState extends EntityRenderState {
    }
    public static class FlailHeadEntityModel extends EntityModel<FlailHeadEntityRenderState> {
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
