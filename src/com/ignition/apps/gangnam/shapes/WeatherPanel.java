package com.ignition.apps.gangnam.shapes;

import com.ignition.apps.gangnam.R;

public class WeatherPanel extends Shape {
    private static final float[] VERTICES = {
            -1.0f, 1.0f,  0.0f,		// V1 - bottom left
            -1.0f,  2.0f,  0.0f,		// V2 - top left
            1.0f, 1.0f,  0.0f,		    // V3 - bottom right
            1.0f,  2.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {R.drawable.weather_bg};

    @Override
    public float[] getVertices() {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int[] getTextures() {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
