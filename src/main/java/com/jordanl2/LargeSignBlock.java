package com.jordanl2;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.data.client.TextureKey;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LargeSignBlock extends Block {
	
	public static final EnumProperty<LargeSignCharacter> CHAR = EnumProperty.of("char", LargeSignCharacter.class);
	
	public static final Identifier ID = new Identifier("jordanl2", "large_sign");
	public static final LargeSignBlock LARGE_SIGN_BLOCK = new LargeSignBlock(FabricBlockSettings.copyOf(Blocks.STONE));
	public static final BlockItem LARGE_SIGN_BLOCK_ITEM = new BlockItem(LargeSignBlock.LARGE_SIGN_BLOCK, new FabricItemSettings());
	
	public static final TextureKey EDGE = TextureKey.of("edge");
	public static final TextureKey SYMBOL = TextureKey.of("symbol");
	

	public LargeSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHAR, LargeSignCharacter.SPACE));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CHAR);
    }
	
	@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0.125f);
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
