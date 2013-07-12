package tang.helper.utils;

import static org.lwjgl.opengl.GL11.*;
import tang.helper.struct.Heading;
import tang.helper.struct.Vector3;

/**
 * Helper class to orient oneself in a 3d world
 * @author michael
 *
 */
public class Axis {

	/**
	 * Draws a 3d axis at the origin, with standard coloring, length 1 in each direction
	 */
	public static void draw() {
		
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glDisable(GL_COLOR_MATERIAL);
		
		glLineWidth(2.0f);

		glBegin(GL_LINES);
			glColor3f(1.0f, 0.0f, 0.0f); // X axis is red.
			glVertex3f(0.0f, 0.0f, 0.0f);
			glVertex3f(1.0f, 0.0f, 0.0f);
			
			glColor3f(0.0f, 1.0f, 0.0f); // Y axis is green.
			glVertex3f(0.0f, 0.0f, 0.0f);
			glVertex3f(0.0f, 1.0f, 0.0f);
			
			glColor3f(0.0f, 0.0f, 1.0f); // z axis is blue.
			glVertex3f(0.0f, 0.0f, 0.0f);
			glVertex3f(0.0f, 0.0f, 1.0f);
		glEnd();

		glLineWidth(1.0f);
		
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);

	}
	
	public static void draw(Vector3 pos, Heading h) {
		//TODO this is broken?
		
		glPushMatrix();
			glTranslatef(pos.getX(), pos.getY(), pos.getZ());
			glRotatef(-h.getYaw() + 90.0f,
					0.0f, 1.0f, 0.0f);
			glRotatef(-h.getPitch() + 90.0f,
					1.0f, 0.0f, 0.0f);
	
			draw();
			
		glPopMatrix();
	}
}
