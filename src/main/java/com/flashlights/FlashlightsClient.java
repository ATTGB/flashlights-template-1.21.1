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
        LightManager.init();


    }
}


