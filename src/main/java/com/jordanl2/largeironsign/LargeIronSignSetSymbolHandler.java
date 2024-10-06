package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeIronSignSetSymbolHandler implements ServerPlayNetworking.PlayPayloadHandler<LargeIronSignSetSymbolPayload> {
    
    @Override
    public void receive(final LargeIronSignSetSymbolPayload payload, final ServerPlayNetworking.Context context) {
        BlockPos pos = payload.pos();
        String characterName = payload.characterName();
        boolean trim = payload.trim();
        LargeIronSignCharacter character = LargeIronSignCharacter.valueOf(characterName);
        
        MinecraftServer server = context.server();
        server.execute(() -> {
            World world = context.player().getWorld();
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

