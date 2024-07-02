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
    public void receive(final MinecraftServer server, final ServerPlayerEntity player,
                        final ServerPlayNetworkHandler handler, final PacketByteBuf buf,
                        final PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        String characterName = buf.readString();
        boolean trim = buf.readBoolean();
        LargeIronSignCharacter character = LargeIronSignCharacter.valueOf(characterName);
        
        server.execute(() -> {
            World world = player.getWorld();
            BlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() instanceof LargeIronSignBlock) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
                    largeIronSignBlockEntity.character = character;
                    world.setBlockState(pos, blockState
                            .with(LargeIronSignBlock.TRIM, trim));
                    largeIronSignBlockEntity.markDirty();
                }
            }
        });
    }
    
}
