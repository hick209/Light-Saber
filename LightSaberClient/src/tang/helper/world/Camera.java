package tang.helper.world;

import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glShadeModel;

import org.lwjgl.util.glu.GLU;

import tang.helper.entities.Entity;
import tang.helper.struct.Heading;
import tang.helper.struct.Vector3;

/**
 * This camera assumes that +z is upwards
 * @author michael
 *
 */
public class Camera {
	public Vector3 pos;
	public Heading heading;
	public Vector3 focus;
	public Entity target;
	
	public Camera() {
		this.pos = new Vector3();
		this.heading = new Heading();
		this.focus = new Vector3();


		glShadeModel(GL_SMOOTH);
		glClearColor(0.55f, 0.804f, 0.97f, 0.0f);
		glClearDepth(1.0f);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	/**
	 * Sets the Entity to "view" from
	 * @param e
	 */
	public void setTarget(Entity e) {
		this.target = e;
	}
	
	public void update() {

		this.pos = this.target.getPos();
		this.heading = this.target.getHeading();
		
		this.focus = this.pos.add(heading.getDirectionVector());

		GLU.gluLookAt(this.pos.getX(), this.pos.getY() + 1.61f, this.pos.getZ(),
				this.focus.getX(), this.focus.getY()  + 1.61f, this.focus.getZ(),
				0.0f, 1.0f, 0.0f);
	}
	
	public void setHeading(Heading h) {
		this.heading = h;
	}
	
	public void setPitch(float f) {
		heading.setPitch(f);
	}
	
	public void setYaw(float f) {
		heading.setYaw(f);
	}
}
