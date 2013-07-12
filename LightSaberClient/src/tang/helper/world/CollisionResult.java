package tang.helper.world;

import tang.helper.struct.Vector3;

public class CollisionResult {
	public Vector3 resolution;
	public Vector3b collision; //this is a boolean vector!
	
	public CollisionResult() {
		this.resolution = new Vector3();
		collision = new Vector3b();
	}
	
	public void setResolution(float x, float y, float z) {
		this.resolution.set(x, y, z);
	}
	
	public Vector3 getResolution() {
		return this.resolution;
	}
	
	public void setCollision(boolean x, boolean y, boolean z) {
		this.collision.set(x, y, z);
	}
	
	public boolean getCollisionX() {
		return this.collision.x;
	}
	public boolean getCollisionY() {
		return this.collision.y;
	}
	public boolean getCollisionZ() {
		return this.collision.z;
	}
	
	
	
	public String toString() {
		return "CollisionResult(resolution: {" + this.resolution + "}; collision: {" + this.collision + "})";
	}


	/**
	 * This is a private class for CollisionResult to use, as I don't see any other class needing it.
	 * 3d boolean vector for storing collision state in each of the Cartesian directions.
	 * @author michael
	 *
	 */
	public class Vector3b {
		public boolean x, y, z;
		public Vector3b(boolean x, boolean y, boolean z) {
			this.set(x, y, z);
		}

		public Vector3b() {
			this(false, false, false);
		}

		public void set(boolean x, boolean y, boolean z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public String toString() {
			return "Vector3b(x: " + this.x + ", y: " + this.y + ", z: " + this.z + ")";
		}
	}
	
}