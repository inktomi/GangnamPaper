package com.ignition.apps.gangnam.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLUtils;
import com.ignition.apps.gangnam.R;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class WeatherPanel {
    private static final float[] VERTICES = {
            -2.0f, 0.0f,  0.0f,		// V1 - bottom left
            -2.0f,  0.5f,  0.0f,		// V2 - top left
            2.0f, 0.0f,  0.0f,		    // V3 - bottom right
            2.0f,  0.5f,  0.0f			// V4 - top right
    };

    private float[] texture = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f,		// top left		(V2)
            0.0f, 0.0f,		// bottom left	(V1)
            1.0f, 1.0f,		// top right	(V4)
            1.0f, 0.0f		// bottom right	(V3)
    };

    private Bitmap generatedBitmap;

    private int[] textures;
    private static final int[] TEXTURES = {R.drawable.weather_bg};

    private FloatBuffer vertexBuffer = null;	// buffer holding the vertices
    private FloatBuffer textureBuffer = null;	// buffer holding the texture coordinates

    public WeatherPanel(Context context, String weatherBlurb) {
        // a float has 4 bytes so we allocate for each coordinate 4 bytes
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(getVertices().length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        if( null == vertexBuffer ){

            // allocates the memory from the byte buffer
            vertexBuffer = byteBuffer.asFloatBuffer();

            // fill the vertexBuffer with the vertices
            vertexBuffer.put(getVertices());

            // set the cursor position to the beginning of the buffer
            vertexBuffer.position(0);
        }

        if( null == textureBuffer ){
            byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            textureBuffer = byteBuffer.asFloatBuffer();
            textureBuffer.put(texture);
            textureBuffer.position(0);
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inMutable = true;
        generatedBitmap = BitmapFactory.decodeResource(context.getResources(), getTextures()[0], o);

        Canvas canvas = new Canvas(generatedBitmap);
        generatedBitmap.eraseColor(0);

        // get a background image from resources
        // note the image format must match the bitmap format
        Drawable background = context.getResources().getDrawable(R.drawable.weather_bg);
        background.setBounds(0, 0, generatedBitmap.getWidth(), generatedBitmap.getHeight());
        background.draw(canvas); // draw the background to our bitmap

        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(12);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0x00, 0x00, 0x00);

        // draw the text centered
        canvas.drawText(weatherBlurb, textPaint.measureText(weatherBlurb)/3, generatedBitmap.getHeight()/2, textPaint);
    }

    public float[] getVertices() {
        return VERTICES;
    }

    public int[] getTextures() {
        return TEXTURES;
    }

    public void draw(GL10 gl, Context context) {
        // initialize textures pointer
        textures = new int[getTextures().length];

        // Enable blending using premultiplied alpha.
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //Generate one texture pointer...
        gl.glGenTextures(1, textures, 0);

        //...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        //Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, generatedBitmap, 0);

        //Clean up
        generatedBitmap.recycle();

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

}
