package com.flashlights;

import dev.foundry.veil.api.client.rendering.VeilRenderEvent;
import dev.foundry.veil.api.client.rendering.VeilRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.math.ColorHelper;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

public class FlashlightsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VeilRenderer.EVENT.register(this::renderHalo);
    }

    private void renderHalo(VeilRenderEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        if (player == null || player.isSpectator()) return;

        MatrixStack matrices = event.getMatrices();
        Vec3d cameraPos = event.getCamera().getPos();

        Vec3d headPos = player.getPos().add(0, 2.4, 0);  // Slightly above head
        double x = headPos.x - cameraPos.x;
        double y = headPos.y - cameraPos.y;
        double z = headPos.z - cameraPos.z;

        int color = ColorHelper.Argb.getArgb(180, 255, 215, 0);  // glowing gold

        event.getDrawContext().drawSphere(matrices, x, y, z, 0.3f, color, true);
    }
}