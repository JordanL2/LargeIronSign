package com.largesign;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeSignSetSymbolHandler implements ServerPlayNetworking.PlayChannelHandler {

	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		BlockPos pos = buf.readBlockPos();
		String characterName = buf.readString();
		LargeSignCharacter character = LargeSignCharacter.valueOf(characterName);

		server.execute(() -> {
			World world = player.getWorld();
			BlockState blockState = world.getBlockState(pos);
			if (blockState.getBlock() instanceof LargeSignBlock) {
				LargeSignBlockEntity blockEntity = (LargeSignBlockEntity) world.getBlockEntity(pos);
				blockEntity.character = character;
				blockEntity.markDirty();
				world.updateListeners(pos, blockState, blockState, Block.NOTIFY_LISTENERS);
				
				PacketByteBuf sendBuf = PacketByteBufs.create();
				sendBuf.writeBlockPos(pos);
				sendBuf.writeEnumConstant(character);
				ServerPlayNetworking.send(player, LargeSignBlock.LARGE_SIGN_REFRESH_MODEL_PACKET_ID, sendBuf);
			}
		});
	}

}
