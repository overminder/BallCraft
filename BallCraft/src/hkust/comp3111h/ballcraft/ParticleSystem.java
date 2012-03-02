package hkust.comp3111h.ballcraft;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author guanlun
 */
class ParticleSystem {
	private Particle [] mParticles;
	private int COUNT = 200;
	
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	
	public ParticleSystem() {
		mParticles = new Particle [COUNT];
		Random gen = new Random(System.currentTimeMillis());
		for (int i = 0; i < COUNT; i++) {
			mParticles[i] = new Particle(gen);
		}
		
		float [] coords = {
				-0.1f, 0.0f, 0.0f, 
                0.1f, 0.0f, 0.0f, 
                0.0f, .0f, 0.1f
        };
		
		short [] icoords = {0, 1, 2};
		mVertexBuffer = makeFloatBuffer(coords);
		mIndexBuffer = makeShortBuffer(icoords);
	}
	
	private FloatBuffer makeFloatBuffer(float [] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}
	
	private ShortBuffer makeShortBuffer(short [] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer sb = bb.asShortBuffer();
		sb.put(arr);
		sb.position(0);
		return sb;
	}
	
	public void move() {
		for (int i = 0; i < COUNT; i++) {
			mParticles[i].move(1);
		}
	}
	
	public void draw(GL10 gl) {
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < COUNT; i++) {
			gl.glPushMatrix();
			gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
			gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
			// gl.glDrawElements(GL10.GL_POINTS, 1, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
			gl.glPopMatrix();
		}
	}
}