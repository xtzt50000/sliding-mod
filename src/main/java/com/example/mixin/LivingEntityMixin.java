package com.example.mixin;

import com.example.util.SlideHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getSwimAmount", at = @At("HEAD"), cancellable = true)
    private void rivals$onGetSwimAmount(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this instanceof SlideHolder holder && holder.rivals$isSliding()) {
            cir.setReturnValue(0.0F);
        }
    }
}
