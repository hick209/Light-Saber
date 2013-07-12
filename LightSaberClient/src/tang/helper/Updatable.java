package tang.helper;

/**
 * Defines a class that can be initialized, updated, and (optionally) drawn
 * @author michael
 *
 */
public interface Updatable {
	/**
	 * Method that should be called once upon creation of the instance
	 */
	public void init();
	
	/**
	 * Method that should be called every tick in the game loop
	 */
	public void update();
	
	/**
	 * Method that should be called immediately after the update() method
	 */
	public void draw();
}
