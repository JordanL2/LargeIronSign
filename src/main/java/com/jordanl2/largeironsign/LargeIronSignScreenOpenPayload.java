package com.jordanl2.largeironsign;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record LargeIronSignScreenOpenPayload(BlockPos pos) implements CustomPayload {
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
    
    public static final CustomPayload.Id<LargeIronSignScreenOpenPayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + "_screen_open"));
    public static final PacketCodec<RegistryByteBuf, LargeIronSignScreenOpenPayload> PACKET_CODEC =
            PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, LargeIronSignScreenOpenPayload::pos,
                    LargeIronSignScreenOpenPayload::new).cast();
}
