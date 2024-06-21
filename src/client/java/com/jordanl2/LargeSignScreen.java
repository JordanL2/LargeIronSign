package com.jordanl2;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
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

	public List<ButtonWidget> buttons;

	@Override
	protected void init() {
		super.init();
		
		buttons = new ArrayList<>();
		int buttonWidth = 40;
		int buttonHeight = 20;
		int space = 10;
		int minMargin = 40;
		
		int margin = minMargin + (((width - minMargin - minMargin - buttonWidth) % (buttonWidth + space)) / 2);
		int x = margin;
		
		int buttonsPerRow = ((width - minMargin - minMargin - buttonWidth) / (buttonWidth + space)) + 1;
		int rows = (int) Math.ceil((float)LargeSignCharacter.values().length / (float)buttonsPerRow);
		int y = minMargin + (height - minMargin - minMargin - buttonHeight - ((rows - 1) * (buttonHeight + space))) / 2;
		
		for (LargeSignCharacter character : LargeSignCharacter.values()) {
			ButtonWidget button = ButtonWidget.builder(Text.literal(character.getDescription()), a -> {
				setBlockChar(character);
			})
					.dimensions(x, y, buttonWidth, buttonHeight)
					.tooltip(Tooltip.of(Text.literal("Set sign to " + character.getDescription())))
					.build();
			buttons.add(button);
			addDrawableChild(button);
			
			x += buttonWidth + space;
			if (x + buttonWidth + margin > width) {
				x = margin;
				y += buttonHeight + space;
			}
		}
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

    public boolean shouldPause() {
        return false;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    	renderBackground(context);
    	super.render(context, mouseX, mouseY, delta);
    }
}