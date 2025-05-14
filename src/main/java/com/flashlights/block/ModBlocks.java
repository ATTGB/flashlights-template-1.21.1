package com.flashlights.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block INTERIOR_MAPPED_BLOCK = new Block(FabricBlockSettings.of()
            .strength(1.0f)
            .nonOpaque());

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, Identifier.of("flashlights", "interior_mapped_block"), INTERIOR_MAPPED_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("flashlights", "interior_mapped_block"),
                new BlockItem(INTERIOR_MAPPED_BLOCK, new Item.Settings()));
    }

}
