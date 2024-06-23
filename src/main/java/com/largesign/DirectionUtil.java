package com.largesign;

import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.util.math.Direction;

public class DirectionUtil {
	
	public static final Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
	};
	
	public Direction rotate(Direction direction, Rotation rotation) {
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

}
