package com.largesign;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LargeSign implements ModInitializer {
	
	public static final String MOD_ID = "largesign";
	
    public static final Logger LOGGER = LoggerFactory.getLogger("largesign");

	@Override
	public void onInitialize() {
		// Blocks
		Registry.register(Registries.BLOCK, 
				LargeSignBlock.ID, 
				LargeSignBlock.LARGE_SIGN_BLOCK);
		
		// Items
		Registry.register(Registries.ITEM, 
				LargeSignBlock.ID, 
				LargeSignBlock.LARGE_SIGN_BLOCK_ITEM);
	    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
	    	content.add(LargeSignBlock.LARGE_SIGN_BLOCK_ITEM);
	    });
	    
	    // Network
		ServerPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_SET_SYMBOL_PACKET_ID,
				new LargeSignSetSymbolHandler());
	}
}