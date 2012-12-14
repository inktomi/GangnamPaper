package com.ignition.apps.zangnam.shapes;

import com.ignition.apps.zangnam.R;

public class LeftDoor extends Door {

    private static final float[] LANDSCAPE_VERTICES = {
            -4.0f, -2.0f,  0.0f,    // V1 - bottom left
            -4.0f,  2.0f,  0.0f,	// V2 - top left
            0.0f, -2.0f,  0.0f,		// V3 - bottom right
            0.0f,  2.0f,  0.0f		// V4 - top right
    };

    private static final float[] PORTRAIT_VERTICES = {
            -4.0f, -2.0f,  0.0f,    // V1 - bottom left
            -4.0f,  2.0f,  0.0f,	// V2 - top left
            0.0f, -2.0f,  0.0f,		// V3 - bottom right
            0.0f,  2.0f,  0.0f		// V4 - top right
    };

    private static final int[] TEXTURES = {R.drawable.door_left};

    @Override
    public float[] getVertices() {
        return isLandscape() ? LANDSCAPE_VERTICES : PORTRAIT_VERTICES;
    }

    @Override
    public int[] getTextures() {
        return TEXTURES;
    }
}
