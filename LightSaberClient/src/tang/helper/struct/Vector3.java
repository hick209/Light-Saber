package tang.helper.struct;

import org.lwjgl.util.vector.Vector3f;

/**
 * Generic 3-dimensional vector that supports immutable add
 * @author michael
 *
 */
public class Vector3 extends Vector3f {
	private static final long serialVersionUID = 7053736861893015187L;

	/**
	 * Create a new Vector3 with fields defaulting to zero
	 */
	public Vector3() {
		super();
	}
	
	/**
	 * Create a new Vector3 with defined values
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(float x, float y, float z) {
		super(x, y, z);
	}

	/**
	 * Returns a new vector that is the sum of the input and the vector it is adding do
	 * @param input vector
	 * @return sum of the vectors
	 */
	public Vector3 add(Vector3 v) {
		return new Vector3(
				this.getX() + v.getX(),
				this.getY() + v.getY(),
				this.getZ() + v.getZ()
				);
	}

	/**
	 * Returns a vector that is scaled by a factor
	 * @param scale factor
	 * @return scaled vector
	 */
	public Vector3 scale(float f) {
		return new Vector3(
				this.getX() * f,
				this.getY() * f,
				this.getZ() * f
				);
	}

	public String toString() {
		return "Vector3(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
