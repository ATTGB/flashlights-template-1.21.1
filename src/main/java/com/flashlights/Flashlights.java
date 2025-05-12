package com.flashlights;

import net.fabricmc.api.ModInitializer;

public class Flashlights implements ModInitializer {
    @Override
    public void onInitialize() {
        VeilRenderSystem.registerRenderTask(RenderPhase.LAST, (context, buffer) -> {
        // Draw a red rectangle at (50, 50) with size (100, 100)
        context.fill(buffer, 50, 50, 150, 150, 0xFFFF0000);
    });
}
}