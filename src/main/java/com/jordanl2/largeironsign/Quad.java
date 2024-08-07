package com.jordanl2.largeironsign;

import net.minecraft.data.client.VariantSettings;

public class Quad {
    
    public float left;
    public float bottom;
    public float right;
    public float top;
    
    public static final float ROTATE_X = 0.5f;
    public static final float ROTATE_Y = 0.5f;
    
    public Quad(final float left, final float bottom, final float right, final float top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }
    
    public void rotate(final VariantSettings.Rotation rotation) {
        switch (rotation) {
            case R0 -> rotate(0);
            case R90 -> rotate(1);
            case R180 -> rotate(2);
            case R270 -> rotate(3);
        }
    }
    
    private void rotate(final int clockwiseRotations) {
        for (int i = 0; i < clockwiseRotations; i++) {
            float normalisedLeft = left - ROTATE_X;
            float normalisedBottom = bottom - ROTATE_Y;
            float normalisedRight = right - ROTATE_X;
            float normalisedTop = top - ROTATE_Y;
            
            top = (0f - normalisedLeft) + ROTATE_Y;
            right = normalisedTop + ROTATE_X;
            bottom = (0f - normalisedRight) + ROTATE_Y;
            left = normalisedBottom + ROTATE_X;
        }
    }
    
}
