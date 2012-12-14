/**
 * 
 */
package com.ignition.apps.zangnam.shapes;

import com.ignition.apps.zangnam.R;

/**
 * @author impaler
 *
 */
public class ElevatorInterior extends Shape {

    private static final float[] VERTICES = {
            -1.0f, -2.0f,  0.0f,		// V1 - bottom left
            -1.0f,  2.0f,  0.0f,		// V2 - top left
            1.0f, -2.0f,  0.0f,		    // V3 - bottom right
            1.0f,  2.0f,  0.0f			// V4 - top right
    };

    private static final int[] TEXTURES = {
            R.drawable.e42,
            R.drawable.e43,
            R.drawable.e44,
            R.drawable.e45,
            R.drawable.e46,
            R.drawable.e47,
            R.drawable.e48,
            R.drawable.e49,
            R.drawable.e50,
            R.drawable.e51,
            R.drawable.e52,
            R.drawable.e53,
            R.drawable.e54,
            R.drawable.e55,
            R.drawable.e56,
            R.drawable.e57,
            R.drawable.e58,
            R.drawable.e59,
            R.drawable.e60,
            R.drawable.e61,
            R.drawable.e62,
            R.drawable.e63,
            R.drawable.e64,
            R.drawable.e65,
            R.drawable.e66,
            R.drawable.e67,
            R.drawable.e68,
            R.drawable.e69,
            R.drawable.e70,
            R.drawable.e71,
            R.drawable.e72,
            R.drawable.e73,
            R.drawable.e74,
            R.drawable.e75,
            R.drawable.e76,
            R.drawable.e77,
            R.drawable.e78,
            R.drawable.e79,
            R.drawable.e80,
            R.drawable.e81,
            R.drawable.e82,
            R.drawable.e83,
            R.drawable.e84,
            R.drawable.e85,
            R.drawable.e86,
            R.drawable.e87,
            R.drawable.e88,
            R.drawable.e89,
            R.drawable.e90,
            R.drawable.e91,
            R.drawable.e92,
            R.drawable.e93,
            R.drawable.e94,
            R.drawable.e95,
            R.drawable.e96,
            R.drawable.e97,
            R.drawable.e98,
            R.drawable.e99,
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
