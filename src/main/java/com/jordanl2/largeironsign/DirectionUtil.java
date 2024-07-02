package com.jordanl2.largeironsign;

import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.util.math.Direction;

public class DirectionUtil {
    
    public static final Direction[] DIRECTIONS = new Direction[]{
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
    };
    
    public static final Rotation[] ROTATIONS = new Rotation[]{
            Rotation.R0,
            Rotation.R90,
            Rotation.R180,
            Rotation.R270
    };
    
    public Direction rotate(final Direction direction, final Rotation rotation) {
        int i = 0;
        while (DIRECTIONS[i] != direction) {
            i++;
        }
        
        switch (rotation) {
            case R90:
                i += 1;
                break;
            case R180:
                i += 2;
                break;
            case R270:
                i += 3;
                break;
            case R0:
            default:
                break;
        }
        
        i = i % 4;
        return DIRECTIONS[i];
    }
    
    public Rotation getRotation(final Direction direction1, final Direction direction2) {
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
