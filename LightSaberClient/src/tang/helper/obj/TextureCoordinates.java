package tang.helper.obj;

import tang.helper.struct.Vector2;

/**
 * This is just a simple Vector2 class that follows naming conventions for uv transforms
 * @author michael
 *
 */
public class TextureCoordinates extends Vector2 {
	private static final long serialVersionUID = 1L;

	public TextureCoordinates(float u, float v) {
		this.x = u;
		this.y = v;
	}
	public float getU() {
		return x;
	}
	public void setU(float u) {
		this.x = u;
	}
	public float getV() {
		return y;
	}
	public void setV(float v) {
		this.y = v;
	}
	
}
