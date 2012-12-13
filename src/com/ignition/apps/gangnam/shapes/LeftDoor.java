package com.ignition.apps.gangnam.shapes;

public class LeftDoor extends Door {

    private static float landscapeVertices[] = {
            -3.1f, -2.0f,  0.0f,    // V1 - bottom left
            -3.1f,  2.0f,  0.0f,	// V2 - top left
            0.0f, -2.0f,  0.0f,		// V3 - bottom right
            0.0f,  2.0f,  0.0f		// V4 - top right
    };

    private static float portraitVertices[] = {
            -2.5f, -2.0f,  0.0f,    // V1 - bottom left
            -2.5f,  2.0f,  0.0f,	// V2 - top left
            0.0f, -2.0f,  0.0f,		// V3 - bottom right
            0.0f,  2.0f,  0.0f		// V4 - top right
    };

    @Override
    public float[] getVertices() {
        return isLandscape() ? landscapeVertices : portraitVertices;
    }
}
