package com.jordanl2.largeironsign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class LargeIronSignClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Custom model loader
        ModelLoadingPlugin.register(new LargeIronSignModelLoadingPlugin());
        
        // Network
        PayloadTypeRegistry.playS2C().register(LargeIronSignScreenOpenPayload.PACKET_ID, LargeIronSignScreenOpenPayload.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(LargeIronSignSetSymbolPayload.PACKET_ID, LargeIronSignSetSymbolPayload.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(
                LargeIronSignScreenOpenPayload.PACKET_ID,
                new LargeIronSignScreenOpenHandler());
    }
    
}