package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class LargeIronSignRefreshModelHandler implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        LargeIronSignCharacter character = buf.readEnumConstant(LargeIronSignCharacter.class);
        int foreground = buf.readInt();
        int background = buf.readInt();
        client.execute(() -> {
        	ClientWorld world = client.world;
        	
        	BlockEntity blockEntity = world.getBlockEntity(pos);
        	if (blockEntity != null && blockEntity instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
        		// Update block entity
        		largeIronSignBlockEntity.character = character;
        		largeIronSignBlockEntity.foreground = foreground;
        		largeIronSignBlockEntity.background = background;

        		// Trigger re-render
            	BlockState blockState = world.getBlockState(pos);
            	world.updateListeners(pos, blockState, blockState, Block.NOTIFY_LISTENERS);
        	}
        });
    }

}
