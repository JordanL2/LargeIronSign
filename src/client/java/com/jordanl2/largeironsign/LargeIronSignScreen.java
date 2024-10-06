package com.jordanl2.largeironsign;

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
    
    private final BlockPos pos;
    private ClientWorld world;
    private LargeIronSignBlockEntity blockEntity;
    private boolean trim;
    
    private ButtonWidget trimButton;
    
    protected LargeIronSignScreen(final BlockPos pos) {
        super(Text.literal("Change Sign Symbol"));
        this.pos = pos;
    }
    
    @Override
    protected void init() {
        super.init();
        
        assert client != null;
        world = client.world;
        assert world != null;
        BlockState blockState = world.getBlockState(pos);
        trim = blockState.get(LargeIronSignBlock.TRIM);
        BlockEntity blockEntity1 = world.getBlockEntity(pos);
        if (blockEntity1 instanceof LargeIronSignBlockEntity largeIronSignBlockEntity) {
            blockEntity = largeIronSignBlockEntity;
        } else {
            this.close();
        }
        
        
        // Trim Button
        
        int trimButtonWidth = 50;
        int trimButtonHeight = 20;
        
        trimButton = ButtonWidget.builder(Text.literal(trimButtonText()), a -> {
                    trim = !trim;
                    trimButton.setMessage(Text.literal(trimButtonText()));
                    updateBlockEntity();
                }).dimensions(5, 5, trimButtonWidth, trimButtonHeight)
                .build();
        addDrawableChild(trimButton);
        
        
        // Symbol buttons
        
        int buttonWidth = 20;
        int buttonHeight = 20;
        int space = 3;
        int newLineSpace = 10;
        int minMargin = 40;
        
        int margin = minMargin + (((width - minMargin - minMargin - buttonWidth) % (buttonWidth + space)) / 2);
        int x = margin;
        int y = minMargin;

        boolean first = true;
        for (LargeIronSignCharacter character : LargeIronSignCharacter.values()) {
            if (!first) {
                if (character.isNewLine()) {
                    x = margin;
                    y += buttonHeight + newLineSpace;
                } else {
                    x += buttonWidth + space;
                    if (x + buttonWidth + margin > width) {
                        x = margin;
                        y += buttonHeight + space;
                    }
                }
            }
            first = false;

            ButtonWidget button = ButtonWidget.builder(Text.literal(character.getLabel()), a ->
                    setBlockChar(character))
                    .dimensions(x, y, buttonWidth, buttonHeight)
                    .tooltip(Tooltip.of(Text.literal("Set sign to " + character.getDescription())))
                    .build();
            addDrawableChild(button);
        }
    }
    
    private String trimButtonText() {
        return trim ? "Trim On" : "Trim Off";
    }
    
    private void setBlockChar(final LargeIronSignCharacter character) {
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
        buf.writeBoolean(trim);
        ClientPlayNetworking.send(LargeIronSignBlock.LARGE_IRON_SIGN_SET_SYMBOL_PACKET_ID, buf);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
    
    @Override
    public void render(final DrawContext context, final int mouseX, final int mouseY, final float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}