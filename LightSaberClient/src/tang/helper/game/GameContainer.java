package tang.helper.game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import tang.helper.input.Input;
import tang.helper.utils.Console;

/**
 * GameContainer holds all the LWJGL code for displaying a window and updating the game loop
 * @author michael
 *
 */
public class GameContainer {
	private static final int DEFAULT_DISPLAY_WIDTH = 640;
	private static final int DEFAULT_DISPLAY_HEIGHT = 480;
	private static final String DEFAULT_DISPLAY_TITLE = "game";

	private int displayWidth, displayHeight;
	private String displayTitle;
	private Game game;

	public GameContainer(Game game) {

		this.game = game;
		this.displayWidth = GameContainer.DEFAULT_DISPLAY_WIDTH;
		this.displayHeight = GameContainer.DEFAULT_DISPLAY_HEIGHT;
		this.displayTitle = GameContainer.DEFAULT_DISPLAY_TITLE;

	}

	/**
	 * Chainable version of setDisplayWidth()
	 * @param width Width of the display
	 * @return pointer to itself, so the method can be chained
	 */
	public GameContainer withDisplayWidth(int width) {
		this.setDisplayWidth(width);
		Console.debug("Set display width: " + this.displayWidth);
		return this;
	}

	/**
	 * Chainable version of setDisplayHeight()
	 * @param height Height of the display
	 * @return pointer to itself, so the method can be chained
	 */
	public GameContainer withDisplayHeight(int height) {
		this.setDisplayHeight(height);
		Console.debug("Set display height: " + this.displayHeight);
		return this;
	}

	/**
	 * Chainable version of setDisplayTitle()
	 * @param title Title of the display
	 * @return pointer to itself, so the method can be chained
	 */
	public GameContainer withDisplayTitle(String title) {
		this.setDisplayTitle(title);
		Console.debug("Set display title: " + this.displayTitle);
		return this;
	}

	//TODO java docs
	public void setDisplayWidth(int width) {
		this.displayWidth = width;
	}
	public void setDisplayHeight(int height) {
		this.displayHeight = height;
	}
	public void setDisplayTitle(String title) {
		this.displayTitle = title;
	}


	public GameContainer start() {

		//set the GL version to 3.2
//		PixelFormat pixelFormat = new PixelFormat();
//		ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
//				.withForwardCompatible(true)
//				.withProfileCore(true);

		//Try and create the GL context
		try {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.setTitle(displayTitle);
			Display.create();
//			Display.create(pixelFormat, contextAtrributes);
		} catch (LWJGLException e) {
			Console.error("Display could not be initialized");
			e.printStackTrace();
			Game.exit(true);
		}


		//initialize the viewport
		glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		glShadeModel(GL_SMOOTH);

		Input.init();
		game.init();
		Console.info("Game loaded");

		//game loop
		while(!Display.isCloseRequested()) {
			Input.update();

			GameContainer.ready3d();
			if(Game.getLoadedWorld() != null) {
				Game.getLoadedWorld().update();
			}
			game.update();

			if(Game.getLoadedWorld() != null) {
				Game.getLoadedWorld().draw();
			}
			game.draw();


			GameContainer.ready2d();
			glDisable(GL_LIGHTING);
			glDisable(GL_COLOR_MATERIAL);
			game.draw2d();
			glEnable(GL_COLOR_MATERIAL);
			glEnable(GL_LIGHTING);


			Display.update();
			Display.sync(60);
		}

		//quit the game if outside the loop
		Game.exit();

		return this;
	}



	private static void ready3d() {
		//Setup for 3d
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(50.0f, ((float) Display.getDisplayMode().getWidth() / (float) Display.getDisplayMode().getHeight()), 0.1f, 256.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);
		

	}

	private static void ready2d() {
		//Setup for 2d
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0f, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0.0f, -1.0f, 1.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glClear(GL_DEPTH_BUFFER_BIT);

		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		


	}

}
