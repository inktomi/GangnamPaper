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
            R.drawable.droid09,
            R.drawable.droid10,
            R.drawable.droid11,
            R.drawable.droid12,
            R.drawable.droid13,
            R.drawable.droid14,
            R.drawable.droid15,
            R.drawable.droid16,
            R.drawable.droid17,
            R.drawable.droid18,
            R.drawable.droid19,
            R.drawable.droid20,
            R.drawable.droid21,
            R.drawable.droid22,
            R.drawable.droid23,
            R.drawable.droid24,
            R.drawable.droid25,
            R.drawable.droid26,
            R.drawable.droid27,
            R.drawable.droid28,
            R.drawable.droid29,
            R.drawable.droid30,
            R.drawable.droid31,
            R.drawable.droid32,
            R.drawable.droid33,
            R.drawable.droid34,
            R.drawable.droid35,
            R.drawable.droid36,
            R.drawable.droid37,
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
