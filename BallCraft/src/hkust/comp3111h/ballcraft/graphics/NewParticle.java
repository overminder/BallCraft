package hkust.comp3111h.ballcraft.graphics;

import hkust.comp3111h.ballcraft.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class NewParticle implements Drawable {
    
    private static FloatBuffer vertexBuffer;
    private static FloatBuffer textureBuffer;
    
    private static float [] vertices = {
            -3, -3, 0, // 0, Top Left
            -3, 3, 0, // 1, Bottom Left
            3, -3, 0, // 3, Bottom Right
            3, 3, 0, // 3, Top Right
    };
    
    private static float[] texture = { 
            0.0f, 1.0f,     // top left     (V2)
            0.0f, 0.0f,     // bottom left  (V1)
            1.0f, 1.0f,     // top right    (V4)
            1.0f, 0.0f      // bottom right (V3)
    };
    
    private static int [] textures = new int[1];
    
    private float x, y, z;
    private float xSpeed, ySpeed, zSpeed;
    
    private static final float gravity = -0.2f;
    
    static {
        vertexBuffer = makeVertexBuffer();
        textureBuffer = makeTextureBuffer();
    }

    public NewParticle(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public NewParticle(float x, float y, float z, float xSpeed, float ySpeed, float zSpeed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }
    
    public void setSpeed(float xSpeed, float ySpeed, float zSpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }
    
    public void move() {
        if (this.z > 5) {
	        this.zSpeed += gravity;
	        this.x += this.xSpeed;
	        this.y += this.ySpeed;
	        this.z += this.zSpeed;
        }
    }
    
    @Override
    public void draw(GL10 gl) {
        gl.glPushMatrix();
        
	        gl.glTranslatef(x, y, z);
	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
	        
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        
	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
	        
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        
	        gl.glDisable(GL10.GL_TEXTURE_2D);
        
	    gl.glPopMatrix();
    }
    
    private static FloatBuffer makeVertexBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(vertices);
        buffer.position(0);
        return buffer;
    }
    
    private static FloatBuffer makeTextureBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(texture.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(texture);
        buffer.position(0);
        return buffer;
    }

    public static void loadTexture(GL10 gl, Context context) {
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.particle_tex);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0); 
		bmp.recycle();
    }

}