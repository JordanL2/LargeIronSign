package com.jordanl2.largeironsign;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

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

    public LargeIronSignBlockNeighbourState(BlockRenderView blockView, BlockState state, BlockPos pos) {
        Direction direction = state.get(LargeIronSignBlock.FACING);

        BlockState blockToTopLeft;
        BlockState blockToTop = blockView.getBlockState(pos.add(0, 1, 0));
        BlockState blockToTopRight;
        BlockState blockToRight;
        BlockState blockToBottomRight;
        BlockState blockToBottom = blockView.getBlockState(pos.add(0, -1, 0));
        BlockState blockToBottomLeft;
        BlockState blockToLeft;
        switch (direction) {
            case NORTH:
                blockToLeft = blockView.getBlockState(pos.add(1, 0, 0));
                blockToRight = blockView.getBlockState(pos.add(-1, 0, 0));
                blockToTopLeft = blockView.getBlockState(pos.add(1, 1, 0));
                blockToTopRight = blockView.getBlockState(pos.add(-1, 1, 0));
                blockToBottomLeft = blockView.getBlockState(pos.add(1, -1, 0));
                blockToBottomRight = blockView.getBlockState(pos.add(-1, -1, 0));
                break;
            case EAST:
                blockToLeft = blockView.getBlockState(pos.add(0, 0, 1));
                blockToRight = blockView.getBlockState(pos.add(0, 0, -1));
                blockToTopLeft = blockView.getBlockState(pos.add(0, 1, 1));
                blockToTopRight = blockView.getBlockState(pos.add(0, 1, -1));
                blockToBottomLeft = blockView.getBlockState(pos.add(0, -1, 1));
                blockToBottomRight = blockView.getBlockState(pos.add(0, -1, -1));
                break;
            case SOUTH:
                blockToLeft = blockView.getBlockState(pos.add(-1, 0, 0));
                blockToRight = blockView.getBlockState(pos.add(1, 0, 0));
                blockToTopLeft = blockView.getBlockState(pos.add(-1, 1, 0));
                blockToTopRight = blockView.getBlockState(pos.add(1, 1, 0));
                blockToBottomLeft = blockView.getBlockState(pos.add(-1, -1, 0));
                blockToBottomRight = blockView.getBlockState(pos.add(1, -1, 0));
                break;
            case WEST:
            default:
                blockToLeft = blockView.getBlockState(pos.add(0, 0, -1));
                blockToRight = blockView.getBlockState(pos.add(0, 0, 1));
                blockToTopLeft = blockView.getBlockState(pos.add(0, 1, -1));
                blockToTopRight = blockView.getBlockState(pos.add(0, 1, 1));
                blockToBottomLeft = blockView.getBlockState(pos.add(0, -1, -1));
                blockToBottomRight = blockView.getBlockState(pos.add(0, -1, 1));
                break;
        }

        innerCornerTopLeft = blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.RIGHT_TRIM) && state.get(LargeIronSignBlock.TOP_TRIM);
        innerCornerTopRight = blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.LEFT_TRIM) && state.get(LargeIronSignBlock.TOP_TRIM);
        innerCornerRightTop = blockToTopRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopRight.get(LargeIronSignBlock.BOTTOM_TRIM) && state.get(LargeIronSignBlock.RIGHT_TRIM);
        innerCornerRightBottom = blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.TOP_TRIM) && state.get(LargeIronSignBlock.RIGHT_TRIM);
        innerCornerBottomRight = blockToBottomRight.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomRight.get(LargeIronSignBlock.LEFT_TRIM) && state.get(LargeIronSignBlock.BOTTOM_TRIM);
        innerCornerBottomLeft = blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.RIGHT_TRIM) && state.get(LargeIronSignBlock.BOTTOM_TRIM);
        innerCornerLeftBottom = blockToBottomLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToBottomLeft.get(LargeIronSignBlock.TOP_TRIM) && state.get(LargeIronSignBlock.LEFT_TRIM);
        innerCornerLeftTop = blockToTopLeft.isOf(LargeIronSignBlock.LARGE_IRON_SIGN_BLOCK) && blockToTopLeft.get(LargeIronSignBlock.BOTTOM_TRIM) && state.get(LargeIronSignBlock.LEFT_TRIM);
        topLeftIsClear = blockIsClear(blockToTopLeft);
        topIsClear = blockIsClear(blockToTop);
        topRightIsClear = blockIsClear(blockToTopRight);
        rightIsClear = blockIsClear(blockToRight);
        bottomRightIsClear = blockIsClear(blockToBottomRight);
        bottomIsClear = blockIsClear(blockToBottom);
        bottomLeftIsClear = blockIsClear(blockToBottomLeft);
        leftIsClear = blockIsClear(blockToLeft);
    }

    private boolean blockIsClear(BlockState blockState) {
        return blockState.isAir();
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
