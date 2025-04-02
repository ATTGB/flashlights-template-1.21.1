package com.flashlights.Item;

import com.flashlights.Flashlights;
import com.flashlights.Item.items.FlashLightItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {

    public static FlashLightItem FLASH_LIGHT = registerItem("flashlight", new FlashLightItem(new Item.Settings().maxCount(1)));

    private static <T extends Item> T registerItem(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(Flashlights.MOD_ID), item);
    }

    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries) {
        entries.add(FLASH_LIGHT);
    }

    public static void RegisterModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsItemGroup);

    }





}
