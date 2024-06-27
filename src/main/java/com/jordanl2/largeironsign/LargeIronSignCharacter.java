package com.jordanl2.largeironsign;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum LargeIronSignCharacter implements StringIdentifiable {
	SPACE("space", " ", "Space"),
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
	KEY_M("m", "M"),
	KEY_N("n", "N"),
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
	KEY_9("9", "9"),

	CHR_46("chr46", "."),
	CHR_44("chr44", ","),
	CHR_59("chr59", ";"),
	CHR_58("chr58", ":"),
	CHR_63("chr63", "?"),
	CHR_191("chr191", "¿"),
	CHR_33("chr33", "!"),
	CHR_161("chr161", "¡"),
	CHR_39("chr39", "'"),
	CHR_34("chr34", "\""),

	CHR_40("chr40", "("),
	CHR_41("chr41", ")"),
	CHR_91("chr91", "["),
	CHR_93("chr93", "]"),
	CHR_123("chr123", "{"),
	CHR_125("chr125", "}"),

	CHR_37("chr37", "%"),
	CHR_42("chr42", "*"),
	CHR_43("chr43", "+"),
	CHR_45("chr45", "-"),
	CHR_47("chr47", "/"),
	CHR_92("chr92", "\\"),
	CHR_61("chr61", "="),
	CHR_60("chr60", "<"),
	CHR_62("chr62", ">"),

	CHR_35("chr35", "#"),
	CHR_38("chr38", "&"),
	CHR_64("chr64", "@"),
	CHR_124("chr124", "|"),
	CHR_126("chr126", "~"),
	CHR_169("chr169", "©"),
	CHR_174("chr174", "®"),
	CHR_8482("chr8482", "™"),
	
	CHR_36("chr36", "$"),
	CHR_163("chr163", "£"),
	CHR_8364("chr8364", "€");
	
	private final String name;
	private final String label;
	private final String description;
	
	private LargeIronSignCharacter(String name, String label) {
		this.name = name;
		this.label = label;
		this.description = label;
	}
	
	private LargeIronSignCharacter(String name, String label, String description) {
		this.name = name;
		this.label = label;
		this.description = description;
	}
	
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
    
    public String getLabel() {
    	return label;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public Identifier getIdentifier() {
    	return new Identifier(LargeIronSign.MOD_ID, LargeIronSignBlock.PATH + this.getSuffix());
    }
    
    public Identifier getBlockTextureIdentifier() {
    	return new Identifier(LargeIronSign.MOD_ID, LargeIronSignBlock.BLOCK_PATH + this.getSuffix());
    }
    
    public String getSuffix() {
    	return "_" + this.asString();
    }
    
    public Identifier getPath() {
    	return new Identifier(LargeIronSign.MOD_ID, "block/" + LargeIronSignBlock.PATH + "_" + this.asString());
    }
}
