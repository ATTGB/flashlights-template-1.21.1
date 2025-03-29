package com.flashlights.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FlashlightTogglePacket {
    public static final Identifier ID =  Identifier.of("flashlights", "toggle_flashlight");

    public static void send(boolean isFlashlightOn) {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(isFlashlightOn);
            // Send packet to server (this should be called client-side)
            ServerPlayNetworking.send(MinecraftClient.getInstance().getNetworkHandler(), ID, buf);
        }
    }

    public static void register() {
        // Register the packet handler (this should be called server-side)
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            boolean isFlashlightOn = buf.readBoolean();
            server.execute(() -> {
                // Handle the packet on the server side
                // Update the flashlight state for the player and broadcast it to all clients
                for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
                    PacketByteBuf responseBuf = PacketByteBufs.create();
                    responseBuf.writeBoolean(isFlashlightOn);
                    ServerPlayNetworking.send(target, ID, responseBuf);
                }
            });
        });
    }
}