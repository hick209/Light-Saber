package tang.testgame;


import org.lwjgl.input.Mouse;

import tang.helper.entities.Entity;
import tang.helper.input.Input;
import tang.helper.struct.Heading;
import tang.helper.struct.Vector3;
import tang.helper.world.CollisionResult;

public class EntityPlayer extends Entity {

	
	public EntityPlayer(Vector3 pos) {
		super(pos);
	}

	@Override
	public void init() {
		float gravity = -0.03f;
		this.accel.setY(gravity);

		this.friction = 1.2f;
		
		this.size = new Vector3(1,2,1);
	}

	public void handleMovementTrace(CollisionResult res) {
		super.handleMovementTrace(res);
	}
	
	public void resolveCollision(Entity other) {
		super.resolveCollision(other);
		
	}
	
	@Override
	public void update() {
				
//		this.updatePosition();
		
		float mouseSensitivity = 1.5f;
		this.heading.addPitch(-1 * Mouse.getDY() / mouseSensitivity);
		this.heading.addYaw(Mouse.getDX() / mouseSensitivity);

		float moveSensitivity = 0.3f;
		if(Input.state(Action.WALK)) {
			moveSensitivity *= 0.4f;
		}
		
		
		Vector3 moveVel = vel;
		if(Input.state(Action.STRAFE_LEFT)) {
			moveVel = heading.getMovementVector(Heading.DIRECTION_LEFT);
		}
		if(Input.state(Action.STRAFE_RIGHT)) {
			moveVel = heading.getMovementVector(Heading.DIRECTION_RIGHT);
		}
		if(Input.state(Action.MOVE_FOWARD)) {
			if(Input.state(Action.STRAFE_LEFT)) {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw() - 45.0f);
			} else if(Input.state(Action.STRAFE_RIGHT)) {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw() + 45.0f);
			} else {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw());
			}
		}
		if(Input.state(Action.MOVE_BACKWARD)) {
			if(Input.state(Action.STRAFE_LEFT)) {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw() - 180.0f + 45.0f);
			} else if(Input.state(Action.STRAFE_RIGHT)) {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw() - 180.0f - 45.0f);
			} else {
				moveVel = heading.getMovementVector(Heading.DEFAULT_PITCH, this.heading.getYaw() - 180.0f);
			}
		}
		moveVel = moveVel.scale(moveSensitivity);
		moveVel.setY(vel.getY()); //maintain y-velocity
		this.vel = moveVel;
		
		if(Input.pressed(Action.JUMP)) {
			if(this.standing) {
				this.vel.y += 0.6f;
			}
		}

	}

	@Override
	public void draw() {
		this.drawBoundingBox();
//		this.drawAxis();
	}

}
