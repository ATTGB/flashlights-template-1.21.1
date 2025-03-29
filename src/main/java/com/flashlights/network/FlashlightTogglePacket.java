package com.flashlights.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class FlashlightTogglePacket {
    private final boolean flashlightState;

    public FlashlightTogglePacket(boolean flashlightState) {
        this.flashlightState = flashlightState;
    }

    public void encode(PacketByteBuf buf) {
        buf.writeBoolean(flashlightState);
    }

    public static FlashlightTogglePacket decode(PacketByteBuf buf) {
        return new FlashlightTogglePacket(buf.readBoolean());
    }

    public static void send(boolean flashlightState) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(flashlightState);
        Identifier identifier = Identifier.of("flashlights", "flashlight_toggle");

    }
}