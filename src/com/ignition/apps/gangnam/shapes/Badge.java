package com.ignition.apps.gangnam.shapes;

import com.ignition.apps.gangnam.R;

public class Badge extends Shape {

    private static final float[] VERTICES = {
            -1.0f, -1.0f,  0.0f,		// V1 - bottom left
            -1.0f,  1.0f,  0.0f,		// V2 - top left
            1.0f, -1.0f,  0.0f,		    // V3 - bottom right
            1.0f,  1.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {R.drawable.badge_sms};

    @Override
    public float[] getVertices() {
        return VERTICES;
    }

    @Override
    public int[] getTextures() {
        return TEXTURES;
    }
}
