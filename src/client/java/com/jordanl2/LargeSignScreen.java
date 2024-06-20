package com.jordanl2;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class LargeSignScreen extends Screen {
	
	private Identifier worldValue;
	private BlockPos pos;

	protected LargeSignScreen(Identifier worldValue, BlockPos pos) {
		super(Text.literal("Change Sign Symbol"));
		this.worldValue = worldValue;
		this.pos = pos;
	}

	public ButtonWidget buttonA;
	public ButtonWidget buttonB;

	@Override
	protected void init() {
		buttonA = ButtonWidget.builder(Text.literal("A"), button -> {
			setBlockChar(LargeSignCharacter.KEY_A);
		})
				.dimensions(width / 2 - 205, 20, 200, 20)
				.tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
				.build();
		buttonB = ButtonWidget.builder(Text.literal("B"), button -> {
			setBlockChar(LargeSignCharacter.KEY_B);
		})
				.dimensions(width / 2 + 5, 20, 200, 20)
				.tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
				.build();

		addDrawableChild(buttonA);
		addDrawableChild(buttonB);
	}
	
	private void setBlockChar(LargeSignCharacter character) {
		System.out.println(
				"Setting block " + pos.getX() + "," + pos.getY() + "," + pos.getZ()
				+ " to " + character.getDescription());
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(worldValue);
		buf.writeBlockPos(pos);
		buf.writeString(character.name());
		ClientPlayNetworking.send(LargeSignBlock.LARGE_SIGN_SET_SYMBOL_PACKET_ID, buf);
		this.close();
	}
}