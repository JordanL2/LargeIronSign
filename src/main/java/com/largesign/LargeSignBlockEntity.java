package com.largesign;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class LargeSignBlockEntity extends BlockEntity {
	
	public class LargeSignBlockEntityState {
		public volatile LargeSignCharacter character = LargeSignCharacter.SPACE;
	}

	
	public final LargeSignBlockEntityState state = new LargeSignBlockEntityState();
    
	public LargeSignBlockEntity(BlockPos pos, BlockState state) {
        super(LargeSign.LARGE_SIGN_BLOCK_ENTITY, pos, state);
    }
	 
    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("character", state.character.ordinal());
 
        super.writeNbt(nbt);
    }
 
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
 
        state.character = LargeSignCharacter.values()[nbt.getInt("character")];
    }
    
    @Override
    public Object getRenderData() {
		return state;
	}
}