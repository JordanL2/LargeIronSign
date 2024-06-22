package com.largesign;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LargeSignBlockEntity extends BlockEntity {
    
	public LargeSignBlockEntity(BlockPos pos, BlockState state) {
        super(LargeSign.LARGE_SIGN_BLOCK_ENTITY, pos, state);
    }
	
}