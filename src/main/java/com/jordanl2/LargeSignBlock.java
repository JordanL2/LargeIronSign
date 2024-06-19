package com.jordanl2;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeSignBlock extends Block {
	
	public static final Identifier ID = new Identifier("jordanl2", "large_sign");
	public static final LargeSignBlock LARGE_SIGN_BLOCK = new LargeSignBlock(FabricBlockSettings.copyOf(Blocks.STONE));
	public static final BlockItem LARGE_SIGN_BLOCK_ITEM = new BlockItem(LargeSignBlock.LARGE_SIGN_BLOCK, new FabricItemSettings());
	
	public static final EnumProperty<LargeSignCharacter> CHAR = EnumProperty.of("char", LargeSignCharacter.class);
	

	public LargeSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHAR, LargeSignCharacter.SPACE));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CHAR);
    }
	
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockState blockState = world.getBlockState(pos);
        LargeSignCharacter currentChar = blockState.getOrEmpty(CHAR).get();
        LargeSignCharacter nextChar = currentChar.getNext();
    	world.setBlockState(pos, state.with(CHAR, nextChar));
        if (!world.isClient) {
            player.sendMessage(Text.literal("Changed block from " + currentChar.getDescription() + " to " + nextChar.getDescription()), false);
        }
 
        return ActionResult.SUCCESS;
    }

}
