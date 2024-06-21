package com.jordanl2;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.data.client.TextureKey;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class LargeSignBlock extends HorizontalFacingBlock implements Waterloggable {
	
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<LargeSignCharacter> CHAR = EnumProperty.of("char", LargeSignCharacter.class);
	
	public static final Identifier ID = new Identifier("jordanl2", "large_sign");
	public static final LargeSignBlock LARGE_SIGN_BLOCK = new LargeSignBlock(FabricBlockSettings.create()
			.requiresTool()
			.strength(1.5f, 6.0f));
	public static final BlockItem LARGE_SIGN_BLOCK_ITEM = new BlockItem(LargeSignBlock.LARGE_SIGN_BLOCK, new FabricItemSettings());
	
	public static final TextureKey EDGE = TextureKey.of("edge");
	public static final TextureKey SYMBOL = TextureKey.of("symbol");
	
	public static final Identifier LARGE_SIGN_SCREEN_OPEN_PACKET_ID = new Identifier("jordanl2", "large_sign_screen_open");
	public static final Identifier LARGE_SIGN_SET_SYMBOL_PACKET_ID = new Identifier("jordanl2", "large_sign_set_symbol");
	
	public LargeSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
        		.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
        		.with(WATERLOGGED, false)
        		.with(CHAR, LargeSignCharacter.SPACE));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(
				FACING,
				WATERLOGGED,
				CHAR);
    }
	
	@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		Direction dir = state.get(FACING);
		switch(dir) {
			case NORTH:
				return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0.125f);
			case SOUTH:
				return VoxelShapes.cuboid(0f, 0f, 0.875f, 1f, 1f, 1f);
			case EAST:
				return VoxelShapes.cuboid(0.875f, 0f, 0f, 1f, 1f, 1f);
			case WEST:
				return VoxelShapes.cuboid(0f, 0f, 0f, 0.125f, 1f, 1f);
			default:
				return VoxelShapes.fullCube();
		}
    }

	@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND && !world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeIdentifier(world.getRegistryKey().getValue());
			buf.writeBlockPos(pos);
			ServerPlayNetworking.send(serverPlayer, LARGE_SIGN_SCREEN_OPEN_PACKET_ID, buf);
		}
		return ActionResult.SUCCESS;
	}
	
    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx)
				.with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing())
				.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
 
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
