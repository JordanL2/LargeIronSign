package com.jordanl2;

import net.minecraft.util.StringIdentifiable;

public enum LargeSignCharacter implements StringIdentifiable {
	SPACE("space", "Space"),
	A("a", "A");
	
	private final String name;
	private final String description;
	
	private static final LargeSignCharacter[] vals = values();

	private LargeSignCharacter(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public LargeSignCharacter getNext() {
    	return vals[(this.ordinal() + 1) % vals.length];
    }
}
