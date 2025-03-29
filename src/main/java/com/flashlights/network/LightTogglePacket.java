package com.flashlights.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record LightTogglePacket(UUID playerUuid, boolean enable) {
    public static final Identifier ID = Identifier.of("flashlights", "light_toggle");

    public static void send(ServerPlayerEntity player, boolean enable) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(player.getUuid());
        buffer.writeBoolean(enable);
        ServerPlayNetworking.send(player, ID, buffer);
    }

    public static LightTogglePacket decode(PacketByteBuf buffer) {
        return new LightTogglePacket(buffer.readUuid(), buffer.readBoolean());
    }

    public void encode(PacketByteBuf buffer) {
        buffer.writeUuid(playerUuid);
        buffer.writeBoolean(enable);
    }
}