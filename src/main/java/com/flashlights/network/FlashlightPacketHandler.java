package com.flashlights.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;

public class FlashlightPacketHandler {
    public static final Identifier FLASHLIGHT_TOGGLE_PACKET_ID = Identifier.of("flashlights", "flashlight_toggle");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(FLASHLIGHT_TOGGLE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean flashlightState = buf.readBoolean();
            server.execute(() -> {
                // Update the player's flashlight state
                updateFlashlightState(player, flashlightState);
                // Notify other players
                notifyPlayers(player, flashlightState);
            });
        });
    }

    private static void updateFlashlightState(PlayerEntity player, boolean flashlightState) {
        LightManager.setFlashlightState(player, flashlightState);
    }

    private static void notifyPlayers(PlayerEntity player, boolean flashlightState) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(player.getUuid());
        buf.writeBoolean(flashlightState);

        for (ServerPlayerEntity otherPlayer : ((MinecraftServer) player.getServer()).getPlayerManager().getPlayerList()) {
            if (otherPlayer != player) {
                ServerPlayNetworking.send(otherPlayer, FLASHLIGHT_TOGGLE_PACKET_ID, buf);
            }
        }
    }
}