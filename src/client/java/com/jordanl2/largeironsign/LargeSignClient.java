package com.jordanl2.largeironsign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LargeSignClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(new LargeSignModelLoadingPlugin());
		
		ClientPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_SCREEN_OPEN_PACKET_ID, 
				new LargeSignScreenOpenHandler());
		ClientPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_REFRESH_MODEL_PACKET_ID, 
				new LargeSignRefreshModelHandler());
	}
	
}