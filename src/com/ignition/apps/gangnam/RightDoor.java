package com.ignition.apps.gangnam;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RightDoor extends Door {
    private FloatBuffer vertexBuffer;
    private float landscapeVertices[] = {
            0.0f, -2.0f,  0.0f,    // V1 - bottom left
            0.0f,  2.0f,  0.0f,	// V2 - top left
            3.1f, -2.0f,  0.0f,		// V3 - bottom right
            3.1f,  2.0f,  0.0f		// V4 - top right
    };

    private float portraitVertices[] = {
            0.0f, -2.0f,  0.0f,    // V1 - bottom left
            0.0f,  2.0f,  0.0f,	// V2 - top left
            2.5f, -2.0f,  0.0f,		// V3 - bottom right
            2.5f,  2.0f,  0.0f		// V4 - top right
    };

    public RightDoor() {
        // a float has 4 bytes so we allocate for each coordinate 4 bytes
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(getVertices().length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        // allocates the memory from the byte buffer
        vertexBuffer = byteBuffer.asFloatBuffer();

        // fill the vertexBuffer with the vertices
        vertexBuffer.put(getVertices());

        // set the cursor position to the beginning of the buffer
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    @Override
    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    @Override
    public float[] getVertices() {
        return isLandscape() ? landscapeVertices : portraitVertices;
    }
}
