package com.example.mixin;

import com.example.util.SlideHolder;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements SlideHolder {

    @Unique private boolean rivals_isSliding = false;
    @Unique private int rivals_slideTicks = 0;
    @Unique private int rivals_slideCooldown = 0;
    @Unique private Vec3 rivals_preTravelVel = Vec3.ZERO;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean rivals$isSliding() {
        return this.rivals_isSliding;
    }

    @Override
    public void rivals$setSliding(boolean sliding) {
        this.rivals_isSliding = sliding;
        if (!sliding) {
            this.rivals_slideTicks = 0;
        }
    }

    @Override
    public int rivals$getSlideTicks() {
        return this.rivals_slideTicks;
    }

    @Override
    public void rivals$setSlideTicks(int ticks) {
        this.rivals_slideTicks = ticks;
    }

    @Override
    public int rivals$getSlideCooldown() {
        return this.rivals_slideCooldown;
    }

    @Override
    public void rivals$setSlideCooldown(int cooldown) {
        this.rivals_slideCooldown = cooldown;
    }

    @Override
    public boolean rivals$canStand() {
        return this.canEnterPose(Pose.STANDING);
    }

    @Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
    protected void rivals$onGetStandingEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        if (this.rivals_isSliding) {
            cir.setReturnValue(0.85F);
        }
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    protected void rivals$onUpdatePlayerPose(CallbackInfo ci) {
        if (this.rivals_isSliding) {
            this.setPose(Pose.SWIMMING);
            ci.cancel();
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void rivals$onTravelHead(Vec3 travelVector, CallbackInfo ci) {
        if (this.rivals_isSliding) {
            this.rivals_preTravelVel = this.getDeltaMovement();
        }
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void rivals$onTravelTail(Vec3 travelVector, CallbackInfo ci) {
        if (this.rivals_isSliding && this.onGround()) {
            if (this.horizontalCollision) {
                if (this.canEnterPose(Pose.STANDING)) {
                    this.rivals_isSliding = false;
                    this.rivals_slideCooldown = 8;
                }
                return;
            }

            double progress = Math.min(1.0, (double) this.rivals_slideTicks / 30.0);
            double decay = 0.975 - (0.02 * progress);
            Vec3 current = this.getDeltaMovement();

            if (current.y < -0.05) {
                decay = Math.min(decay + 0.018, 0.992);
            }

            this.setDeltaMovement(this.rivals_preTravelVel.x * decay, current.y, this.rivals_preTravelVel.z * decay);
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void rivals$onJumpFromGround(CallbackInfo ci) {
        if (this.rivals_isSliding) {
            Vec3 vel = this.getDeltaMovement();
            double horizSpeed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);
            double jumpBoost = Math.max(horizSpeed * 1.15, 0.45);

            Vec3 look = this.getLookAngle();
            Vec3 dir = new Vec3(look.x, 0, look.z);
            if (dir.lengthSqr() > 1e-4) {
                dir = dir.normalize();
            } else {
                dir = new Vec3(vel.x, 0, vel.z).normalize();
            }

            float jumpPower = this.getJumpPower();
            this.setDeltaMovement(dir.x * jumpBoost, (double) jumpPower * 1.05, dir.z * jumpBoost);
            this.hasImpulse = true;

            this.rivals_isSliding = false;
            this.rivals_slideCooldown = 6;
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void rivals$onTick(CallbackInfo ci) {
        if (this.rivals_slideCooldown > 0) {
            this.rivals_slideCooldown--;
        }

        if (this.rivals_isSliding) {
            this.rivals_slideTicks++;

            Vec3 vel = this.getDeltaMovement();
            double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);

            boolean timeout = this.rivals_slideTicks > 30;
            boolean tooSlow = speed < 0.10 && this.rivals_slideTicks > 5;
            boolean leftGround = !this.onGround() && this.fallDistance > 0.6F;

            if (timeout || tooSlow || leftGround) {
                if (this.canEnterPose(Pose.STANDING)) {
                    this.rivals_isSliding = false;
                    this.rivals_slideCooldown = 8;
                }
            }
        }
    }
}