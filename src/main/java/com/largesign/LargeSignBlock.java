package com.largesign;

import java.util.Map;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureKey;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
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

public class LargeSignBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable {
	
	// BlockState properties
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	// Block ID, block and item singletons
	public static final Identifier ID = new Identifier(LargeSign.MOD_ID, "large_sign");
	public static final LargeSignBlock LARGE_SIGN_BLOCK = new LargeSignBlock(FabricBlockSettings.create()
			.requiresTool()
			.strength(1.5f, 6.0f));
	public static final BlockItem LARGE_SIGN_BLOCK_ITEM = new BlockItem(
			LargeSignBlock.LARGE_SIGN_BLOCK, 
			new FabricItemSettings());
	
	// Textures
	public static final TextureKey EDGE = TextureKey.of("edge");
	public static final TextureKey SYMBOL = TextureKey.of("symbol");
	
	// Network packets
	public static final Identifier LARGE_SIGN_SCREEN_OPEN_PACKET_ID = new Identifier(LargeSign.MOD_ID, "large_sign_screen_open");
	public static final Identifier LARGE_SIGN_SET_SYMBOL_PACKET_ID = new Identifier(LargeSign.MOD_ID, "large_sign_set_symbol");
	public static final Identifier LARGE_SIGN_REFRESH_MODEL_PACKET_ID = new Identifier(LargeSign.MOD_ID, "large_sign_refresh_model");
	
	// Dyes
	public static final Map<Item, Integer> FOREGROUND_DYES = Map.ofEntries(
			Map.entry(Items.BLACK_DYE, DyeColor.BLACK.getSignColor()),
			Map.entry(Items.GRAY_DYE, DyeColor.GRAY.getSignColor()),
			Map.entry(Items.LIGHT_GRAY_DYE, DyeColor.LIGHT_GRAY.getSignColor()),
			Map.entry(Items.WHITE_DYE, DyeColor.WHITE.getSignColor()),
			Map.entry(Items.RED_DYE, DyeColor.RED.getSignColor()),
			Map.entry(Items.BROWN_DYE, DyeColor.BROWN.getSignColor()),
			Map.entry(Items.ORANGE_DYE, DyeColor.ORANGE.getSignColor()),
			Map.entry(Items.YELLOW_DYE, DyeColor.YELLOW.getSignColor()),
			Map.entry(Items.LIME_DYE, DyeColor.LIME.getSignColor()),
			Map.entry(Items.GREEN_DYE, DyeColor.GREEN.getSignColor()),
			Map.entry(Items.CYAN_DYE, DyeColor.CYAN.getSignColor()),
			Map.entry(Items.LIGHT_BLUE_DYE, DyeColor.LIGHT_BLUE.getSignColor()),
			Map.entry(Items.BLUE_DYE, DyeColor.BLUE.getSignColor()),
			Map.entry(Items.PURPLE_DYE, DyeColor.PURPLE.getSignColor()),
			Map.entry(Items.MAGENTA_DYE, DyeColor.MAGENTA.getSignColor()),
			Map.entry(Items.PINK_DYE, DyeColor.PINK.getSignColor()));
	
	public LargeSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
        		.with(FACING, Direction.NORTH)
        		.with(WATERLOGGED, false));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(
				FACING,
				WATERLOGGED);
    }
	
	@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		Direction dir = state.get(FACING);
		return LargeSignBlock.getOutlineShape(dir);
    }
	
	public static VoxelShape getOutlineShape(Direction dir) {
		switch (dir) {
			case NORTH:
				return VoxelShapes.cuboid(0f, 0f, 0.9375f, 1f, 1f, 1f);
			case SOUTH:
				return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0.0625f);
			case EAST:
				return VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 1f, 1f);
			case WEST:
				return VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 1f, 1f);
			default:
				return VoxelShapes.fullCube();
		}
	}

	@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND && !world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
			Item item = player.getMainHandStack().getItem();
			if (FOREGROUND_DYES.containsKey(item)) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null && blockEntity instanceof LargeSignBlockEntity largeSignBlockEntity) {
					largeSignBlockEntity.foreground = FOREGROUND_DYES.get(item) | 0xff000000;
					LargeSignBlockEntity.syncUpdateToClient(largeSignBlockEntity, pos, serverPlayer);
				}
				return ActionResult.SUCCESS;				
			}
			
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeBlockPos(pos);
			ServerPlayNetworking.send(serverPlayer, LARGE_SIGN_SCREEN_OPEN_PACKET_ID, buf);
		}
		return ActionResult.SUCCESS;
	}
	
    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx)
				.with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
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

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LargeSignBlockEntity(pos, state);
	}
}
