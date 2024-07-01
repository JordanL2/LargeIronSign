package com.jordanl2.largeironsign;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import static com.jordanl2.largeironsign.LargeIronSignBlock.THICKNESS;
import static com.jordanl2.largeironsign.LargeIronSignBlock.TRIM_WIDTH;

public class LargeIronSignBlockNeighbourState {

    private boolean innerCornerTopLeft;
    private boolean innerCornerTopRight;
    private boolean innerCornerRightTop;
    private boolean innerCornerRightBottom;
    private boolean innerCornerBottomRight;
    private boolean innerCornerBottomLeft;
    private boolean innerCornerLeftBottom;
    private boolean innerCornerLeftTop;
    private boolean topLeftIsClear;
    private boolean topIsClear;
    private boolean topRightIsClear;
    private boolean rightIsClear;
    private boolean bottomRightIsClear;
    private boolean bottomIsClear;
    private boolean bottomLeftIsClear;
    private boolean leftIsClear;

    public LargeIronSignBlockNeighbourState() {
    }

    public LargeIronSignBlockNeighbourState(BlockView blockView, BlockState state, BlockPos pos) {
        Direction direction = state.get(LargeIronSignBlock.FACING);

        VoxelShape ourShape;
        BlockPos blockToTopLeftPos;
        BlockPos blockToTopPos = pos.add(0, 1, 0);
        BlockPos blockToTopRightPos;
        BlockPos blockToRightPos;
        BlockPos blockToBottomRightPos;
        BlockPos blockToBottomPos = pos.add(0, -1, 0);
        BlockPos blockToBottomLeftPos;
        BlockPos blockToLeftPos;

        switch (direction) {
            case NORTH:
                ourShape = VoxelShapes.cuboid(0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 1.0f - THICKNESS, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, 1f);
                blockToLeftPos = pos.add(1, 0, 0);
                blockToRightPos = pos.add(-1, 0, 0);
                blockToTopLeftPos = pos.add(1, 1, 0);
                blockToTopRightPos = pos.add(-1, 1, 0);
                blockToBottomLeftPos = pos.add(1, -1, 0);
                blockToBottomRightPos = pos.add(-1, -1, 0);
                break;
            case EAST:
                ourShape = VoxelShapes.cuboid(0f, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, THICKNESS, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH);
                blockToLeftPos = pos.add(0, 0, 1);
                blockToRightPos = pos.add(0, 0, -1);
                blockToTopLeftPos = pos.add(0, 1, 1);
                blockToTopRightPos = pos.add(0, 1, -1);
                blockToBottomLeftPos = pos.add(0, -1, 1);
                blockToBottomRightPos = pos.add(0, -1, -1);
                break;
            case SOUTH:
                ourShape = VoxelShapes.cuboid(0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 0f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, THICKNESS);
                blockToLeftPos = pos.add(-1, 0, 0);
                blockToRightPos = pos.add(1, 0, 0);
                blockToTopLeftPos = pos.add(-1, 1, 0);
                blockToTopRightPos = pos.add(1, 1, 0);
                blockToBottomLeftPos = pos.add(-1, -1, 0);
                blockToBottomRightPos = pos.add(1, -1, 0);
                break;
            case WEST:
            default:
                ourShape = VoxelShapes.cuboid(1.0f - THICKNESS, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 1f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH);
                blockToLeftPos = pos.add(0, 0, -1);
                blockToRightPos = pos.add(0, 0, 1);
                blockToTopLeftPos = pos.add(0, 1, -1);
                blockToTopRightPos = pos.add(0, 1, 1);
                blockToBottomLeftPos = pos.add(0, -1, -1);
                blockToBottomRightPos = pos.add(0, -1, 1);
                break;
        }
        BlockState blockToTopLeft = blockView.getBlockState(blockToTopLeftPos);
        BlockState blockToTop = blockView.getBlockState(blockToTopPos);
        BlockState blockToTopRight = blockView.getBlockState(blockToTopRightPos);
        BlockState blockToRight = blockView.getBlockState(blockToRightPos);
        BlockState blockToBottomRight = blockView.getBlockState(blockToBottomRightPos);
        BlockState blockToBottom = blockView.getBlockState(blockToBottomPos);
        BlockState blockToBottomLeft = blockView.getBlockState(blockToBottomLeftPos);
        BlockState blockToLeft = blockView.getBlockState(blockToLeftPos);

        Box ourShapeBox = ourShape.getBoundingBox().offset(pos);

        innerCornerTopLeft = blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.RIGHT_TRIM) && state.get(LargeIronSignBlock.TOP_TRIM);
        innerCornerTopRight = blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.LEFT_TRIM) && state.get(LargeIronSignBlock.TOP_TRIM);
        innerCornerRightTop = blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.BOTTOM_TRIM) && state.get(LargeIronSignBlock.RIGHT_TRIM);
        innerCornerRightBottom = blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.TOP_TRIM) && state.get(LargeIronSignBlock.RIGHT_TRIM);
        innerCornerBottomRight = blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.LEFT_TRIM) && state.get(LargeIronSignBlock.BOTTOM_TRIM);
        innerCornerBottomLeft = blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.RIGHT_TRIM) && state.get(LargeIronSignBlock.BOTTOM_TRIM);
        innerCornerLeftBottom = blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.TOP_TRIM) && state.get(LargeIronSignBlock.LEFT_TRIM);
        innerCornerLeftTop = blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.BOTTOM_TRIM) && state.get(LargeIronSignBlock.LEFT_TRIM);
        topLeftIsClear = blockIsClear(blockToTopLeft, blockToTopLeftPos, blockView, ourShapeBox);
        topIsClear = blockIsClear(blockToTop, blockToTopPos, blockView, ourShapeBox);
        topRightIsClear = blockIsClear(blockToTopRight, blockToTopRightPos, blockView, ourShapeBox);
        rightIsClear = blockIsClear(blockToRight, blockToRightPos, blockView, ourShapeBox);
        bottomRightIsClear = blockIsClear(blockToBottomRight, blockToBottomRightPos, blockView, ourShapeBox);
        bottomIsClear = blockIsClear(blockToBottom, blockToBottomPos, blockView, ourShapeBox);
        bottomLeftIsClear = blockIsClear(blockToBottomLeft, blockToBottomLeftPos, blockView, ourShapeBox);
        leftIsClear = blockIsClear(blockToLeft, blockToLeftPos, blockView, ourShapeBox);
    }

    private boolean blockIsClear(BlockState blockState, BlockPos pos, BlockView blockView, Box ourShape) {
        if (blockState.isAir()) {
            return true;
        }
        if (blockState.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK)) {
            return false;
        }
        VoxelShape thisShape = blockState.getOutlineShape(blockView, pos);
        if (thisShape.isEmpty()) {
            return true;
        }
        return !ourShape.intersects(thisShape.getBoundingBox().offset(pos));
    }

    public boolean innerCornerTopLeft() {
        return innerCornerTopLeft;
    }

    public boolean innerCornerTopRight() {
        return innerCornerTopRight;
    }

    public boolean innerCornerRightTop() {
        return innerCornerRightTop;
    }

    public boolean innerCornerRightBottom() {
        return innerCornerRightBottom;
    }

    public boolean innerCornerBottomRight() {
        return innerCornerBottomRight;
    }

    public boolean innerCornerBottomLeft() {
        return innerCornerBottomLeft;
    }

    public boolean innerCornerLeftBottom() {
        return innerCornerLeftBottom;
    }

    public boolean innerCornerLeftTop() {
        return innerCornerLeftTop;
    }

    public boolean topLeftIsClear() {
        return topLeftIsClear;
    }

    public boolean topIsClear() {
        return topIsClear;
    }

    public boolean topRightIsClear() {
        return topRightIsClear;
    }

    public boolean rightIsClear() {
        return rightIsClear;
    }

    public boolean bottomRightIsClear() {
        return bottomRightIsClear;
    }

    public boolean bottomIsClear() {
        return bottomIsClear;
    }

    public boolean bottomLeftIsClear() {
        return bottomLeftIsClear;
    }

    public boolean leftIsClear() {
        return leftIsClear;
    }
}
