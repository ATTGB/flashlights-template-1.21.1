package com.flashlights.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FlashlightTogglePacket {
    public static final Identifier ID = Identifier.of("flashlights", "toggle_flashlight");

    public static void send(boolean isFlashlightOn) {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            // Create the buffer and write the boolean value
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(isFlashlightOn);

            // Send the packet to the server using CustomPayloadC2SPacket
            CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(ID, buf);
            ClientPlayNetworking.send(packet);
        }
    }

    public static void register() {
        // Register the packet handler server-side
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            // Read the boolean value from the buffer
            boolean isFlashlightOn = buf.readBoolean();
            server.execute(() -> {
                if (player.getServer() != null) {
                    // Send the updated flashlight state to all other players
                    for (ServerPlayerEntity target : player.getServer().getPlayerManager().getPlayerList()) {
                        PacketByteBuf responseBuf = PacketByteBufs.create();
                        responseBuf.writeBoolean(isFlashlightOn);
                        ServerPlayNetworking.send(target, ID, responseBuf);
                    }
                }
            });
        });
    }
}
