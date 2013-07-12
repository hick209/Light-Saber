package tang.helper.world;

import tang.helper.obj.Model;
import tang.helper.struct.Vector3;

public class CollisionMap {
	
	Model collisionMap;
	
	public CollisionMap() {
		collisionMap = null;
	}
	
	public void setModel(Model cm) {
		this.collisionMap = cm;
		
		
	}
	
	public CollisionResult trace(Vector3 pos, Vector3 delta, Vector3 size) {
		//calculate collision with faces of collision map
		
		CollisionResult res = new CollisionResult();
		
		res.setResolution(pos.x + delta.x, pos.y + delta.y, pos.z + delta.z);
		
		if(pos.y + delta.y <= 0) {
			res.getResolution().setY(0);
			res.setCollision(false, true, false);
		}
				
		return res;
		
		/*TODO pseudocode alert!
		
		store collision resolution as null
		for: each face of the map
			for: each of the vertices facing in the direction of movement
				if: line from the vertex to the the delta is in the face 
					calculate the resolution of the position to the face
					if: resolution is shorter than the stored resolution
						set the stored resolution
					endif
				endif
			endfor
		endfor
		
		 */
	}
}
