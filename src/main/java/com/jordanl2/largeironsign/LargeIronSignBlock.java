package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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

import java.util.Map;
import java.util.Objects;

public class LargeIronSignBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable {
    
    // BlockState properties
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty TRIM = BooleanProperty.of("trim");
    
    // IDs
    public static final String PATH = "large_iron_sign";
    public static final String BLOCK_PATH = "block/" + PATH;
    public static final String ITEM_PATH = "item/" + PATH;
    public static final Identifier ID = new Identifier(LargeIronSign.MOD_ID, PATH);
    
    // Block and Item singletons
    public static final LargeIronSignBlock LARGE_IRON_SIGN_BLOCK = new LargeIronSignBlock(FabricBlockSettings.create()
            .requiresTool()
            .strength(1.5f, 6f));
    public static final BlockItem LARGE_IRON_SIGN_BLOCK_ITEM = new BlockItem(
            LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK,
            new FabricItemSettings());
    
    // Textures
    public static final Identifier EDGE_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_edge");
    public static final Identifier BACK_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_back");
    public static final Identifier FRONT_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_front");
    public static final Identifier TRIM_FRONT_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_front");
    public static final Identifier TRIM_BACK_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_front");
    public static final Identifier TRIM_EDGE_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_edge");
    public static final Identifier TRIM_INSIDE_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_inside");
    public static final Identifier TRIM_CORNER_EDGE_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_corner_edge");
    public static final Identifier TRIM_CORNER_FRONT_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_corner_front");
    public static final Identifier TRIM_INNER_CORNER_FRONT_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_inner_corner_front");
    public static final Identifier TRIM_INNER_CORNER_BACK_TEXTURE = new Identifier(LargeIronSign.MOD_ID, "block/" + PATH + "_trim_inner_corner_front");
    
    // Model
    public static final float THICKNESS = 1f / 16f;
    public static final float TRIM_WIDTH = 2f / 16f;
    
    // Network packets
    public static final Identifier LARGE_IRON_SIGN_SCREEN_OPEN_PACKET_ID = new Identifier(LargeIronSign.MOD_ID, PATH + "_screen_open");
    public static final Identifier LARGE_IRON_SIGN_SET_SYMBOL_PACKET_ID = new Identifier(LargeIronSign.MOD_ID, PATH + "_set_symbol");
    
    // Dyes
    public static final int DEFAULT_COLOUR_FOREGROUND = DyeColor.BLACK.getSignColor() | 0xff000000;
    public static final int DEFAULT_COLOUR_BACKGROUND = DyeColor.WHITE.getSignColor() | 0xff000000;
    public static final Map<Item, Integer> DYES = Map.ofEntries(
            Map.entry(Items.BLACK_DYE, DyeColor.BLACK.getSignColor() | 0xff000000),
            Map.entry(Items.GRAY_DYE, DyeColor.GRAY.getSignColor() | 0xff000000),
            Map.entry(Items.LIGHT_GRAY_DYE, DyeColor.LIGHT_GRAY.getSignColor() | 0xff000000),
            Map.entry(Items.WHITE_DYE, DyeColor.WHITE.getSignColor() | 0xff000000),
            Map.entry(Items.RED_DYE, DyeColor.RED.getSignColor() | 0xff000000),
            Map.entry(Items.BROWN_DYE, DyeColor.BROWN.getSignColor() | 0xff000000),
            Map.entry(Items.ORANGE_DYE, DyeColor.ORANGE.getSignColor() | 0xff000000),
            Map.entry(Items.YELLOW_DYE, DyeColor.YELLOW.getSignColor() | 0xff000000),
            Map.entry(Items.LIME_DYE, DyeColor.LIME.getSignColor() | 0xff000000),
            Map.entry(Items.GREEN_DYE, DyeColor.GREEN.getSignColor() | 0xff000000),
            Map.entry(Items.CYAN_DYE, DyeColor.CYAN.getSignColor() | 0xff000000),
            Map.entry(Items.LIGHT_BLUE_DYE, DyeColor.LIGHT_BLUE.getSignColor() | 0xff000000),
            Map.entry(Items.BLUE_DYE, DyeColor.BLUE.getSignColor() | 0xff000000),
            Map.entry(Items.PURPLE_DYE, DyeColor.PURPLE.getSignColor() | 0xff000000),
            Map.entry(Items.MAGENTA_DYE, DyeColor.MAGENTA.getSignColor() | 0xff000000),
            Map.entry(Items.PINK_DYE, DyeColor.PINK.getSignColor() | 0xff000000));
    
    public LargeIronSignBlock(final Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
                .with(TRIM, false));
    }
    
    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
        builder.add(
                FACING,
                WATERLOGGED,
                TRIM);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos,
                                      final ShapeContext context) {
        Direction dir = state.get(FACING);
        LargeIronSignBlockNeighbourState neighbourState = new LargeIronSignBlockNeighbourState(view, state, pos);
        boolean trim = state.get(TRIM);
        float trimLeft = trim && neighbourState.leftIsClear() ? TRIM_WIDTH : 0f;
        float trimRight = trim && neighbourState.rightIsClear() ? TRIM_WIDTH : 0f;
        float trimTop = trim && neighbourState.topIsClear() ? TRIM_WIDTH : 0f;
        float trimBottom = trim && neighbourState.bottomIsClear() ? TRIM_WIDTH : 0f;
        return switch (dir) {
            case NORTH ->
                    VoxelShapes.cuboid(0f - trimRight, 0f - trimBottom, 1f - THICKNESS, 1f + trimLeft, 1f + trimTop, 1f);
            case SOUTH ->
                    VoxelShapes.cuboid(0f - trimLeft, 0f - trimBottom, 0f, 1f + trimRight, 1f + trimTop, THICKNESS);
            case EAST ->
                    VoxelShapes.cuboid(0f, 0f - trimBottom, 0f - trimRight, THICKNESS, 1f + trimTop, 1f + trimLeft);
            case WEST ->
                    VoxelShapes.cuboid(1f - THICKNESS, 0f - trimBottom, 0f - trimLeft, 1f, 1f + trimTop, 1f + trimRight);
            default -> VoxelShapes.fullCube();
        };
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(final BlockState state, final World world, final BlockPos pos,
                              final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
        Item item1 = player.getMainHandStack().getItem();
        Item item2 = player.getOffHandStack().getItem();
        if (hand == Hand.MAIN_HAND) {
            // If holding dye, apply foreground / background colour on sign
            if (DYES.containsKey(item1) || DYES.containsKey(item2)) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
                    if (DYES.containsKey(item1)) {
                        largeIronSignBlockEntity.foreground = DYES.get(item1);
                    }
                    if (DYES.containsKey(item2)) {
                        largeIronSignBlockEntity.background = DYES.get(item2);
                    }
                    if (world.isClient()) {
                        // On client side, cause block to re-render
                        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                    } else {
                        // On server side, ensure block entity gets saved
                        largeIronSignBlockEntity.markDirty();
                    }
                }
            } else if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
                // Otherwise, open sign edit screen
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(pos);
                ServerPlayNetworking.send(serverPlayer, LARGE_IRON_SIGN_SCREEN_OPEN_PACKET_ID, buf);
                return ActionResult.SUCCESS;
            }
        }
        
        // If holding dye, ensure animation plays for correct hand
        if (world.isClient()) {
            if (DYES.containsKey(item1) || DYES.containsKey(item2)) {
                if (hand == Hand.MAIN_HAND && DYES.containsKey(item1)) {
                    return ActionResult.SUCCESS;
                } else if (hand == Hand.OFF_HAND && DYES.containsKey(item2)) {
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }
        }
        
        return ActionResult.SUCCESS;
    }
    
    @Override
    public BlockState getPlacementState(final ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx))
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(final BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(final BlockState state, final Direction direction,
                                                final BlockState neighborState, final WorldAccess world,
                                                final BlockPos pos, final BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public BlockEntity createBlockEntity(final BlockPos pos, final BlockState state) {
        return new LargeIronSignBlockEntity(pos, state);
    }
}
