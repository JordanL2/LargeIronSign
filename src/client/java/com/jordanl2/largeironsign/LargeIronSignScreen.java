package com.jordanl2.largeironsign;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class LargeIronSignScreen extends Screen {

	private BlockPos pos;
	private ClientWorld world;
	private LargeIronSignBlockEntity blockEntity;

	private List<ButtonWidget> buttons;
	private ButtonWidget topEdgeButton;
	private ButtonWidget rightEdgeButton;
	private ButtonWidget bottomEdgeButton;
	private ButtonWidget leftEdgeButton;

	protected LargeIronSignScreen(BlockPos pos) {
		super(Text.literal("Change Sign Symbol"));
		this.pos = pos;
	}

	@Override
	protected void init() {
		super.init();

		world = client.world;
		BlockEntity blockEntity1 = world.getBlockEntity(pos);
		if (blockEntity1 instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
			blockEntity = largeIronSignBlockEntity;
		} else {
			this.close();
		}


		// Edge Buttons

		int edgeButtonWidth = 30;
		int edgeButtonHeight = 20;

		topEdgeButton = ButtonWidget.builder(Text.literal((blockEntity.edges & LargeIronSignBlockEntity.TOP_EDGE) > 0 ? "On" : "Off"), a -> {
				if ((blockEntity.edges & LargeIronSignBlockEntity.TOP_EDGE) > 0) {
					blockEntity.edges -= LargeIronSignBlockEntity.TOP_EDGE;
					topEdgeButton.setMessage(Text.literal("Off"));
				} else {
					blockEntity.edges |= LargeIronSignBlockEntity.TOP_EDGE;
					topEdgeButton.setMessage(Text.literal("On"));
				}
				updateBlockEntity();
			}).dimensions(width / 2 - (edgeButtonWidth / 2), 0, edgeButtonWidth, edgeButtonHeight)
			.build();
		addDrawableChild(topEdgeButton);

		rightEdgeButton = ButtonWidget.builder(Text.literal((blockEntity.edges & LargeIronSignBlockEntity.RIGHT_EDGE) > 0 ? "On" : "Off"), a -> {
					if ((blockEntity.edges & LargeIronSignBlockEntity.RIGHT_EDGE) > 0) {
						blockEntity.edges -= LargeIronSignBlockEntity.RIGHT_EDGE;
						rightEdgeButton.setMessage(Text.literal("Off"));
					} else {
						blockEntity.edges |= LargeIronSignBlockEntity.RIGHT_EDGE;
						rightEdgeButton.setMessage(Text.literal("On"));
					}
					updateBlockEntity();
				}).dimensions(width - edgeButtonWidth, height / 2 - (edgeButtonHeight / 2), edgeButtonWidth, edgeButtonHeight)
				.build();
		addDrawableChild(rightEdgeButton);

		bottomEdgeButton = ButtonWidget.builder(Text.literal((blockEntity.edges & LargeIronSignBlockEntity.BOTTOM_EDGE) > 0 ? "On" : "Off"), a -> {
					if ((blockEntity.edges & LargeIronSignBlockEntity.BOTTOM_EDGE) > 0) {
						blockEntity.edges -= LargeIronSignBlockEntity.BOTTOM_EDGE;
						bottomEdgeButton.setMessage(Text.literal("Off"));
					} else {
						blockEntity.edges |= LargeIronSignBlockEntity.BOTTOM_EDGE;
						bottomEdgeButton.setMessage(Text.literal("On"));
					}
					updateBlockEntity();
				}).dimensions(width / 2 - (edgeButtonWidth / 2), height - edgeButtonHeight, edgeButtonWidth, edgeButtonHeight)
				.build();
		addDrawableChild(bottomEdgeButton);

		leftEdgeButton = ButtonWidget.builder(Text.literal((blockEntity.edges & LargeIronSignBlockEntity.LEFT_EDGE) > 0 ? "On" : "Off"), a -> {
					if ((blockEntity.edges & LargeIronSignBlockEntity.LEFT_EDGE) > 0) {
						blockEntity.edges -= LargeIronSignBlockEntity.LEFT_EDGE;
						leftEdgeButton.setMessage(Text.literal("Off"));
					} else {
						blockEntity.edges |= LargeIronSignBlockEntity.LEFT_EDGE;
						leftEdgeButton.setMessage(Text.literal("On"));
					}
					updateBlockEntity();
				}).dimensions(0, height / 2 - (edgeButtonHeight / 2), edgeButtonWidth, edgeButtonHeight)
				.build();
		addDrawableChild(leftEdgeButton);


		// Symbol buttons

		buttons = new ArrayList<>();
		int buttonWidth = 20;
		int buttonHeight = 20;
		int space = 5;
		int minMargin = 40;
		
		int margin = minMargin + (((width - minMargin - minMargin - buttonWidth) % (buttonWidth + space)) / 2);
		int x = margin;
		
		int buttonsPerRow = ((width - minMargin - minMargin - buttonWidth) / (buttonWidth + space)) + 1;
		int rows = (int) Math.ceil((float)LargeIronSignCharacter.values().length / (float)buttonsPerRow);
		int y = minMargin + (height - minMargin - minMargin - buttonHeight - ((rows - 1) * (buttonHeight + space))) / 2;
		
		for (LargeIronSignCharacter character : LargeIronSignCharacter.values()) {
			ButtonWidget button = ButtonWidget.builder(Text.literal(character.getLabel()), a -> {
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

	private void setBlockChar(LargeIronSignCharacter character) {
		blockEntity.character = character;
		updateBlockEntity();

		this.close();
	}

	private void updateBlockEntity() {
		// Trigger client side update
		BlockState blockState = world.getBlockState(pos);
		world.updateListeners(pos, blockState, blockState, Block.NOTIFY_LISTENERS);

		// Sync to server
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		buf.writeString(blockEntity.character.name());
		buf.writeInt(blockEntity.edges);
		ClientPlayNetworking.send(LargeIronSignBlock.LARGE_IRON_SIGN_SET_SYMBOL_PACKET_ID, buf);
	}

	@Override
    public boolean shouldPause() {
        return false;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    	renderBackground(context);
    	super.render(context, mouseX, mouseY, delta);
    }
}