package com.largesign;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.util.math.Direction;

public class DirectionUtil {
	
	private final List<Map.Entry<Direction, VariantSettings.Rotation>> directions = new ArrayList<>();
	
	public DirectionUtil() {
		directions.add(new AbstractMap.SimpleEntry<Direction, Rotation>(Direction.NORTH, VariantSettings.Rotation.R0));
		directions.add(new AbstractMap.SimpleEntry<Direction, Rotation>(Direction.EAST, VariantSettings.Rotation.R90));
		directions.add(new AbstractMap.SimpleEntry<Direction, Rotation>(Direction.SOUTH, VariantSettings.Rotation.R180));
		directions.add(new AbstractMap.SimpleEntry<Direction, Rotation>(Direction.WEST, VariantSettings.Rotation.R270));
	}
	
	public List<Map.Entry<Direction, VariantSettings.Rotation>> getDirections() {
		return directions;
	}
	
	public Direction rotate(Direction direction, Rotation rotation) {
		int i = 0;
		while (directions.get(i).getKey() != direction) {
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
		return directions.get(i).getKey();
	}

}
