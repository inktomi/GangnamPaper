package com.ignition.apps.zangnam.shapes;

import com.ignition.apps.zangnam.R;

public class Dance extends Shape {

    private static final float[] VERTICES = {
            -0.5f, -1.0f,  0.0f,		// V1 - bottom left
            -0.5f,  1.0f,  0.0f,		// V2 - top left
            0.5f, -1.0f,  0.0f,		    // V3 - bottom right
            0.5f,  1.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {
            R.drawable.droid01,
            R.drawable.droid02,
            R.drawable.droid03,
            R.drawable.droid04,
            R.drawable.droid05,
            R.drawable.droid06,
            R.drawable.droid07,
            R.drawable.droid08,
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
