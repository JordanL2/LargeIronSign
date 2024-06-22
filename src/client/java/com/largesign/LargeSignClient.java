package com.largesign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LargeSignClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		//System.out.println("JORDAN REGISTERING MODEL");
		ModelLoadingPlugin.register(new LargeSignModelLoadingPlugin());
		
		ClientPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_SCREEN_OPEN_PACKET_ID, 
				new LargeSignScreenOpenHandler());
	}
	
}