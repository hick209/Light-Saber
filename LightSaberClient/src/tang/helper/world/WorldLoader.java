package tang.helper.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import tang.helper.entities.Entity;
import tang.helper.obj.Model;
import tang.helper.obj.OBJLoader;
import tang.helper.struct.Vector3;
import tang.helper.utils.Console;
import tang.testgame.EntityNpc;
import tang.testgame.EntityPlayer;

public class WorldLoader {
	public static World loadWorld(File f) {
		World m = null;
		
		//TODO: figure out a file format for containing maps
		
		List<Entity> worldEntities = new ArrayList<Entity>();
		
//		readEntities.add(new Player());
		
		
		//this map is hardcoded for now
		m = new World();
		m.setName("testworld");
		
		
		Camera camera = new Camera();
		EntityPlayer player = new EntityPlayer(new Vector3(-8, 10, 0));
		camera.setTarget(player);
		
		worldEntities.add(player);
		Console.debug("Player added: " + player);

		EntityNpc enemy = new EntityNpc(new Vector3(10, 0, 10));
		worldEntities.add(enemy);
		Console.debug("Enemy added: " + enemy);
		
		EntityNpc enemy2 = new EntityNpc(new Vector3(-10, 0, 10));
		enemy2.getHeading().setYaw(150.0f);
		worldEntities.add(enemy2);
		Console.debug("Enemy added: " + enemy2);
		
		
		EntityNpc enemy3 = new EntityNpc(new Vector3(-15, 5, -10));
		worldEntities.add(enemy3);
		Console.debug("Enemy added: " + enemy3);
		
		m.setEntityList(worldEntities);
		m.setCamera(camera);
		
		Model worldModel = null;
		try {
			worldModel = OBJLoader.loadModel(new File("assets/levels/collisiontest.obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		
		
		m.setModel(worldModel);
		m.setCollisionModel(worldModel);
		
		Console.info("World \"" + m.getName() + "\" loaded");
		
		return m;
	}
}
