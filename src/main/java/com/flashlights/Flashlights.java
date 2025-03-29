package com.flashlights;

import com.flashlights.network.FlashlightTogglePacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Flashlights implements ModInitializer {
	public static final String MOD_ID = "flashlights";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitialize() {
		FlashlightTogglePacket.register();

	}
}