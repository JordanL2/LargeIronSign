package com.jordanl2.largeironsign;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LargeIronSignBlockEntity extends BlockEntity {
	
	public static final Identifier ID = new Identifier(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + "_block_entity");
	
	public volatile LargeIronSignCharacter character = LargeIronSignCharacter.SPACE;
	public volatile int foreground = LargeIronSignBlock.DEFAULT_COLOUR_FOREGROUND;
	public volatile int background = LargeIronSignBlock.DEFAULT_COLOUR_BACKGROUND;
    
	public LargeIronSignBlockEntity(BlockPos pos, BlockState state) {
        super(LargeIronSign.LARGE_IRON_SIGN_BLOCK_ENTITY, pos, state);
    }
	
	public static void syncUpdateToClient(LargeIronSignBlockEntity blockEntity, BlockPos pos, ServerPlayerEntity player) {
		PacketByteBuf sendBuf = PacketByteBufs.create();
		sendBuf.writeBlockPos(pos);
		sendBuf.writeEnumConstant(blockEntity.character);
		sendBuf.writeInt(blockEntity.foreground);
		sendBuf.writeInt(blockEntity.background);
		ServerPlayNetworking.send(player, LargeIronSignBlock.LARGE_IRON_SIGN_REFRESH_MODEL_PACKET_ID, sendBuf);
	}
	 
    @Override
    public void writeNbt(NbtCompound nbt) {
    	nbt.putInt("character", character.ordinal());
    	nbt.putInt("foreground", foreground);
        nbt.putInt("background", background);
 
        super.writeNbt(nbt);
    }
 
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
 
        character = LargeIronSignCharacter.values()[nbt.getInt("character")];
        foreground = nbt.getInt("foreground");
        background = nbt.getInt("background");
    }
    
    @Override
    public Object getRenderData() {
		return this;
	}
    
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
    }
   
    @Override
    public NbtCompound toInitialChunkDataNbt() {
      return createNbt();
    }
}