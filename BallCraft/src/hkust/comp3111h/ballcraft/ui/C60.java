package hkust.comp3111h.ballcraft.ui;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class C60 {
	
	private float [] vertices = {
			-100.0f, 0.0f, -273.6068f,
			-30.9017f,      -95.1057f, -273.6068f,
			-30.9017f,      -95.1057f, -273.6068f,
			80.9017f,      -58.7785f, -273.6068f,
			80.9017f,      -58.7785f, -273.6068f,
			80.9017f,      58.7785f, -273.6068f,
			80.9017f,      58.7785f, -273.6068f,
			-30.9017f,      95.1057f, -273.6068f,
			
			-200.0000f, 0.0000f, -211.8034f,
			-230.9017f, 95.1057f, -150.0000f,
			-230.9017f, 95.1057f, -150.0000f,
			-280.9017f, 58.7785f, -50.0000f,
			-280.9017f, 58.7785f, -50.0000f,
			-280.9017f, -58.7785f, -50.0000f,
			-280.9017f, -58.7785f, -50.0000f,
			-230.9017f, -95.1057f, -150.0000f,
			
			-61.8034f,      -190.2113f, -211.8034f,
			-161.8034f, -190.2113f, -150.0000f,
			-161.8034f, -190.2113f, -150.0000f,
			-142.7051f, -248.9898f, -50.0000f,
			-142.7051f, -248.9898f, -50.0000f,
			-30.9017f,      -285.3170f, -50.0000f,
			-30.9017f,      -285.3170f, -50.0000f,
			19.0983f,      -248.9898f, -150.0000f,
			
			161.8034f,      -117.5571f, -211.8034f,
			130.9017f,      -212.6627f, -150.0000f,
			130.9017f,      -212.6627f, -150.0000f,
			192.7051f,      -212.6627f, -50.0000f,
			192.7051f,      -212.6627f, -50.0000f,
			261.8034f,      -117.5571f, -50.0000f,
			261.8034f,      -117.5571f, -50.0000f,
			242.7051f,      -58.7785f, -150.0000f,
			
			161.8034f,      117.5571f, -211.8034f,
			242.7051f,      58.7785f, -150.0000f,
			242.7051f,      58.7785f, -150.0000f,
			261.8034f,      117.5571f, -50.0000f,
			261.8034f,      117.5571f, -50.0000f,
			192.7051f,      212.6627f, -50.0000f,
			192.7051f,      212.6627f, -50.0000f,
			130.9017f,      212.6627f, -150.0000f,
			
			-61.8034f,      190.2113f, -211.8034f,
			19.0983f,      248.9898f, -150.0000f,
			19.0983f,      248.9898f, -150.0000f,
			-30.9017f,      285.3170f, -50.0000f,
			-30.9017f,      285.3170f, -50.0000f,
			-142.7051f, 248.9898f, -50.0000f,
			-142.7051f, 248.9898f, -50.0000f,
			-161.8034f, 190.2113f, -150.0000f,
			         
			-261.8034f,      -117.5571f,      50.0000f,
			-242.7051f,      -58.7785f,      150.0000f,
			-242.7051f,      -58.7785f,      150.0000f,
			-161.8034f,      -117.5571f,      211.8034f,
			-161.8034f,      -117.5571f,      211.8034f,
			-130.9017f,      -212.6627f,      150.0000f,
			-130.9017f,      -212.6627f,      150.0000f,
			-192.7051f,      -212.6627f,      50.0000f,
			
			30.9017f,      -285.3170f,      50.0000f,
			-19.0983f,      -248.9898f,      150.0000f,
			-19.0983f,      -248.9898f,      150.0000f,
			61.8034f,      -190.2113f,      211.8034f,
			61.8034f,      -190.2113f,      211.8034f,
			161.8034f,      -190.2113f,      150.0000f,
			161.8034f,      -190.2113f,      150.0000f,
			142.7051f,      -248.9898f,      50.0000f,
			
			280.9017f,      -58.7785f,      50.0000f,
			230.9017f,      -95.1057f,      150.0000f,
			230.9017f,      -95.1057f,      150.0000f,
			200.0000f,      0.0000f,       211.8034f,
			200.0000f,      0.0000f,       211.8034f,
			230.9017f,      95.1057f,      150.0000f,
			230.9017f,      95.1057f,      150.0000f,
			280.9017f,      58.7785f,      50.0000f,
			
			142.7051f,      248.9898f,      50.0000f,
			161.8034f,      190.2113f,      150.0000f,
			161.8034f,      190.2113f,      150.0000f,
			61.8034f,      190.2113f,      211.8034f,
			61.8034f,      190.2113f,      211.8034f,
			-19.0983f,      248.9898f,      150.0000f,
			-19.0983f,      248.9898f,      150.0000f,
			30.9017f,      285.3170f,      50.0000f,
			
			-192.7051f,      212.6627f,      50.0000f,
			-130.9017f,      212.6627f,      150.0000f,
			-130.9017f,      212.6627f,      150.0000f,
			-161.8034f,      117.5571f,      211.8034f,
			-161.8034f,      117.5571f,      211.8034f,
			-242.7051f,      58.7785f,      150.0000f,
			-242.7051f,      58.7785f,      150.0000f,
			-261.8034f,      117.5571f,      50.0000f,
			
			-80.9017f,      58.7785f,      273.6068f,
			30.9017f,      95.1057f,      273.6068f,
			30.9017f,      95.1057f,      273.6068f,
			100.0000f,      0.0000f,       273.6068f,
			100.0000f,      0.0000f,       273.6068f,
			30.9017f,      -95.1057f,      273.6068f,
			30.9017f,      -95.1057f,      273.6068f,
			-80.9017f,      -58.7785f,      273.6068f,
			
			-30.9017f,      -95.1057f, -273.6068f,
			-100.0f,      0.0f,      -273.6068f,
			-100.0f,      0.0f,      -273.6068f,
			-200.0000f, 0.0000f, -211.8034f,
			-200.0000f, 0.0000f, -211.8034f,
			-230.9017f, -95.1057f, -150.0000f,
			-230.9017f, -95.1057f, -150.0000f,
			-161.8034f, -190.2113f, -150.0000f,
			-161.8034f, -190.2113f, -150.0000f,
			-61.8034f,      -190.2113f, -211.8034f,
			
			80.9017f,      -58.7785f, -273.6068f,
			-30.9017f,      -95.1057f, -273.6068f,
			-30.9017f,      -95.1057f, -273.6068f,
			-61.8034f,      -190.2113f, -211.8034f,
			-61.8034f,      -190.2113f, -211.8034f,
			19.0983f,      -248.9898f, -150.0000f,
			19.0983f,      -248.9898f, -150.0000f,
			130.9017f,      -212.6627f, -150.0000f,
			130.9017f,      -212.6627f, -150.0000f,
			161.8034f,      -117.5571f, -211.8034f,
			
			80.9017f,      58.7785f, -273.6068f,
			80.9017f,      -58.7785f, -273.6068f,
			80.9017f,      -58.7785f, -273.6068f,
			161.8034f,      -117.5571f, -211.8034f,
			161.8034f,      -117.5571f, -211.8034f,
			242.7051f,      -58.7785f, -150.0000f,
			242.7051f,      -58.7785f, -150.0000f,
			242.7051f,      58.7785f, -150.0000f,
			242.7051f,      58.7785f, -150.0000f,
			161.8034f,      117.5571f, -211.8034f,
			
			-30.9017f,      95.1057f, -273.6068f,
			80.9017f,      58.7785f, -273.6068f,
			80.9017f,      58.7785f, -273.6068f,
			161.8034f,      117.5571f, -211.8034f,
			161.8034f,      117.5571f, -211.8034f,
			130.9017f,      212.6627f, -150.0000f,
			130.9017f,      212.6627f, -150.0000f,
			19.0983f,      248.9898f, -150.0000f,
			19.0983f,      248.9898f, -150.0000f,
			-61.8034f,      190.2113f, -211.8034f,
			
			-100.0f, 0.0f, -273.6068f,
			-30.9017f, 95.1057f, -273.6068f,
			-30.9017f, 95.1057f, -273.6068f,
			-61.8034f, 190.2113f, -211.8034f,
			-61.8034f, 190.2113f, -211.8034f,
			-161.8034f, 190.2113f, -150.0000f,
			-161.8034f, 190.2113f, -150.0000f,
			-230.9017f, 95.1057f, -150.0000f,
			-230.9017f, 95.1057f, -150.0000f,
			-200.0000f, 0.0000f, -211.8034f,
			
			-230.9017f, -95.1057f, -150.0000f,
			-280.9017f, -58.7785f, -50.0000f,
			-280.9017f, -58.7785f, -50.0000f,
			-261.8034f,      -117.5571f,      50.0000f,
			-261.8034f,      -117.5571f,      50.0000f,
			-192.7051f,      -212.6627f,      50.0000f,
			-192.7051f,      -212.6627f,      50.0000f,
			-142.7051f, -248.9898f, -50.0000f,
			-142.7051f, -248.9898f, -50.0000f,
			-161.8034f, -190.2113f, -150.0000f,
			
			19.0983f,      -248.9898f, -150.0000f,
			-30.9017f,      -285.3170f, -50.0000f,
			-30.9017f,      -285.3170f, -50.0000f,
			30.9017f,      -285.3170f,      50.0000f,
			30.9017f,      -285.3170f,      50.0000f,
			142.7051f,      -248.9898f,      50.0000f,
			142.7051f,      -248.9898f,      50.0000f,
			192.7051f,      -212.6627f, -50.0000f,
			192.7051f,      -212.6627f, -50.0000f,
			130.9017f,      -212.6627f, -150.0000f,
			         
			242.7051f,      -58.7785f, -150.0000f,
			261.8034f,      -117.5571f, -50.0000f,
			261.8034f,      -117.5571f, -50.0000f,
			280.9017f,      -58.7785f,      50.0000f,
			280.9017f,      -58.7785f,      50.0000f,
			280.9017f,      58.7785f,      50.0000f,
			280.9017f,      58.7785f,      50.0000f,
			261.8034f,      117.5571f, -50.0000f,
			261.8034f,      117.5571f, -50.0000f,
			242.7051f,      58.7785f, -150.0000f,
			
			130.9017f,      212.6627f, -150.0000f,
			192.7051f,      212.6627f, -50.0000f,
			192.7051f,      212.6627f, -50.0000f,
			142.7051f,      248.9898f,      50.0000f,
			142.7051f,      248.9898f,      50.0000f,
			30.9017f,      285.3170f,      50.0000f,
			30.9017f,      285.3170f,      50.0000f,
			-30.9017f,      285.3170f, -50.0000f,
			-30.9017f,      285.3170f, -50.0000f,
			19.0983f,      248.9898f, -150.0000f,
			
			-161.8034f, 190.2113f, -150.0000f,
			-142.7051f, 248.9898f, -50.0000f,
			-142.7051f, 248.9898f, -50.0000f,
			-192.7051f,      212.6627f,      50.0000f,
			-192.7051f,      212.6627f,      50.0000f,
			-261.8034f,      117.5571f,      50.0000f,
			-261.8034f,      117.5571f,      50.0000f,
			-280.9017f, 58.7785f, -50.0000f,
			-280.9017f, 58.7785f, -50.0000f,
			-230.9017f, 95.1057f, -150.0000f,
			         
			-280.9017f, 58.7785f, -50.0000f,
			-261.8034f,      117.5571f,      50.0000f,
			-261.8034f,      117.5571f,      50.0000f,
			-242.7051f,      58.7785f,      150.0000f,
			-242.7051f,      58.7785f,      150.0000f,
			-242.7051f,      -58.7785f,      150.0000f,
			-242.7051f,      -58.7785f,      150.0000f,
			-261.8034f,      -117.5571f,      50.0000f,
			-261.8034f,      -117.5571f,      50.0000f,
			-280.9017f, -58.7785f, -50.0000f,
			
			-142.7051f, -248.9898f, -50.0000f,
			-192.7051f,      -212.6627f,      50.0000f,
			-192.7051f,      -212.6627f,      50.0000f,
			-130.9017f,      -212.6627f,      150.0000f,
			-130.9017f,      -212.6627f,      150.0000f,
			-19.0983f,      -248.9898f,      150.0000f,
			-19.0983f,      -248.9898f,      150.0000f,
			30.9017f,      -285.3170f,      50.0000f,
			30.9017f,      -285.3170f,      50.0000f,
			-30.9017f,      -285.3170f, -50.0000f,
			         
			192.7051f,      -212.6627f, -50.0000f,
			142.7051f,      -248.9898f,      50.0000f,
			142.7051f,      -248.9898f,      50.0000f,
			161.8034f,      -190.2113f,      150.0000f,
			161.8034f,      -190.2113f,      150.0000f,
			230.9017f,      -95.1057f,      150.0000f,
			230.9017f,      -95.1057f,      150.0000f,
			280.9017f,      -58.7785f,      50.0000f,
			280.9017f,      -58.7785f,      50.0000f,
			280.9017f,      -58.7785f,      50.0000f,
			
			261.8034f,      117.5571f, -50.0000f,
			280.9017f,      58.7785f,      50.0000f,
			280.9017f,      58.7785f,      50.0000f,
			230.9017f,      95.1057f,      150.0000f,
			230.9017f,      95.1057f,      150.0000f,
			161.8034f,      190.2113f,      150.0000f,
			161.8034f,      190.2113f,      150.0000f,
			142.7051f,      248.9898f,      50.0000f,
			142.7051f,      248.9898f,      50.0000f,
			192.7051f,      212.6627f, -50.0000f,
			
			-30.9017f,      285.3170f, -50.0000f,
			30.9017f,      285.3170f,      50.0000f,
			30.9017f,      285.3170f,      50.0000f,
			-19.0983f,      248.9898f,      150.0000f,
			-19.0983f,      248.9898f,      150.0000f,
			-130.9017f,      212.6627f,      150.0000f,
			-130.9017f,      212.6627f,      150.0000f,
			-192.7051f,      212.6627f,      50.0000f,
			-192.7051f,      212.6627f,      50.0000f,
			-142.7051f, 248.9898f, -50.0000f,
			
			-242.7051f,      58.7785f,      150.0000f,
			-161.8034f,      117.5571f,      211.8034f,
			-161.8034f,      117.5571f,      211.8034f,
			-80.9017f,      58.7785f,      273.6068f,
			-80.9017f,      58.7785f,      273.6068f,
			-80.9017f,      -58.7785f,      273.6068f,
			-80.9017f,      -58.7785f,      273.6068f,
			-161.8034f,      -117.5571f,      211.8034f,
			-161.8034f,      -117.5571f,      211.8034f,
			-242.7051f,      -58.7785f,      150.0000f,
			
			-130.9017f,      -212.6627f,      150.0000f,
			-161.8034f,      -117.5571f,      211.8034f,
			-161.8034f,      -117.5571f,      211.8034f,
			-80.9017f,      -58.7785f,      273.6068f,
			-80.9017f,      -58.7785f,      273.6068f,
			30.9017f,      -95.1057f,      273.6068f,
			30.9017f,      -95.1057f,      273.6068f,
			61.8034f,      -190.2113f,      211.8034f,
			61.8034f,      -190.2113f,      211.8034f,
			-19.0983f,      -248.9898f,      150.0000f,
			
			161.8034f,      -190.2113f,      150.0000f,
			61.8034f,      -190.2113f,      211.8034f,
			61.8034f,      -190.2113f,      211.8034f,
			30.9017f,      -95.1057f,      273.6068f,
			30.9017f,      -95.1057f,      273.6068f,
			100.0000f,      0.0000f,       273.6068f,
			100.0000f,      0.0000f,       273.6068f,
			200.0000f,      0.0000f,       211.8034f,
			200.0000f,      0.0000f,       211.8034f,
			230.9017f,      -95.1057f,      150.0000f,
			         
			200.0000f,      0.0000f,       211.8034f,
			100.0000f,      0.0000f,       273.6068f,
			100.0000f,      0.0000f,       273.6068f,
			30.9017f,      95.1057f,      273.6068f,
			30.9017f,      95.1057f,      273.6068f,
			61.8034f,      190.2113f,      211.8034f,
			61.8034f,      190.2113f,      211.8034f,
			161.8034f,      190.2113f,      150.0000f,
			161.8034f,      190.2113f,      150.0000f,
			230.9017f,      95.1057f,      150.0000f,
			
			-19.0983f,      248.9898f,      150.0000f,
			61.8034f,      190.2113f,      211.8034f,
			61.8034f,      190.2113f,      211.8034f,
			30.9017f,      95.1057f,      273.6068f,
			30.9017f,      95.1057f,      273.6068f,
			-80.9017f,      58.7785f,      273.6068f,
			-80.9017f,      58.7785f,      273.6068f,
			-161.8034f,      117.5571f,      211.8034f,
			-161.8034f,      117.5571f,      211.8034f,
			-242.7051f,      58.7785f,      150.0000f,

	};
	
	private FloatBuffer vertexBuffer = null;
	
	private Random randGen;
	
	private int [] RGB = {255, 0, 0};
	
	public C60() {
		vertexBuffer = makeVertexBuffer();
		randGen = new Random();
	}
	
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPushMatrix();
		
		gl.glScalef(0.5f, 0.5f, 0.5f);
		gl.glColor4f(getRand(0), getRand(1), getRand(2), 1);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, vertices.length / 3);
		
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
    /**
     * Get a random new color value according to the current one
     * @param index The index used to indicate R, G or B
     * @return The new value of the channel
     */
	private float getRand(int index) {
		int rand = randGen.nextInt(20) - 10;
		RGB[index] += rand;
		if (RGB[index] < 96) {
			RGB[index] = 96;
		} else if (RGB[index] > 255) {
			RGB[index] = 255;
		}
		return RGB[index] / 255f;
	}
	
	private FloatBuffer makeVertexBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = bb.asFloatBuffer();
		buffer.put(vertices);
		buffer.position(0);
		return buffer;
	}
	
}
