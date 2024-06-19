package com.jordanl2;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum LargeSignCharacter implements StringIdentifiable {
	SPACE("space", "Space"),
	KEY_A("a", "A"),
	KEY_B("b", "B"),
	KEY_C("c", "C"),
	KEY_D("d", "D"),
	KEY_E("e", "E"),
	KEY_F("f", "F"),
	KEY_G("g", "G"),
	KEY_H("h", "H"),
	KEY_I("i", "I"),
	KEY_J("j", "J"),
	KEY_K("k", "K"),
	KEY_L("l", "L"),
	KEY_M("n", "N"),
	KEY_O("o", "O"),
	KEY_P("p", "P"),
	KEY_Q("q", "Q"),
	KEY_R("r", "R"),
	KEY_S("s", "S"),
	KEY_T("t", "T"),
	KEY_U("u", "U"),
	KEY_V("v", "V"),
	KEY_W("w", "W"),
	KEY_X("x", "X"),
	KEY_Y("y", "Y"),
	KEY_Z("z", "Z"),
	KEY_0("0", "0"),
	KEY_1("1", "1"),
	KEY_2("2", "2"),
	KEY_3("3", "3"),
	KEY_4("4", "4"),
	KEY_5("5", "5"),
	KEY_6("6", "6"),
	KEY_7("7", "7"),
	KEY_8("8", "8"),
	KEY_9("9", "9");
	
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
    
    public Identifier getIdentifier() {
    	return new Identifier("jordanl2", "large_sign_" + this.asString());
    }
    
    public String getSuffix() {
    	return "_" + this.asString();
    }
    
    public Identifier getPath() {
    	return new Identifier("jordanl2", "block/large_sign_" + this.asString());
    }
    
    public LargeSignCharacter getNext() {
    	return vals[(this.ordinal() + 1) % vals.length];
    }
}
