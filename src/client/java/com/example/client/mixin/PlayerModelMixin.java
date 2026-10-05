package com.example.client.mixin;

import com.example.util.SlideHolder;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void slide$onSetupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof SlideHolder holder && holder.slide$isSliding()) {
            PlayerModel<?> pm = (PlayerModel<?>) (Object) this;

            // Lower entire body closer to ground for an action knee-slide
            float dropY = 8.5F;
            this.body.y += dropY;
            this.head.y += dropY;
            this.hat.y += dropY;
            this.rightArm.y += dropY;
            this.leftArm.y += dropY;
            this.rightLeg.y += dropY;
            this.leftLeg.y += dropY;

            // Upper body leans back slightly into the slide (~18 degrees)
            this.body.xRot = (float) Math.toRadians(-18.0);
            this.body.yRot = 0.0F;
            this.body.zRot = 0.0F;

            // Head compensates for torso tilt to aim where looking
            this.head.xRot = (float) Math.toRadians(headPitch) + (float) Math.toRadians(18.0);
            this.head.yRot = (float) Math.toRadians(netHeadYaw);
            this.head.zRot = 0.0F;
            this.hat.copyFrom(this.head);

            // Right leg kicked forward along ground (action slide)
            this.rightLeg.xRot = (float) Math.toRadians(-75.0);
            this.rightLeg.yRot = (float) Math.toRadians(8.0);
            this.rightLeg.zRot = (float) Math.toRadians(5.0);

            // Left leg tucked underneath (knee on ground)
            this.leftLeg.xRot = (float) Math.toRadians(35.0);
            this.leftLeg.yRot = (float) Math.toRadians(-12.0);
            this.leftLeg.zRot = (float) Math.toRadians(-8.0);

            // Arms: balancing or aiming held item
            if (this.rightArmPose != HumanoidModel.ArmPose.EMPTY) {
                this.rightArm.xRot = (float) Math.toRadians(-35.0);
                this.rightArm.yRot = (float) Math.toRadians(-8.0);
                this.rightArm.zRot = (float) Math.toRadians(8.0);
            } else {
                this.rightArm.xRot = (float) Math.toRadians(-20.0);
                this.rightArm.yRot = (float) Math.toRadians(-10.0);
                this.rightArm.zRot = (float) Math.toRadians(15.0);
            }

            this.leftArm.xRot = (float) Math.toRadians(30.0);
            this.leftArm.yRot = (float) Math.toRadians(10.0);
            this.leftArm.zRot = (float) Math.toRadians(-18.0);

            // Synchronize skin outer layers
            pm.leftPants.copyFrom(this.leftLeg);
            pm.rightPants.copyFrom(this.rightLeg);
            pm.leftSleeve.copyFrom(this.leftArm);
            pm.rightSleeve.copyFrom(this.rightArm);
            pm.jacket.copyFrom(this.body);
        }
    }
}
