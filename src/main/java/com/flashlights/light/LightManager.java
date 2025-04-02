package com.flashlights.light;

import com.flashlights.Item.items.FlashLightItem;
import com.flashlights.component.ModComponents;
import com.flashlights.utils.ColorUtils;
import com.flashlights.utils.OrientationUtils;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.AreaLight;
import foundry.veil.platform.VeilEventPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;

import java.util.HashMap;

public class LightManager {
    public static final HashMap<Integer, AreaLight> ITEM_LIGHT = new HashMap<>();
    public static final int DEFAULT_LIGHT_COLOR = 0xFFf0d3d3;
    public static final int WHITE_LIGHT_COLOR = 0xFFf0f0f0;

    private static boolean isFlashlightOn = false; // Track flashlight state

    public static void init() {
        VeilEventPlatform.INSTANCE.onVeilRenderLevelStage((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
            for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
                if (entity instanceof PlayerEntity player) {
                    tickPlayerFlashlight(player, deltaTracker);
                }
            }
        });
    }

    public static void toggleFlashlight() {
        isFlashlightOn = !isFlashlightOn; // Toggle flashlight state
    }

    private static void tickPlayerFlashlight(PlayerEntity player, RenderTickCounter deltaTracker) {
        if (isFlashlightOn && (player.getMainHandStack().getItem() instanceof FlashLightItem || player.getOffHandStack().getItem() instanceof FlashLightItem)) {
            Hand hand = (player.getOffHandStack().getItem() instanceof FlashLightItem) ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack stack = player.getStackInHand(hand);
            AreaLight light = new AreaLight();
            Vec3d renderPosition = player.getLerpedPos(deltaTracker.getTickDelta(false));

            if (ITEM_LIGHT.containsKey(player.getId())) {
                light = ITEM_LIGHT.get(player.getId());
            } else {
                light.setBrightness(2.0f);

                int color = DEFAULT_LIGHT_COLOR;
                if (stack.get(ModComponents.COLOR) != null) {
                    color = ColorUtils.blendColors(DEFAULT_LIGHT_COLOR, stack.get(ModComponents.COLOR));
                }

                light.setColor(color);
                light.setBrightness(0.0f);
                light.setSize(0.0f, 0.0f);
                light.setDistance(20.0f);
                light.setAngle(0.6f);

                ITEM_LIGHT.put(player.getId(), light);
                VeilRenderSystem.renderer().getLightRenderer().addLight(light);
            }
            light.setPosition(renderPosition.x, renderPosition.y + 1.5, renderPosition.z);
            light.setOrientation(OrientationUtils.getOrientation(player.getRotationVector()));

            int color = DEFAULT_LIGHT_COLOR;
            if (stack.get(ModComponents.COLOR) != null) {
                color = ColorUtils.blendColors(DEFAULT_LIGHT_COLOR, stack.get(ModComponents.COLOR));
            }

            light.setColor(color);

            if (stack.get(ModComponents.IS_ON) != null) {
                if (!stack.get(ModComponents.IS_ON)) {
                    light.setBrightness(2.0f);
                    light.setPosition(player.getX(), player.getY() + 1.6f, player.getZ());
                } else {
                    light.setBrightness(0.0f);
                }
            } else {
                light.setBrightness(0.0f);
            }
        } else if (ITEM_LIGHT.containsKey(player.getId())) {
            AreaLight light = ITEM_LIGHT.get(player.getId());
            VeilRenderSystem.renderer().getLightRenderer().removeLight(light);
            ITEM_LIGHT.remove(player.getId());
        }
    }
}