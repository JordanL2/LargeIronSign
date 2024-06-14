package com.jordanl2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LargeSign implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("largesign");

	//public static final Block LARGE_SIGN = new LargeSignBlock(FabricBlockSettings.create().strength(4.0f));

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, 
				new Identifier("jordanl2", "large_sign"), 
				LargeSignBlock.LARGE_SIGN_BLOCK);
		Registry.register(Registries.ITEM, 
				new Identifier("jordanl2", "large_sign"), 
				new BlockItem(LargeSignBlock.LARGE_SIGN_BLOCK, new FabricItemSettings()));
	}
}