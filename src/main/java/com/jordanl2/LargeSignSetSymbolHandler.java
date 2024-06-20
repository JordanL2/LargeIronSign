package com.jordanl2;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeSignSetSymbolHandler implements ServerPlayNetworking.PlayChannelHandler {

	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		Identifier worldValue = buf.readIdentifier();
		World world = null;
		for (World thisWorld : server.getWorlds()) {
			if (thisWorld.getRegistryKey().getValue().equals(worldValue)) {
				world = thisWorld;
				break;
			}
		}
		
		BlockPos pos = buf.readBlockPos();
				
		String characterName = buf.readString();
		LargeSignCharacter character = LargeSignCharacter.valueOf(characterName);

		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof LargeSignBlock) {
			world.setBlockState(pos, blockState.with(LargeSignBlock.CHAR, character));
		}
	}

}
