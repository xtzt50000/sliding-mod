package com.example.client.network;

import com.example.network.SlideNetworking;
import com.example.util.SlideHolder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class SlideClientNetworking {
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(SlideNetworking.S2C_SLIDE_PACKET, (client, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            boolean sliding = buf.readBoolean();

            client.execute(() -> {
                if (client.level != null) {
                    Entity entity = client.level.getEntity(entityId);
                    if (entity instanceof SlideHolder holder) {
                        holder.slide$setSliding(sliding);
                        if (!sliding) {
                            holder.slide$setSlideTicks(0);
                        }
                    }
                }
            });
        });
    }

    public static void sendSlidePacket(boolean sliding) {
        if (Minecraft.getInstance().getConnection() != null) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(sliding);
            ClientPlayNetworking.send(SlideNetworking.C2S_SLIDE_PACKET, buf);
        }
    }
}
