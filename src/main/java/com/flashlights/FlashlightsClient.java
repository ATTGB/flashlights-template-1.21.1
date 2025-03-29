package com.flashlights;

import com.flashlights.keybinds.KeybindsManager;
import com.flashlights.light.LightManager;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.fabric.event.FabricVeilRenderLevelStageEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;

public class FlashlightsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeybindsManager.register();
        LightManager.initialize();

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            UUID playerUuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
            if (playerUuid == null) return;
            LightManager.handleToggleFlashlight(playerUuid, false);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, client, isFirstJoin) -> {
            UUID playerUuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
            if (playerUuid != null) {
                LightManager.handleToggleFlashlight(playerUuid, LightManager.isFlashlightEnabled(playerUuid));
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            KeybindsManager.handleFlashlightToggle();
            LightManager.updateFlashlights();
        });

        FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
            if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
                LightManager.updateFlashlights();
            }
        });
    }
}