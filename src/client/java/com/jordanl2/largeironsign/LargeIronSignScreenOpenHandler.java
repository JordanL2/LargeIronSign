package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class LargeIronSignScreenOpenHandler implements ClientPlayNetworking.PlayPayloadHandler<LargeIronSignScreenOpenPayload> {
    
    @Override
    public void receive(final LargeIronSignScreenOpenPayload payload, final ClientPlayNetworking.Context context) {
        BlockPos pos = payload.pos();
        MinecraftClient client = context.client();
        client.execute(() -> client.setScreen(new LargeIronSignScreen(pos)));
    }
    
}
