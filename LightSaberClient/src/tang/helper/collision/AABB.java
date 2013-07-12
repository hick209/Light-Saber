package tang.helper.collision;

import tang.helper.struct.Vector3;

import static org.lwjgl.opengl.GL11.*;

//TODO: SAT collision checking

/**
 * Simple axis-aligned bounding box object
 * @author michael
 *
 */
public class AABB {
	public float minX = Float.POSITIVE_INFINITY;
	public float maxX = Float.NEGATIVE_INFINITY;
	public float minY = Float.POSITIVE_INFINITY;
	public float maxY = Float.NEGATIVE_INFINITY;
	public float minZ = Float.POSITIVE_INFINITY;
	public float maxZ = Float.NEGATIVE_INFINITY;
	private Vector3 pos;
	private Vector3 size;

	public AABB(Vector3 pos, Vector3 size) {
		this.pos = pos;
		this.size = size;
		this.updateCoordinates();
	}

	private void updateCoordinates() {
		this.minX = pos.getX() - size.getX() / 2;
		this.maxX = pos.getX() + size.getX() / 2;

		this.minZ = pos.getZ() - size.getZ() / 2;
		this.maxZ = pos.getZ() + size.getZ() / 2;

		this.minY = pos.getY();
		this.maxY = pos.getY() + size.getY();
	}

	public void setPos(Vector3 pos) {
		this.pos = pos;
		this.updateCoordinates();
	}

	public void setSize(Vector3 size) {
		this.size = size;
		this.updateCoordinates();
	}

	public Vector3 getPos() {
		return this.pos;
	}

	public Vector3 getSize() {
		return this.size;
	}

	public Vector3[] getVertices() {
		return new Vector3[]{
				new Vector3(minX, maxY, 0.0f),
				new Vector3(minX, minY, 0.0f),
				new Vector3(maxX, minY, 0.0f),
				new Vector3(maxX, maxY, 0.0f),
		};
	}

	public boolean isColliding(AABB other) {
		return !(
				other.maxX <= this.minX ||
				other.minX >= this.maxX ||
				other.maxY <= this.minY ||
				other.minY >= this.maxY ||
				other.maxZ <= this.minZ ||
				other.minZ >= this.maxZ
		);
	}

	public void draw() {
		glDisable(GL_LIGHTING);
		glDisable(GL_COLOR_MATERIAL);

		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(1.0f);

		glBegin(GL_QUADS);
			glColor3f(1f, 1f, 1f);

			//-z
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y + this.size.y, this.pos.z - (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y + this.size.y, this.pos.z - (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y, this.pos.z - (this.size.z / 2));
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y, this.pos.z - (this.size.z / 2));

			//+z
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y + this.size.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y + this.size.y, this.pos.z + (this.size.z / 2));

			//-x
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y, this.pos.z - (this.size.z / 2));
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y + this.size.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x - (this.size.x / 2), this.pos.y + this.size.y, this.pos.z - (this.size.z / 2));

			//+x
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y + this.size.y, this.pos.z - (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y + this.size.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y, this.pos.z + (this.size.z / 2));
			glVertex3f(this.pos.x + (this.size.x / 2), this.pos.y, this.pos.z - (this.size.z / 2));

			//+ and - y are automagically set
		glEnd();

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);
	}
}
