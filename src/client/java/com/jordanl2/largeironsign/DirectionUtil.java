package com.jordanl2.largeironsign;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class DirectionUtil {
    
    public static final Direction[] DIRECTIONS = new Direction[]{
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
    };
    
    public static final BlockRotation[] ROTATIONS = new BlockRotation[]{
            BlockRotation.NONE,
            BlockRotation.CLOCKWISE_90,
            BlockRotation.CLOCKWISE_180,
            BlockRotation.COUNTERCLOCKWISE_90
    };
    
    public Direction rotate(final Direction direction, final BlockRotation rotation) {
        int i = 0;
        while (DIRECTIONS[i] != direction) {
            i++;
        }
        
        switch (rotation) {
            case CLOCKWISE_90:
                i += 1;
                break;
            case CLOCKWISE_180:
                i += 2;
                break;
            case COUNTERCLOCKWISE_90:
                i += 3;
                break;
            case NONE:
            default:
                break;
        }
        
        i = i % 4;
        return DIRECTIONS[i];
    }
    
    public BlockRotation getRotation(final Direction direction1, final Direction direction2) {
        int i = 0;
        while (DIRECTIONS[i] != direction1) {
            i++;
        }
        
        int j = 0;
        while (DIRECTIONS[j] != direction2) {
            j++;
        }
        
        int r = (j + 4 - i) % 4;
        return ROTATIONS[r];
    }
    
}
