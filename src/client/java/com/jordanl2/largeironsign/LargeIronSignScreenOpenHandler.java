package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class LargeIronSignScreenOpenHandler implements ClientPlayNetworking.PlayChannelHandler {
    
    @Override
    public void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
                        final PacketByteBuf buf, final PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        client.execute(() -> client.setScreen(new LargeIronSignScreen(pos)));
    }
    
}
