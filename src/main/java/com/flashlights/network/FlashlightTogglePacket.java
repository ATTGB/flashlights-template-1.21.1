package com.flashlights.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FlashlightTogglePacket {
    public static final Identifier ID = new Identifier("flashlights", "toggle_flashlight");

    public static void send(boolean isFlashlightOn) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(isFlashlightOn);
        // Send packet to server (this should be called client-side)
        ServerPlayNetworking.send(MinecraftClient.getInstance().getNetworkHandler(), ID, buf);
    }

    public static void register() {
        // Register the packet handler (this should be called server-side)
        ServerPlayNetworking.registerGlobalReceiver(ID, new PlayChannelHandler() {
            @Override
            public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworking.PlayChannelHandler handler, PacketByteBuf buf, PacketSender responseSender) {
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
            }
        });
    }
}