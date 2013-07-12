package tang.helper.obj;


public class FaceIndices {
	private int a, b, c, d;
	public FaceIndices() {
		this(0, 0, 0, 0);
	}
	public FaceIndices(int x, int y, int z) {
		//defaults indices D to -1.0f if we're dealing with a 3d vector (triangle)
		this(x, y, z, -1);
	}
	public FaceIndices(int x, int y, int z, int w) {
		this.a = x;
		this.b = y;
		this.c = z;
		this.d = w;
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
}
