package com.example.client;

import com.example.client.network.SlideClientNetworking;
import com.example.util.SlideHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlideClientController {
    private static float currentRoll = 0.0F;
    private static float prevRoll = 0.0F;
    private static float currentPitch = 0.0F;
    private static float prevPitch = 0.0F;

    public static void clientTick(Minecraft mc) {
        prevRoll = currentRoll;
        prevPitch = currentPitch;

        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!(player instanceof SlideHolder holder)) return;

        boolean isSliding = holder.rivals$isSliding();

        if (isSliding) {
            // Target camera tilt: -3.5 degrees roll for an action lean into the slide
            float targetRoll = -3.5F;
            float targetPitch = 1.0F;

            currentRoll = Mth.lerp(0.3F, currentRoll, targetRoll);
            currentPitch = Mth.lerp(0.3F, currentPitch, targetPitch);

            // Release sneak to end slide early if headroom permits
            boolean sneakHeld = player.input.shiftKeyDown;
            if (!sneakHeld && holder.rivals$canStand()) {
                holder.rivals$setSliding(false);
                holder.rivals$setSlideCooldown(8);
                SlideClientNetworking.sendSlidePacket(false);
            }

            // Spawn dust particles while sliding on the ground
            if (player.onGround() && mc.level != null) {
                spawnSlideParticles(mc, player);
            }
        } else {
            // Smoothly return camera back to level
            currentRoll = Mth.lerp(0.25F, currentRoll, 0.0F);
            currentPitch = Mth.lerp(0.25F, currentPitch, 0.0F);
            if (Math.abs(currentRoll) < 0.01F) currentRoll = 0.0F;
            if (Math.abs(currentPitch) < 0.01F) currentPitch = 0.0F;

            // Trigger slide: Sprinting + Crouch + OnGround + Cooldown ready
            if (player.isSprinting() && player.input.shiftKeyDown && player.onGround() && holder.rivals$getSlideCooldown() <= 0) {
                Vec3 vel = player.getDeltaMovement();
                double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);
                if (speed > 0.08) {
                    startSlide(player, holder);
                }
            }
        }
    }

    private static void startSlide(LocalPlayer player, SlideHolder holder) {
        holder.rivals$setSliding(true);
        holder.rivals$setSlideTicks(0);

        Vec3 look = player.getLookAngle();
        Vec3 lookHoriz = new Vec3(look.x, 0, look.z);
        if (lookHoriz.lengthSqr() > 1e-4) {
            lookHoriz = lookHoriz.normalize();
        }

        Vec3 vel = player.getDeltaMovement();
        Vec3 moveHoriz = new Vec3(vel.x, 0, vel.z);
        if (moveHoriz.lengthSqr() > 1e-4) {
            moveHoriz = moveHoriz.normalize();
        } else {
            moveHoriz = lookHoriz;
        }

        // Blend 75% look direction + 25% current movement direction
        Vec3 slideDir = lookHoriz.scale(0.75).add(moveHoriz.scale(0.25)).normalize();

        double currentSpeed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);
        double boostSpeed = Math.max(currentSpeed, 0.28) + 0.38;
        boostSpeed = Math.min(boostSpeed, 0.72);

        player.setDeltaMovement(slideDir.x * boostSpeed, vel.y, slideDir.z * boostSpeed);
        player.hasImpulse = true;

        SlideClientNetworking.sendSlidePacket(true);

        // Slide start sound effects
        if (player.clientLevel != null) {
            player.clientLevel.playLocalSound(
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                0.75F, 1.4F, false
            );

            BlockPos posBelow = BlockPos.containing(player.getX(), player.getY() - 0.2, player.getZ());
            BlockState state = player.clientLevel.getBlockState(posBelow);
            if (!state.isAir()) {
                player.clientLevel.playLocalSound(
                    player.getX(), player.getY(), player.getZ(),
                    state.getSoundType().getStepSound(),
                    SoundSource.PLAYERS,
                    0.7F, 0.85F, false
                );
            }
        }
    }

    private static void spawnSlideParticles(Minecraft mc, LocalPlayer player) {
        if (mc.level == null) return;
        BlockPos posBelow = BlockPos.containing(player.getX(), player.getY() - 0.2, player.getZ());
        BlockState state = mc.level.getBlockState(posBelow);
        if (!state.isAir()) {
            Vec3 vel = player.getDeltaMovement();
            for (int i = 0; i < 2; i++) {
                double ox = (player.getRandom().nextDouble() - 0.5) * 0.4;
                double oz = (player.getRandom().nextDouble() - 0.5) * 0.4;
                mc.level.addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    player.getX() + ox,
                    player.getY() + 0.05,
                    player.getZ() + oz,
                    -vel.x * 0.25,
                    0.08,
                    -vel.z * 0.25
                );
            }
        }
    }

    public static void applyCameraTilt(PoseStack poseStack, float tickDelta) {
        float roll = Mth.lerp(tickDelta, prevRoll, currentRoll);
        float pitch = Mth.lerp(tickDelta, prevPitch, currentPitch);

        if (Math.abs(roll) > 0.001F) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
        }
        if (Math.abs(pitch) > 0.001F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        }
    }
}
