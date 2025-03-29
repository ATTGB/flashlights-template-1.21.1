package com.flashlights.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record LightTogglePacket(UUID playerUuid, boolean enable) {
    public static final Identifier ID = Identifier.of("flashlights", "light_toggle");

    public static void sendToServer(boolean enable) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeBoolean(enable);
        ClientPlayNetworking.send(ID, buffer);
    }

    public static void sendToClient(ServerPlayerEntity player, UUID playerUuid, boolean enable) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(playerUuid);
        buffer.writeBoolean(enable);
        ServerPlayNetworking.send(player, ID, buffer);
    }

    public static LightTogglePacket decode(Object buffer) {
        return new LightTogglePacket(buffer.readUuid(), buffer.readBoolean());
    }

    public void encode(PacketByteBuf buffer) {
        buffer.writeUuid(playerUuid);
        buffer.writeBoolean(enable);
    }
}