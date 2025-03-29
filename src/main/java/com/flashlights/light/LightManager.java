package com.flashlights.light;

import com.flashlights.network.LightTogglePacket;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.AreaLight;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LightManager {
    private static final Map<UUID, AreaLight> flashlights = new HashMap<>();
    private static final float BRIGHTNESS = 2.0f;
    private static final float DISTANCE = 20.0f;
    private static final float ANGLE = 0.6f;
    private static final Map<UUID, Boolean> debugEnabledMap = new HashMap<>();

    public static void toggleDebugEnabled(ServerPlayerEntity player) {
        UUID playerUuid = player.getUuid();
        boolean enabled = !debugEnabledMap.getOrDefault(playerUuid, true);
        debugEnabledMap.put(playerUuid, enabled);
        LightTogglePacket.send(player, enabled);
    }

    public static void toggleDebugEnabled(ClientPlayerEntity player) {
        UUID playerUuid = player.getUuid();
        boolean enabled = !debugEnabledMap.getOrDefault(playerUuid, true);
        debugEnabledMap.put(playerUuid, enabled);
        // Here you can add a mechanism to inform the server if needed
    }

    public static void setDebugEnabled(UUID playerUuid, boolean enabled) {
        debugEnabledMap.put(playerUuid, enabled);
    }

    public static Map<UUID, Boolean> getDebugEnabledMap() {
        return debugEnabledMap;
    }

    public static void updateFlashlights() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null) {
            for (PlayerEntity player : client.world.getPlayers()) {
                if (player == null) continue;

                UUID playerUuid = player.getUuid();
                Vec3d camPosVec = player.getPos().add(player.getRotationVec(1.0f).multiply(0.3).add(0, 1.75, 0));

                if (debugEnabledMap.getOrDefault(playerUuid, true) && playerHasRequiredItem(player)) {
                    AreaLight flashLight = flashlights.computeIfAbsent(playerUuid, uuid -> {
                        AreaLight newLight = new AreaLight();
                        newLight.setBrightness(BRIGHTNESS);
                        newLight.setColor(1.0f, 0.95f, 0.85f);
                        newLight.setDistance(DISTANCE);
                        newLight.setAngle(ANGLE);
                        newLight.setSize(0.0F, 0.0F);
                        newLight.setPosition(camPosVec.x, camPosVec.y, camPosVec.z);
                        VeilRenderSystem.renderer().getLightRenderer().addLight(newLight);
                        return newLight;
                    });

                    flashLight.setPosition(flashLight.getPosition().lerp(
                            new Vector3d(camPosVec.x, camPosVec.y, camPosVec.z), 0.5F, new Vector3d()));

                    Quaternionf goal = new Quaternionf().rotateXYZ(
                            (float) -Math.toRadians(player.getPitch()),
                            (float) Math.toRadians(player.getYaw()),
                            0.0f
                    );
                    Quaternionf currentOrientation = new Quaternionf(flashLight.getOrientation());
                    currentOrientation.slerp(goal, 0.15f);
                    flashLight.setOrientation(currentOrientation);
                } else {
                    cleanUpFlashlight(playerUuid);
                    flashlights.remove(playerUuid);
                }
            }
        }
    }

    private static boolean playerHasRequiredItem(PlayerEntity player) {
        for (ItemStack itemStack : player.getInventory().main) {
            if (itemStack.getItem() == Items.TORCH) {
                return true;
            }
        }
        return false;
    }

    public static void removeInactiveFlashlights() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null) {
            flashlights.entrySet().removeIf(entry -> {
                UUID uuid = entry.getKey();
                PlayerEntity player = client.world.getPlayerByUuid(uuid);

                if (player == null) {
                    VeilRenderSystem.renderer().getLightRenderer().removeLight(entry.getValue());
                    return true;
                }

                return false;
            });
        }
    }

    private static void cleanUpFlashlight(UUID playerUuid) {
        AreaLight light = flashlights.get(playerUuid);
        if (light != null) {
            VeilRenderSystem.renderer().getLightRenderer().removeLight(light);
        }
    }

    public static void onPlayerDisconnected(UUID playerUuid) {
        cleanUpFlashlight(playerUuid);
    }

    public static void onPlayerRejoined(UUID playerUuid) {
        cleanUpFlashlight(playerUuid);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        updateFlashlights();
    }
}