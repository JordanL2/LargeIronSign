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
                blockToLeftPos = pos.add(1, 0, 0);
                blockToRightPos = pos.add(-1, 0, 0);
                blockToTopLeftPos = pos.add(1, 1, 0);
                blockToTopRightPos = pos.add(-1, 1, 0);
                blockToBottomLeftPos = pos.add(1, -1, 0);
                blockToBottomRightPos = pos.add(-1, -1, 0);
                break;
            case EAST:
                blockToLeftPos = pos.add(0, 0, 1);
                blockToRightPos = pos.add(0, 0, -1);
                blockToTopLeftPos = pos.add(0, 1, 1);
                blockToTopRightPos = pos.add(0, 1, -1);
                blockToBottomLeftPos = pos.add(0, -1, 1);
                blockToBottomRightPos = pos.add(0, -1, -1);
                break;
            case SOUTH:
                blockToLeftPos = pos.add(-1, 0, 0);
                blockToRightPos = pos.add(1, 0, 0);
                blockToTopLeftPos = pos.add(-1, 1, 0);
                blockToTopRightPos = pos.add(1, 1, 0);
                blockToBottomLeftPos = pos.add(-1, -1, 0);
                blockToBottomRightPos = pos.add(1, -1, 0);
                break;
            case WEST:
            default:
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

        VoxelShape ourShape = makeOutlineShape(direction);
        Box ourShapeBox = ourShape.getBoundingBox().offset(pos);

        boolean trim = state.get(LargeIronSignBlock.TRIM);

        topLeftIsClear = blockIsClear(blockToTopLeft, blockToTopLeftPos, blockView, ourShapeBox);
        topIsClear = blockIsClear(blockToTop, blockToTopPos, blockView, ourShapeBox);
        topRightIsClear = blockIsClear(blockToTopRight, blockToTopRightPos, blockView, ourShapeBox);
        rightIsClear = blockIsClear(blockToRight, blockToRightPos, blockView, ourShapeBox);
        bottomRightIsClear = blockIsClear(blockToBottomRight, blockToBottomRightPos, blockView, ourShapeBox);
        bottomIsClear = blockIsClear(blockToBottom, blockToBottomPos, blockView, ourShapeBox);
        bottomLeftIsClear = blockIsClear(blockToBottomLeft, blockToBottomLeftPos, blockView, ourShapeBox);
        leftIsClear = blockIsClear(blockToLeft, blockToLeftPos, blockView, ourShapeBox);
        innerCornerTopLeft = trim && blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.TRIM) && topIsClear;
        innerCornerTopRight = trim && blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.TRIM) && topIsClear;
        innerCornerRightTop = trim && blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.TRIM) && rightIsClear;
        innerCornerRightBottom = trim && blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.TRIM) && rightIsClear;
        innerCornerBottomRight = trim && blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.TRIM) && bottomIsClear;
        innerCornerBottomLeft = trim && blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.TRIM) && bottomIsClear;
        innerCornerLeftBottom = trim && blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.TRIM) && leftIsClear;
        innerCornerLeftTop = trim && blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.TRIM) && leftIsClear;
    }

    private boolean blockIsClear(BlockState blockState, BlockPos pos, BlockView blockView, Box ourShape) {
        if (blockState.isAir()) {
            return true;
        }
        VoxelShape thisShape;
        if (blockState.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK)) {
            thisShape = makeOutlineShape(blockState.get(LargeIronSignBlock.FACING));
        } else {
            thisShape = blockState.getOutlineShape(blockView, pos);
        }
        if (thisShape.isEmpty()) {
            return true;
        }
        return !ourShape.intersects(thisShape.getBoundingBox().offset(pos));
    }

    private VoxelShape makeOutlineShape(Direction direction) {
        return switch (direction) {
            case NORTH ->
                    VoxelShapes.cuboid(0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 1f - THICKNESS, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, 1f);
            case EAST ->
                    VoxelShapes.cuboid(0f, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, THICKNESS, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH);
            case SOUTH ->
                    VoxelShapes.cuboid(0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 0f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, THICKNESS);
            default ->
                    VoxelShapes.cuboid(1f - THICKNESS, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 1f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH);
        };
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
