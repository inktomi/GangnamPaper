package com.ignition.apps.gangnam.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class Shape {

    abstract public float[] getVertices();

    private FloatBuffer vertexBuffer;	// buffer holding the vertices
    private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
    private float[] texture = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f,		// top left		(V2)
            0.0f, 0.0f,		// bottom left	(V1)
            1.0f, 1.0f,		// top right	(V4)
            1.0f, 0.0f		// bottom right	(V3)
    };

    /** The texture pointer */
    private int[] textures;

    protected Shape() {
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

    public void draw(GL10 gl) {
        draw(gl, 0);
    }

    public void draw(GL10 gl, int textureIdx) {
        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureIdx]);

        // Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Set the face rotation
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, getVertices().length / 3);

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    public void loadGLTextures(GL10 gl, Context context, int... drawableResIds) {

        // initialize textures pointer
        textures = new int[drawableResIds.length];

        // generate one texture pointer
        gl.glGenTextures(drawableResIds.length, textures, 0);

        // load bitmaps
        for (int i = 0; i < drawableResIds.length; i++) {

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResIds[i]);

            // ...and bind it to our array
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);

            // create nearest filtered texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Clean up
            bitmap.recycle();
        }
    }
}
