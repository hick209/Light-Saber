package tang.helper.utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class FloatUtils {
	
	/**
	 * Allows the creation of a float buffer from an array of floats
	 * @param f Input float[] to be turned into a FloatBuffer
	 * @return FloatBuffer from float[]
	 */
	public static FloatBuffer asFloatBuffer(float[] f) {
		//TODO this should move to it's own class, FloatUtils?
		FloatBuffer buffer = BufferUtils.createFloatBuffer(f.length);
		buffer.put(f);
		buffer.flip();
		return buffer;
	}
}
