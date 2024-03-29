package com.zappos.android.zangnam.shapes;

import com.zappos.android.zangnam.R;

public class DanceInterior extends Shape {

    private static final float[] VERTICES = {
            -2.0f, -2.0f,  0.0f,		// V1 - bottom left
            -2.0f,  2.0f,  0.0f,		// V2 - top left
            2.0f, -2.0f,  0.0f,		    // V3 - bottom right
            2.0f,  2.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {
            R.drawable.dancebg01,
            R.drawable.dancebg02
    };

    @Override
    public float[] getVertices() {
        return VERTICES;
    }

    @Override
    public int[] getTextures() {
        return TEXTURES;
    }
}
