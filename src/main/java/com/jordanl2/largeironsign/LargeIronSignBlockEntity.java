package com.jordanl2.largeironsign;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LargeIronSignBlockEntity extends BlockEntity {
    
    public static final Identifier ID = new Identifier(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + "_block_entity");
    
    // Entity properties
    public volatile LargeIronSignCharacter character = LargeIronSignCharacter.SPACE;
    public volatile int foreground = LargeIronSignBlock.DEFAULT_COLOUR_FOREGROUND;
    public volatile int background = LargeIronSignBlock.DEFAULT_COLOUR_BACKGROUND;
    
    public LargeIronSignBlockEntity(final BlockPos pos, final BlockState state) {
        super(LargeIronSign.LARGE_IRON_SIGN_BLOCK_ENTITY, pos, state);
    }
    
    @Override
    public void writeNbt(final NbtCompound nbt) {
        nbt.putString("character", character.name());
        nbt.putInt("foreground", foreground);
        nbt.putInt("background", background);
        
        super.writeNbt(nbt);
    }
    
    @Override
    public void readNbt(final NbtCompound nbt) {
        super.readNbt(nbt);
        
        character = LargeIronSignCharacter.valueOf(nbt.getString("character"));
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