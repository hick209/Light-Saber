package tang.helper.struct;

import org.lwjgl.util.vector.Vector2f;

/**
 * Generic 2-dimensional vector that supports immutable add
 * @author michael
 *
 */
public class Vector2 extends Vector2f {
	private static final long serialVersionUID = -6625677651186662020L;

	/**
	 * Create a new Vector2 with fields defaulting to zero
	 */
	public Vector2() {
		super();
	}

	/**
	 * Create a new Vector2 with defined values
	 * @param x
	 * @param y
	 */
	public Vector2(float x, float y) {
		super(x, y);
	}

	/**
	 * Returns a new vector that is the sum of the input and the vector it is adding do
	 * @param input vector
	 * @return sum of the vectors
	 */
	public Vector2 add(Vector2 v) {
		return new Vector2(
				this.getX() + v.getX(),
				this.getY() + v.getY()
				);
	}

	/**
	 * Returns a vector that is scaled by a factor
	 * @param scale factor
	 * @return scaled vector
	 */
	public Vector2 scale(float f) {
		return new Vector2(
				this.getX() * f,
				this.getY() * f
				);
	}

	public String toString() {
		return "Vector2(" + this.x + ", " + this.y + ")";
	}
}
