package com.ignition.apps.gangnam.shapes;

public class RightDoor extends Door {

    private static float landscapeVertices[] = {
            0.0f, -2.0f,  0.0f,     // V1 - bottom left
            0.0f,  2.0f,  0.0f,	    // V2 - top left
            3.1f, -2.0f,  0.0f,		// V3 - bottom right
            3.1f,  2.0f,  0.0f		// V4 - top right
    };

    private static float portraitVertices[] = {
            0.0f, -2.0f,  0.0f,     // V1 - bottom left
            0.0f,  2.0f,  0.0f,	    // V2 - top left
            2.5f, -2.0f,  0.0f,		// V3 - bottom right
            2.5f,  2.0f,  0.0f		// V4 - top right
    };

    @Override
    public float[] getVertices() {
        return isLandscape() ? landscapeVertices : portraitVertices;
    }
}
