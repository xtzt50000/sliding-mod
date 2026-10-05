package com.example.network;

import com.example.ExampleMod;
import com.example.util.SlideHolder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SlideNetworking {
    public static final ResourceLocation C2S_SLIDE_PACKET = new ResourceLocation(ExampleMod.MOD_ID, "slide_c2s");
    public static final ResourceLocation S2C_SLIDE_PACKET = new ResourceLocation(ExampleMod.MOD_ID, "slide_s2c");

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_SLIDE_PACKET, (server, player, handler, buf, responseSender) -> {
            boolean sliding = buf.readBoolean();
            server.execute(() -> {
                if (player instanceof SlideHolder holder) {
                    holder.rivals$setSliding(sliding);
                    if (!sliding) {
                        holder.rivals$setSlideTicks(0);
                    }
                }

                FriendlyByteBuf outBuf = PacketByteBufs.create();
                outBuf.writeInt(player.getId());
                outBuf.writeBoolean(sliding);

                for (ServerPlayer tracker : PlayerLookup.tracking(player)) {
                    ServerPlayNetworking.send(tracker, S2C_SLIDE_PACKET, outBuf);
                }
            });
        });
    }
}
