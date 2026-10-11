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

    @Unique private boolean slide_isSliding = false;
    @Unique private int slide_slideTicks = 0;
    @Unique private int slide_slideCooldown = 0;
    @Unique private Vec3 slide_preTravelVel = Vec3.ZERO;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean slide$isSliding() {
        return this.slide_isSliding;
    }

    @Override
    public void slide$setSliding(boolean sliding) {
        boolean prev = this.slide_isSliding;
        this.slide_isSliding = sliding;
        if (!sliding) {
            this.slide_slideTicks = 0;
        }
        if (prev != sliding) {
            this.refreshDimensions();
        }
    }

    @Override
    public int slide$getSlideTicks() {
        return this.slide_slideTicks;
    }

    @Override
    public void slide$setSlideTicks(int ticks) {
        this.slide_slideTicks = ticks;
    }

    @Override
    public int slide$getSlideCooldown() {
        return this.slide_slideCooldown;
    }

    @Override
    public void slide$setSlideCooldown(int cooldown) {
        this.slide_slideCooldown = cooldown;
    }

    @Override
    public boolean slide$canStand() {
        return this.canEnterPose(Pose.STANDING);
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    public void slide$onGetDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (this.slide_isSliding) {
            // Hitbox height doubled (0.6 -> 1.2) for PvP/combat fairness so head & upper body can be hit
            cir.setReturnValue(EntityDimensions.scalable(0.6F, 1.2F));
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
    protected void slide$onGetStandingEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        if (this.slide_isSliding) {
            cir.setReturnValue(0.85F);
        }
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    protected void slide$onUpdatePlayerPose(CallbackInfo ci) {
        if (this.slide_isSliding) {
            this.setPose(Pose.SWIMMING);
            ci.cancel();
        }
    }

    @Inject(method = "maybeBackOffFromEdge", at = @At("HEAD"), cancellable = true)
    protected void slide$onMaybeBackOffFromEdge(Vec3 movement, MoverType moverType, CallbackInfoReturnable<Vec3> cir) {
        if (this.slide_isSliding) {
            // Disable vanilla sneak edge-protection: allow sliding smoothly over 1-block holes and off cliffs
            cir.setReturnValue(movement);
        }
    }

    @Inject(method = "isStayingOnGroundSurface", at = @At("HEAD"), cancellable = true)
    protected void slide$onIsStayingOnGroundSurface(CallbackInfoReturnable<Boolean> cir) {
        if (this.slide_isSliding) {
            // Do not treat slide as edge-holding sneak
            cir.setReturnValue(false);
        }
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        if (this.slide_isSliding) {
            Vec3 currentVel = this.getDeltaMovement();
            double incomingHorizSqr = x * x + z * z;
            if (incomingHorizSqr < 1.0E-4) {
                // Non-knockback damage (fire, fall, cactus, poison, etc.):
                // Server sends (0, y, 0) because ServerPlayer deltaMovement is 0 on server.
                // Preserve the client's current horizontal sliding velocity so slide is not canceled.
                Vec3 preserved = new Vec3(currentVel.x, currentVel.y, currentVel.z);
                this.setDeltaMovement(preserved);
                this.slide_preTravelVel = preserved;
            } else {
                // Knockback while sliding: keep sliding active and blend slide momentum with knockback
                double newX = currentVel.x * 0.55 + x;
                double newZ = currentVel.z * 0.55 + z;
                double combinedHorizSqr = newX * newX + newZ * newZ;
                if (combinedHorizSqr < incomingHorizSqr) {
                    if (combinedHorizSqr < 1.0E-4) {
                        newX = x;
                        newZ = z;
                    } else {
                        double scale = Math.sqrt(incomingHorizSqr / combinedHorizSqr);
                        newX *= scale;
                        newZ *= scale;
                    }
                }
                double newY = Math.max(currentVel.y, Math.min(y, 0.42));
                Vec3 knockbackSlideVel = new Vec3(newX, newY, newZ);
                this.setDeltaMovement(knockbackSlideVel);
                this.slide_preTravelVel = knockbackSlideVel;
                if (this.slide_slideTicks > 18) {
                    this.slide_slideTicks = 18;
                }
            }
            return;
        }
        super.lerpMotion(x, y, z);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        Vec3 before = this.getDeltaMovement();
        super.knockback(strength, x, z);
        if (this.slide_isSliding) {
            Vec3 after = this.getDeltaMovement();
            Vec3 blended = new Vec3(before.x * 0.35 + after.x, after.y, before.z * 0.35 + after.z);
            this.setDeltaMovement(blended);
            this.slide_preTravelVel = blended;
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void slide$onTravelHead(Vec3 travelVector, CallbackInfo ci) {
        if (this.slide_isSliding) {
            this.slide_preTravelVel = this.getDeltaMovement();
        }
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void slide$onTravelTail(Vec3 travelVector, CallbackInfo ci) {
        if (this.slide_isSliding) {
            // Smooth sliding decay (decay curve starting at 0.975 and gradually settling)
            double progress = Math.min(1.0, (double) this.slide_slideTicks / 30.0);
            double decay = 0.975 - (0.02 * progress);
            Vec3 current = this.getDeltaMovement();

            // Downhill gravity boost: sliding down slopes preserves or boosts momentum
            if (current.y < -0.05) {
                decay = Math.min(decay + 0.018, 0.992);
            }

            // Preserve horizontal sliding momentum on unobstructed axes
            double nextX = (this.horizontalCollision && Math.abs(current.x) < 1.0E-4) ? 0.0 : this.slide_preTravelVel.x * decay;
            double nextZ = (this.horizontalCollision && Math.abs(current.z) < 1.0E-4) ? 0.0 : this.slide_preTravelVel.z * decay;
            this.setDeltaMovement(nextX, current.y, nextZ);
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void slide$onJumpFromGround(CallbackInfo ci) {
        if (this.slide_isSliding) {
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

            this.slide_isSliding = false;
            this.slide_slideCooldown = 6;
            this.refreshDimensions();
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void slide$onTick(CallbackInfo ci) {
        if (this.slide_slideCooldown > 0) {
            this.slide_slideCooldown--;
        }

        if (this.slide_isSliding) {
            this.slide_slideTicks++;

            if (this.level().isClientSide()) {
                Vec3 vel = this.getDeltaMovement();
                double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);

                boolean timeout = this.slide_slideTicks > 30;
                // Do not cancel slide due to low speed while taking damage / knockback (hurtTime > 0)
                boolean tooSlow = speed < 0.08 && this.slide_slideTicks > 5 && this.hurtTime == 0;
                // Only stop if fallen far (greater than 1.8 blocks), allowing smooth glide over 1-block holes
                boolean fellFar = !this.onGround() && this.fallDistance > 1.8F;

                if (timeout || tooSlow || fellFar) {
                    if (this.canEnterPose(Pose.STANDING)) {
                        this.slide_isSliding = false;
                        this.slide_slideCooldown = 8;
                        this.refreshDimensions();
                    }
                }
            } else {
                // Server-side safety fallback timeout; normal slide end is synced via packet from client
                if (this.slide_slideTicks > 60 && this.canEnterPose(Pose.STANDING)) {
                    this.slide_isSliding = false;
                    this.slide_slideCooldown = 8;
                    this.refreshDimensions();
                }
            }
        }
    }
}