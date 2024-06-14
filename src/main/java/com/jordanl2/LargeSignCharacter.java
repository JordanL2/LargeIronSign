package com.jordanl2;

import net.minecraft.util.StringIdentifiable;

public enum LargeSignCharacter implements StringIdentifiable {
	SPACE("space"),
	A("a");
	
	private final String name;

	private LargeSignCharacter(String name) {
		this.name = name;
	}
	
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
