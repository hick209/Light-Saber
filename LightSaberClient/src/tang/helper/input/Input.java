package tang.helper.input;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import tang.helper.game.Game;
import tang.helper.struct.Vector2;
import tang.helper.utils.Console;

/**
 * Input handler that abstracts keyboard and mouse input into actions that a client can check against, allowing easy rebinding of keys
 * @author michael
 *
 */
public class Input {
	//maybe this class should be a singleton?

	private static Map<Integer, Object> bindings = new HashMap<Integer, Object>();

	private static Map<Object, Boolean> actions = new HashMap<Object, Boolean>();
	
	//these could be sets? clear them before every update and check if the action exists when returning state
	private static Map<Object, Boolean> pressed = new HashMap<Object, Boolean>();
	private static Map<Object, Boolean> released = new HashMap<Object, Boolean>();

	private static int mouseDX;
	private static int mouseDY;
	/**
	 * Initialize the input handler. Doesn't necessarily need to be called, but I'm exceeding expectations
	 */
	public static void init() {
		try {
			Input.initMouse();
			Input.initKeyboard();
		} catch (LWJGLException e) {
			Console.error("Could not initialize input!");
			e.printStackTrace();
			Game.exit();
		}
	}

	private static void initMouse() throws LWJGLException {
		Mouse.create();
	}

	/**
	 * Allows the display to "grab" the mouse, useful for mouse-controlled camera views
	 */
	public static void grabMouse() {
		Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2); //force the mouse to become "grabbable" (inside window)
		Mouse.setGrabbed(true);
	}
	
	/**
	 * Releases the mouse and places it at it's last location (probably the center of the screen)
	 */
	public static void releaseMouse() {
		Input.releaseMouse(Mouse.getX(), Mouse.getY());
	}
	
	/**
	 * Releases the mouse and places it at the specified location
	 * @param x
	 * @param y
	 */
	public static void releaseMouse(int x, int y) {
		Mouse.setCursorPosition(x, y);
		Mouse.setGrabbed(false);
	}
	
	/**
	 * Releases the mouse and places it at the specified location
	 * @param mousePos
	 */
	public static void releaseMouse(Vector2 mousePos) {
		Mouse.setCursorPosition((int) mousePos.getX(), (int) mousePos.getY());
		Mouse.setGrabbed(false);
	}
	
	//TODO method for grabbing mouse projection in 3d space? maybe?
	//TODO mouse method getters
	public static int getMouseDX() {
		return mouseDX;
	}
	
	public static int getMouseDY() {
		return mouseDY;
	}
	
	
	

	private static void initKeyboard() throws LWJGLException {
		Keyboard.create();
	}

	/**
	 * Bind a key to an action, so you can check the state of the action, rather than the key
	 * <br>
	 * action can be a string, but it is recommended to create a public enum class to hold all actions
	 * @param key
	 * @param action
	 */
	public static void bind(int key, Object action) {
		bindings.put(key, action);
	}

	/**
	 * Release the bind from the key
	 * @param key
	 */
	public static void unbind(int key) {
		bindings.remove(key);
	}

	/**
	 * Checks whether the key associated with an action is pressed in the last tick
	 * @param action
	 * @return pressed state of the key
	 */
	public static boolean pressed(Object action) {
		return pressed.containsKey(action) && pressed.get(action);
	}
	
	/**
	 * Checks whether the key associated with an action is released in the last tick
	 * @param action
	 * @return pressed state of the key
	 */
	public static boolean released(Object action) {
		return released.containsKey(action) && released.get(action);
	}

	/**
	 * Checks whether the key associated with an action is held down currently
	 * @param action
	 * @return state of the key
	 */
	public static boolean state(Object action) {
		//this implementation sucks probably
		//TODO spit out an error if an action isn't bound yet
		return actions.containsKey(action) && actions.get(action);
	}

	
	/**
	 * Updates the state of the input handler
	 */
	public static void update() {
		
		pressed.clear();
		released.clear();
		
		while(Keyboard.next()) {
			for(Map.Entry<Integer, Object> binding : bindings.entrySet()) {
				int key = binding.getKey();
				Object action = binding.getValue();
				if(Keyboard.getEventKey() == key) {
					if(Keyboard.getEventKeyState()) {
						pressed.put(action, true);
						actions.put(action, true);
					} else {
						actions.put(action, false);
						released.put(action, true);
					}
				}
			}
		}
		
		//TODO get mouse delta methods

		//TODO add mouse events
		while(Mouse.next()) {
			int eventButton = Mouse.getEventButton();
			if(eventButton >= 0) {
				
			} else {
				mouseDX = Mouse.getEventDX();
				mouseDY = Mouse.getEventDY();
			}
		}
	}

}