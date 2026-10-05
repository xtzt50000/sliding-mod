package com.example.mixin;

import com.example.util.SlideHolder;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
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
        boolean prev = this.rivals_isSliding;
        this.rivals_isSliding = sliding;
        if (!sliding) {
            this.rivals_slideTicks = 0;
        }
        if (prev != sliding) {
            this.refreshDimensions();
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

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    public void rivals$onGetDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (this.rivals_isSliding) {
            // Hitbox height doubled (0.6 -> 1.2) for PvP/combat fairness so head & upper body can be hit
            cir.setReturnValue(EntityDimensions.scalable(0.6F, 1.2F));
        }
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

    @Inject(method = "maybeBackOffFromEdge", at = @At("HEAD"), cancellable = true)
    protected void rivals$onMaybeBackOffFromEdge(Vec3 movement, MoverType moverType, CallbackInfoReturnable<Vec3> cir) {
        if (this.rivals_isSliding) {
            // Disable vanilla sneak edge-protection: allow sliding smoothly over 1-block holes and off cliffs
            cir.setReturnValue(movement);
        }
    }

    @Inject(method = "isStayingOnGroundSurface", at = @At("HEAD"), cancellable = true)
    protected void rivals$onIsStayingOnGroundSurface(CallbackInfoReturnable<Boolean> cir) {
        if (this.rivals_isSliding) {
            // Do not treat slide as edge-holding sneak
            cir.setReturnValue(false);
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
        if (this.rivals_isSliding) {
            if (this.horizontalCollision) {
                if (this.canEnterPose(Pose.STANDING)) {
                    this.rivals_isSliding = false;
                    this.rivals_slideCooldown = 8;
                    this.refreshDimensions();
                }
                return;
            }

            // Smooth sliding decay (decay curve starting at 0.975 and gradually settling)
            double progress = Math.min(1.0, (double) this.rivals_slideTicks / 30.0);
            double decay = 0.975 - (0.02 * progress);
            Vec3 current = this.getDeltaMovement();

            // Downhill gravity boost: sliding down slopes preserves or boosts momentum
            if (current.y < -0.05) {
                decay = Math.min(decay + 0.018, 0.992);
            }

            // Preserve horizontal sliding momentum whether on ground or crossing a small gap/falling
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
            this.refreshDimensions();
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
            // Only stop if fallen far (greater than 1.8 blocks), allowing smooth glide over 1-block holes
            boolean fellFar = !this.onGround() && this.fallDistance > 1.8F;

            if (timeout || tooSlow || fellFar) {
                if (this.canEnterPose(Pose.STANDING)) {
                    this.rivals_isSliding = false;
                    this.rivals_slideCooldown = 8;
                    this.refreshDimensions();
                }
            }
        }
    }
}