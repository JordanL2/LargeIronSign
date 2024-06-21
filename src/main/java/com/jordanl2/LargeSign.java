package com.jordanl2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroups;
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
	    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
	    	content.add(LargeSignBlock.LARGE_SIGN_BLOCK_ITEM);
	    });
		ServerPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_SET_SYMBOL_PACKET_ID,
				new LargeSignSetSymbolHandler());
	}
}