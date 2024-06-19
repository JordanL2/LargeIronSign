package com.jordanl2;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LargeSign implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("largesign");

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, 
				LargeSignBlock.ID, 
				LargeSignBlock.LARGE_SIGN_BLOCK);
		Registry.register(Registries.ITEM, 
				LargeSignBlock.ID, 
				LargeSignBlock.LARGE_SIGN_BLOCK_ITEM);
	}
}