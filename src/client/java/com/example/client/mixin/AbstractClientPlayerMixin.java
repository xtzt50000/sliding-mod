package com.example.client.mixin;

import com.example.util.SlideHolder;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void slide$onGetFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        if (player instanceof SlideHolder holder && holder.slide$isSliding()) {
            float base = cir.getReturnValueF();
            double speed = player.getDeltaMovement().horizontalDistance();
            float factor = (float) Math.min(1.0, speed / 0.5);
            cir.setReturnValue(base * (1.0F + 0.12F * factor));
        }
    }
}
