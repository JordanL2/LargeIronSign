package com.jordanl2.largeironsign;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class LargeIronSign implements ModInitializer {
    
    public static final String MOD_ID = "largeironsign";
    
    // Block entities
    public static final BlockEntityType<LargeIronSignBlockEntity> LARGE_IRON_SIGN_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            LargeIronSignBlockEntity.ID,
            FabricBlockEntityTypeBuilder.create(
                    LargeIronSignBlockEntity::new,
                    LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK).build());
    
    @Override
    public void onInitialize() {
        // Blocks
        Registry.register(Registries.BLOCK,
                LargeIronSignBlock.ID,
                LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK);
        
        // Items
        Registry.register(Registries.ITEM,
                LargeIronSignBlock.ID,
                LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK_ITEM);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
                content.add(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK_ITEM));
        
        // Network
        ServerPlayNetworking.registerGlobalReceiver(
                LargeIronSignBlock.LARGE_IRON_SIGN_SET_SYMBOL_PACKET_ID,
                new LargeIronSignSetSymbolHandler());
    }
}