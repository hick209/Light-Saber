package tang.testgame;

import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import tang.helper.game.Game;
import tang.helper.game.GameContainer;
import tang.helper.gl.GLShader;
import tang.helper.input.Input;
import tang.helper.utils.Axis;
import tang.helper.utils.FloatUtils;

public class Main extends Game {
	
	public static void main(String[] args) {
	    new GameContainer(new Main())
			    .withDisplayWidth(800)
			    .withDisplayHeight(600)
			    .withDisplayTitle("tangmi/lwjgl-test")
			    .start();
	}

	GLShader tangShader;

	public static String text = "";

	@Override
	public void init() {
				
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLight(GL_LIGHT0, GL_DIFFUSE, FloatUtils.asFloatBuffer(new float[]{1f, 1f, 1f, 1.0f}));

		glLightModel(GL_LIGHT_MODEL_AMBIENT, FloatUtils.asFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1.0f}));
		
//		glEnable(GL_LIGHT1);
//		glLight(GL_LIGHT1, GL_AMBIENT, FloatUtils.asFloatBuffer(new float[]{0.1f, 0.1f, 0.1f, 0.1f}));


		
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);		


		Game.setLoadedWorld(new File("lolThisDoesntMatterYet.hax"));
		
		Input.bind(Keyboard.KEY_A, Action.STRAFE_LEFT);
		Input.bind(Keyboard.KEY_D, Action.STRAFE_RIGHT);
		Input.bind(Keyboard.KEY_W, Action.MOVE_FOWARD);
		Input.bind(Keyboard.KEY_S, Action.MOVE_BACKWARD);
		Input.bind(Keyboard.KEY_SPACE, Action.JUMP);
		Input.bind(Keyboard.KEY_LSHIFT, Action.WALK);
		
		Input.bind(Keyboard.KEY_ESCAPE, Action.QUIT);
		Input.bind(Keyboard.KEY_F5, Action.WIREFRAME_MODE);

		Input.grabMouse();
		
		tangShader = new GLShader("assets/shaders/tang-concept-shader/shader.vert", "assets/shaders/tang-concept-shader/shader.frag");
	}

	boolean wireFrameToggle = false;

	
	@Override
	public void update() {
	
		if(Input.state(Action.QUIT)) {
			Game.exit();
		}
		if(Input.pressed(Action.WIREFRAME_MODE)) {
			wireFrameToggle = !wireFrameToggle;
		}
		
		if(wireFrameToggle) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		} else {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		
	}

	@Override
	public void draw() {
		glLight(GL_LIGHT0, GL_POSITION, FloatUtils.asFloatBuffer(new float[]{0, 15, 0, 1.0f}));
		
		glBegin(GL_TRIANGLES);
			glColor3f(1,1,0);
			glTexCoord2f(0,0);
			glVertex3f(1.0f, 1.0f, 1.0f);
			glColor3f(0,1,1);
			glTexCoord2f(0.5f,0);
			glVertex3f(2.0f, 0.0f, 0.0f);
			glColor3f(1,0,1);
			glTexCoord2f(0.5f,1);
			glVertex3f(0.0f, 0.0f, 6f);
		glEnd();			

		Axis.draw();
	}
	
	@Override
	public void draw2d() {

//		tangShader.release();
		
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		glBegin(GL_LINES);
			glColor3f(1.0f, 1.0f, 1.0f);

			int size = 10;
			glVertex2f(width/2 - size, height/2);
			glVertex2f(width/2 + size, height/2);

			glVertex2f(width/2, height/2 - size);
			glVertex2f(width/2, height/2 + size);
		glEnd();
		
//		tangShader.use();

	}
	
}