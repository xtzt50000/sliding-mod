package com.example.client.mixin;

import com.example.client.SlideClientController;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobHurt", at = @At("TAIL"))
    private void slide$onBobHurt(PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        SlideClientController.applyCameraTilt(poseStack, tickDelta);
    }
}
