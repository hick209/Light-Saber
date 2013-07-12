package tang.helper.struct;

/**
 * Helper class for handling pitch and yaw heading in degrees
 * 
 * @author michael
 */
public class Heading {
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;
	public static final int DIRECTION_FORWARD = 4;
	public static final int DIRECTION_BACKWARD = 5;
	
	/**
	 * Pitch corresponding to looking forward (parallel to the ground)
	 */
	public static final float DEFAULT_PITCH = 90.0f;
	
	/**
	 * This field exists so it yaw doesn't get jealous of pitch for having a default value
	 */
	public static final float DEFAULT_YAW = 0.0f;
	
	/**
	 * The pitch should be constrained by almost 0 to 180 degrees
	 */
	public float pitch;
	/**
	 * The yaw is constrained by -360 to 360
	 */
	public float yaw;
	/**
	 * not implemented yet, yo
	 */
	public float roll;
	
	/** 
	 * Create a Heading object with all fields set to 0
	 */
	public Heading() {
		this(0.0f, 0.0f);
	}
	
	/**
	 * Create a Heading object with a pitch and yaw
	 */
	public Heading(float pitch, float yaw) {
		this.set(pitch, yaw);
	}
	
	/**
	 * Sets the fields to passed values
	 * @param pitch
	 * @param yaw
	 */
	public void set(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	/**
	 * Assigns the default values for the heading, meant to be chained onto the constructor, ex:
	 * <br>
	 * <code>new Heading().withDefaults()</code>
	 * @return heading with values set
	 */
	public Heading withDefaults() {
		this.set(DEFAULT_PITCH, DEFAULT_YAW);
		return this;
	}
	
	/**
	 * Gets the value for the pitch
	 * @return pitch
	 */
	public float getPitch() {
		return this.pitch;
	}
	/**
	 * Sets the value of the pitch and limits it so we can only look up to down
	 * @param f pitch
	 */
	public void setPitch(float f) {
		this.pitch = f;
		//we must constrain the pitch before it reaches the limits, because the view will flip if it's truly at the limit
		this.pitch = Math.max( 0.001f, Math.min( this.pitch, 179.999f ) );
	}
	/**
	 * Adds to the value of the pitch
	 * @param f pitch
	 */
	public void addPitch(float f) {
		this.setPitch(this.pitch + f);
	}
	
	/**
	 * Gets the value of the yaw
	 * @return yaw
	 */
	public float getYaw() {
		return this.yaw;
	}
	/**
	 * Sets the value of the yaw
	 * @param f yaw
	 */
	public void setYaw(float f) {
		this.yaw = f;
		this.yaw = this.yaw % 360;
	}
	/**
	 * Adds to the value of the yaw
	 * @param f yaw
	 */
	public void addYaw(float f) {
		this.setYaw(this.yaw + f);
	}
	
	private Vector3 calculateUnitVector(float pitch, float yaw) {
		float theta = (float) ((pitch) * (Math.PI/180.0f));
		float phi = (float) (yaw * (Math.PI/180.0f));
		return new Vector3(
				(float) ( Math.sin(theta) * Math.cos(phi)  ),
				(float) ( Math.cos(theta) ),
				(float) ( Math.sin(theta) * Math.sin(phi) )
				);
	}
	
	/**
	 * Calculates and returns a unit vector in Cartesian coordinates pointing in the direction of the pitch and yaw
	 * @return directionVector
	 */
	public Vector3 getDirectionVector() {
		return this.calculateUnitVector(this.pitch, this.yaw);
	}
	
	public Vector3 getMovementVector(float pitch, float yaw) {
		return this.calculateUnitVector(pitch, yaw);
	}
	
	public Vector3 getMovementVector(int d) {
		Vector3 vector;
		if(d == DIRECTION_UP) {
			vector = this.calculateUnitVector(0.0f, 0.0f);
		} else if(d == DIRECTION_DOWN) {
			vector = this.calculateUnitVector(180.0f, 0.0f);
		} else if(d == DIRECTION_LEFT) {
			vector = this.calculateUnitVector(90.0f, this.yaw - 90.0f);
		} else if(d == DIRECTION_RIGHT) {
			vector = this.calculateUnitVector(90.0f, this.yaw + 90.0f);
		} else if(d == DIRECTION_FORWARD) {
			vector = this.calculateUnitVector(90.0f, this.yaw);
		} else if(d == DIRECTION_BACKWARD) {
			vector = this.calculateUnitVector(90.0f, this.yaw + 180.0f);
		} else {
			vector = new Vector3();
		}
		return vector;
	}
	
	public String toString() {
		return "Heading(" + this.pitch + ", " + this.yaw + ")";
	}
}
