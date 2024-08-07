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
        
        int trimButtonWidth = 30;
        int trimButtonHeight = 20;
        
        trimButton = ButtonWidget.builder(Text.literal("Trim"), a -> {
                    if (trim) {
                        trim = false;
                        trimButton.setAlpha(0.5f);
                    } else {
                        trim = true;
                        trimButton.setAlpha(1f);
                    }
                    updateBlockEntity();
                }).dimensions(5, 5, trimButtonWidth, trimButtonHeight)
                .build();
        trimButton.setAlpha(trim ? 1f : 0.5f);
        addDrawableChild(trimButton);
        
        
        // Symbol buttons
        
        int buttonWidth = 20;
        int buttonHeight = 20;
        int space = 5;
        int minMargin = 40;
        
        int margin = minMargin + (((width - minMargin - minMargin - buttonWidth) % (buttonWidth + space)) / 2);
        int x = margin;
        
        int buttonsPerRow = ((width - minMargin - minMargin - buttonWidth) / (buttonWidth + space)) + 1;
        int rows = (int) Math.ceil((float) LargeIronSignCharacter.values().length / (float) buttonsPerRow);
        int y = minMargin + (height - minMargin - minMargin - buttonHeight - ((rows - 1) * (buttonHeight + space))) / 2;
        
        for (LargeIronSignCharacter character : LargeIronSignCharacter.values()) {
            ButtonWidget button = ButtonWidget.builder(Text.literal(character.getLabel()), a ->
                    setBlockChar(character))
                    .dimensions(x, y, buttonWidth, buttonHeight)
                    .tooltip(Tooltip.of(Text.literal("Set sign to " + character.getDescription())))
                    .build();
            addDrawableChild(button);
            
            x += buttonWidth + space;
            if (x + buttonWidth + margin > width) {
                x = margin;
                y += buttonHeight + space;
            }
        }
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
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}