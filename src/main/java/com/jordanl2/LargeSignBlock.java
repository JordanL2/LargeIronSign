package com.jordanl2;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeSignBlock extends Block {
	
	public static final EnumProperty<LargeSignCharacter> CHAR = EnumProperty.of("char", LargeSignCharacter.class);
	
	public static final LargeSignBlock LARGE_SIGN_BLOCK = new LargeSignBlock(FabricBlockSettings.copyOf(Blocks.STONE));
	

	public LargeSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHAR, LargeSignCharacter.SPACE));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		if (CHAR == null) {
			System.out.println("CHAR IS NULL!!!!!");
		}
		builder.add(CHAR);
    }
	
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.literal("Setting block to A"), false);
        }
    	world.setBlockState(pos, state.with(CHAR, LargeSignCharacter.A));
 
        return ActionResult.SUCCESS;
    }

}
