package com.largesign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LargeSignClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(
				LargeSignBlock.LARGE_SIGN_SCREEN_OPEN_PACKET_ID, 
				new LargeSignScreenOpenHandler());
	}
}