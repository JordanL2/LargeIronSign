package com.largesign;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class LargeSignRefreshModelHandler implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        client.execute(() -> {
            System.out.println("JORDAN refresh model at " + pos.toShortString());
        	ClientWorld world = client.world;
        	BlockState blockState = world.getBlockState(pos);
			world.updateListeners(pos, blockState, blockState, Block.NOTIFY_LISTENERS);
        });
    }

}
