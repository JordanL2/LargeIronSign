package com.jordanl2.largeironsign;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
    
    public static final Identifier ID = Identifier.of(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + "_block_entity");
    
    // Entity properties
    public volatile LargeIronSignCharacter character = LargeIronSignCharacter.SPACE;
    public volatile int foreground = LargeIronSignBlock.DEFAULT_COLOUR_FOREGROUND;
    public volatile int background = LargeIronSignBlock.DEFAULT_COLOUR_BACKGROUND;
    
    public LargeIronSignBlockEntity(final BlockPos pos, final BlockState state) {
        super(LargeIronSign.LARGE_IRON_SIGN_BLOCK_ENTITY, pos, state);
    }
    
    @Override
    protected void writeData(final WriteView view) {
        view.putString("character", character.name());
        view.putInt("foreground", foreground);
        view.putInt("background", background);
        
        super.writeData(view);
    }
    
    @Override
    protected void readData(final ReadView view) {
        super.readData(view);
        
        character = LargeIronSignCharacter.valueOf(view.getString("character", "x"));
        foreground = view.getInt("foreground", 0);
        background = view.getInt("background", 0);
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
    public NbtCompound toInitialChunkDataNbt(final RegistryWrapper.WrapperLookup wrapperLookup) {
        return createNbt(wrapperLookup);
    }
}