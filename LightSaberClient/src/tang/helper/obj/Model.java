package tang.helper.obj;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import tang.helper.struct.Heading;
import tang.helper.struct.Vector3;

public class Model implements Comparable<Model> {

	public List<Vector3> vertices;
	public List<Vector3> normals;
	public List<TextureCoordinates> texVertices;
	public List<Face> faces;
	public String name;
	
	public Model() {
		this.vertices = new ArrayList<Vector3>();
		this.normals = new ArrayList<Vector3>();
		this.texVertices = new ArrayList<TextureCoordinates>();
		this.faces = new ArrayList<Face>();
	}
	
	public void draw() {
		this.draw(new Vector3(), new Heading());
	}

	public void draw(Vector3 position, Heading rotation) {
//		Axis.draw(pos, heading);

//		glCallList(displayList);
		glPushMatrix();

		glTranslatef(position.x, position.y, position.z);
		glRotatef(rotation.roll,  1f, 0f, 0f);
		glRotatef(rotation.yaw,   0f, 1f, 0f);
		glRotatef(rotation.pitch, 0f, 0f, 1f);
		glScalef(15, 15, 15);

//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

//		glColor3f(1.0f, 1.0f, 1.0f);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		for(Face face : this.faces) {
			if(face.getMaterial() != null) {
				//TODO make this betterer
				Color diffuseColor = face.getMaterial().getDiffuseColor();
				if(diffuseColor != null) {
					glColor3f(diffuseColor.getRed() / 255.0f, diffuseColor.getGreen() / 255.0f, diffuseColor.getBlue() / 255.0f);
				}
				
				if(face.getMaterial().getDiffuseMap() != null) {
					glEnable(GL_TEXTURE_2D);
		            glBindTexture(GL_TEXTURE_2D, face.getMaterial().getDiffuseMap().getTextureId());
				}
			}
			
			if(face.isQuad()) {
				glBegin(GL_QUADS);
				Vector3 n1 = normals.get(face.normal.getA() - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				if(face.texture != null) {
					TextureCoordinates t1 = texVertices.get(face.texture.getA() - 1);
					glTexCoord2f(t1.getU(), t1.getV());
				}
				Vector3 v1 = vertices.get(face.vertex.getA() - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				

				Vector3 n2 = normals.get(face.normal.getB() - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				if(face.texture != null) {
					TextureCoordinates t2 = texVertices.get(face.texture.getB() - 1);
					glTexCoord2f(t2.getU(), t2.getV());
				}
				Vector3 v2 = vertices.get(face.vertex.getB() - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				

				Vector3 n3 = normals.get(face.normal.getC() - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				if(face.texture != null) {
					TextureCoordinates t3 = texVertices.get(face.texture.getC() - 1);
					glTexCoord2f(t3.getU(), t3.getV());
				}
				Vector3 v3 = vertices.get(face.vertex.getC() - 1);
				glVertex3f(v3.x, v3.y, v3.z);
				

				Vector3 n4 = normals.get(face.normal.getD() - 1);
				glNormal3f(n4.x, n4.y, n4.z);
				if(face.texture != null) {
					TextureCoordinates t4 = texVertices.get(face.texture.getD() - 1);
					glTexCoord2f(t4.getU(), t4.getV());
				}
				Vector3 v4 = vertices.get(face.vertex.getD() - 1);
				glVertex3f(v4.x, v4.y, v4.z);
				
				glEnd();
			} else {
				glBegin(GL_TRIANGLES);
				Vector3 n1 = normals.get(face.normal.getA() - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				if(face.texture != null) {
					TextureCoordinates t1 = texVertices.get(face.texture.getA() - 1);
					glTexCoord2f(t1.getU(), t1.getV());
				}
				Vector3 v1 = vertices.get(face.vertex.getA() - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				

				Vector3 n2 = normals.get(face.normal.getB() - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				if(face.texture != null) {
					TextureCoordinates t2 = texVertices.get(face.texture.getB() - 1);
					glTexCoord2f(t2.getU(), t2.getV());
				}
				Vector3 v2 = vertices.get(face.vertex.getB() - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				

				Vector3 n3 = normals.get(face.normal.getC() - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				if(face.texture != null) {
					TextureCoordinates t3 = texVertices.get(face.texture.getC() - 1);
					glTexCoord2f(t3.getU(), t3.getV());
				}
				Vector3 v3 = vertices.get(face.vertex.getC() - 1);
				glVertex3f(v3.x, v3.y, v3.z);
				
				glEnd();
			}
			
			glDisable(GL_TEXTURE_2D);
			glColor3f(1.0f, 1.0f, 1.0f);

		}
		
		
		
		glDisable(GL_CULL_FACE);
		
		glPopMatrix();
	}
	
	/**
	 * Compare two models based on name (filename)
	 */
	@Override
	public int compareTo(Model model) {
		if(this.name.equals(model.name)) {
			return 0;
		} else {
			return 1;
		}
	}
}
