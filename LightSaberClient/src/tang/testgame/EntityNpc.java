package tang.testgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import tang.helper.entities.Entity;
import tang.helper.obj.OBJLoader;
import tang.helper.struct.Vector3;

public class EntityNpc extends Entity {
	
	
	public EntityNpc(Vector3 pos) {
		super(pos);
		this.getHeading().setPitch(90.0f);
		
		this.size = new Vector3(1, 2.5f, 1);
	}

	@Override
	public void init() {
		try {
//			this.model = OBJLoader.loadModel(new File("assets/objects/texturedcube.obj"));
//			this.model = OBJLoader.loadModel(new File("assets/objects/dogthing.obj"));
			this.model = OBJLoader.loadModel(new File("assets/objects/person.obj"));
//			this.model = OBJLoader.loadModel(new File("assets/objects/MICAHEL1111.obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void resolveCollision(Entity e) {}
	
	public void update() {
		
//		this.heading.addYaw(1.0f);
//		this.pos = this.pos.add(heading.getMovementVector(Heading.DIRECTION_FORWARD).scale(0.1f));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) heading.pitch++;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) heading.pitch--;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) heading.yaw++;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) heading.yaw--;
		
	}

	public void draw() {		
		this.model.draw(this.pos, this.heading);
		this.drawBoundingBox();
		this.drawAxis();
	}
}
