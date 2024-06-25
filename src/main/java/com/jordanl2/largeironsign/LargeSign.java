package com.jordanl2.largeironsign;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LargeSign implements ModInitializer {
	
	public static final String MOD_ID = "largesign";
	
    public static final BlockEntityType<LargeSignBlockEntity> LARGE_SIGN_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            LargeSignBlockEntity.ID,
            FabricBlockEntityTypeBuilder.create(
            		LargeSignBlockEntity::new,
            		LargeSignBlock.LARGE_SIGN_BLOCK).build());
	
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