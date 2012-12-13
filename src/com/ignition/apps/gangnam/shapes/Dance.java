package com.ignition.apps.gangnam.shapes;

import com.ignition.apps.gangnam.R;

public class Dance extends Shape {

    private static final float[] VERTICES = {
            -0.5f, -1.0f,  0.0f,		// V1 - bottom left
            -0.5f,  1.0f,  0.0f,		// V2 - top left
            0.5f, -1.0f,  0.0f,		    // V3 - bottom right
            0.5f,  1.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {
            R.drawable.dance1_01,
            R.drawable.dance1_02,
            R.drawable.dance1_03,
            R.drawable.dance1_04,
            R.drawable.dance1_05,
            R.drawable.dance1_06,
            R.drawable.dance1_07,
            R.drawable.dance1_08,
            R.drawable.dance1_09,
            R.drawable.dance1_10,
            R.drawable.dance1_11,
            R.drawable.dance1_12,
            R.drawable.dance1_13,
            R.drawable.dance1_14,
            R.drawable.dance1_15,
            R.drawable.dance1_16,
            R.drawable.dance1_17,
            R.drawable.dance1_18,
            R.drawable.dance1_19,
            R.drawable.dance1_20,
            R.drawable.dance1_21
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
