/**
 * 
 */
package com.ignition.apps.gangnam.shapes;

/**
 * @author impaler
 *
 */
public class ElevatorInterior extends Shape {
	
    @Override
    public float[] getVertices() {
        return new float[] {
                    -1.0f, -2.0f,  0.0f,		// V1 - bottom left
                    -1.0f,  2.0f,  0.0f,		// V2 - top left
                    1.0f, -2.0f,  0.0f,		    // V3 - bottom right
                    1.0f,  2.0f,  0.0f			// V4 - top right
        };
    }

}
