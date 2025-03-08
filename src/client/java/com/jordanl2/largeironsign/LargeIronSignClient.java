package com.jordanl2.largeironsign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LargeIronSignClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Custom model loader
        ModelLoadingPlugin.register(new LargeIronSignModelLoadingPlugin());
        
        // Network
        ClientPlayNetworking.registerGlobalReceiver(
                LargeIronSignScreenOpenPayload.PACKET_ID,
                new LargeIronSignScreenOpenHandler());
    }
    
}