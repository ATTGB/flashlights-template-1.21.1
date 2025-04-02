package com.flashlights.keybinds;

import com.flashlights.light.LightManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindsManager {
    public static final KeyBinding toggleFlashlight = new KeyBinding(
            "key.flashlight.toggle_flashlight",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.flashlight"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(toggleFlashlight);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleFlashlight.wasPressed()) {
                LightManager.toggleFlashlight();
            }
        });
    }
}