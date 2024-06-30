package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeIronSignSetSymbolHandler implements ServerPlayNetworking.PlayChannelHandler {

	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		BlockPos pos = buf.readBlockPos();
		String characterName = buf.readString();
		boolean topTrim = buf.readBoolean();
		boolean rightTrim = buf.readBoolean();
		boolean bottomTrim = buf.readBoolean();
		boolean leftTrim = buf.readBoolean();
		LargeIronSignCharacter character = LargeIronSignCharacter.valueOf(characterName);

		server.execute(() -> {
			World world = player.getWorld();
			BlockState blockState = world.getBlockState(pos);
			if (blockState.getBlock() instanceof LargeIronSignBlock) {
	        	BlockEntity blockEntity = world.getBlockEntity(pos);
	        	if (blockEntity instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
	        		largeIronSignBlockEntity.character = character;
					world.setBlockState(pos, blockState
							.with(LargeIronSignBlock.TOP_TRIM, topTrim)
							.with(LargeIronSignBlock.RIGHT_TRIM, rightTrim)
							.with(LargeIronSignBlock.BOTTOM_TRIM, bottomTrim)
							.with(LargeIronSignBlock.LEFT_TRIM, leftTrim));
	        		largeIronSignBlockEntity.markDirty();
	        	}
			}
		});
	}

}
