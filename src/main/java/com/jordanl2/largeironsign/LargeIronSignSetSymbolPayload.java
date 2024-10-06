package com.jordanl2.largeironsign;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record LargeIronSignSetSymbolPayload(BlockPos pos, String characterName, boolean trim) implements CustomPayload {
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static final CustomPayload.Id<LargeIronSignSetSymbolPayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + "_set_symbol"));
    public static final PacketCodec<RegistryByteBuf, LargeIronSignSetSymbolPayload> PACKET_CODEC =
            PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, LargeIronSignSetSymbolPayload::pos,
                    PacketCodecs.STRING, LargeIronSignSetSymbolPayload::characterName,
                    PacketCodecs.BOOL, LargeIronSignSetSymbolPayload::trim,
                    LargeIronSignSetSymbolPayload::new).cast();
}
