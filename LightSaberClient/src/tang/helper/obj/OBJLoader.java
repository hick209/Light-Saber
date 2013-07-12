package tang.helper.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import tang.helper.struct.Vector3;
import tang.helper.utils.Console;


public class OBJLoader {
	
	public static Model loadModel(File f) throws FileNotFoundException, IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		Model m = new Model();
		m.name = f.getParent() + "/" + f.getName(); //this could be very kludgey
		String line;
		List<Material> materials = null;
		
		Material currentMaterial = null;
		while((line=reader.readLine()) != null) {
			line = line.trim();
			while(line.contains("  ")) {
				line = line.replace("  ", " ");
			}
			
			if(line.startsWith("mtllib ")) {
				//loads up all the materials used in the file. this should theoretically appear first
				materials = loadMaterialsFromMtl(new File(f.getParent() + "/" + line.split(" ")[1]));
				
			} else if(line.startsWith("usemtl ")) {
				//set the currently used material for all future faces, until another is defined
				currentMaterial = findMaterialByName(line.split(" ")[1], materials);
				
			} else if(line.startsWith("v ")) {
				//defines a vertex point
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.vertices.add(new Vector3(x,y,z));
				
			} else if(line.startsWith("vn ")) {
				//defines a normal of a face
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.normals.add(new Vector3(x,y,z));
				
			} else if(line.startsWith("vt ")) {
				//defines a uv texture location
				float u = Float.valueOf(line.split(" ")[1]);
				float v = 1.0f - Float.valueOf(line.split(" ")[2]);	//texture y coordinates need to be flipped
				m.texVertices.add(new TextureCoordinates(u, v));
				
			} else if(line.startsWith("f ")) {
				//defines a face on the object (triangle or quad, with a material)
				String[] data = line.split(" ");

				try {
					FaceIndices vertexIndicies = new FaceIndices(
							Integer.valueOf(data[1].split("/")[0]), 
							Integer.valueOf(data[2].split("/")[0]), 
							Integer.valueOf(data[3].split("/")[0])
							);
					if(data.length - 1 == 4) {
						vertexIndicies.setD(Integer.valueOf(data[4].split("/")[0]));
					}

					FaceIndices textureIndicies = null;
					try {
						textureIndicies = new FaceIndices(
								Integer.valueOf(data[1].split("/")[1]), 
								Integer.valueOf(data[2].split("/")[1]), 
								Integer.valueOf(data[3].split("/")[1])
								);
						if(data.length - 1 == 4) {
							textureIndicies.setD(Integer.valueOf(data[4].split("/")[1]));
						}
					} catch(NumberFormatException e) {
						//just ignore if there's no texture coordinates to parse
					}

					FaceIndices normalIndicies = new FaceIndices(
							Integer.valueOf(data[1].split("/")[2]), 
							Integer.valueOf(data[2].split("/")[2]), 
							Integer.valueOf(data[3].split("/")[2])
							);
					if(data.length - 1 == 4) {
						normalIndicies.setD(Integer.valueOf(data[4].split("/")[2]));
					}

					m.faces.add(new Face(vertexIndicies, normalIndicies, textureIndicies, currentMaterial));
					
				} catch(Exception e) {
					Console.warn(m.name + ": could not parse line, \"" + line + "\"");
//					e.printStackTrace();
				}
			}
		}
		reader.close();
		materials = null;

		Console.info(m.name + " loaded, " + m.vertices.size() + " vertices, " + m.normals.size() + " normals, " + m.faces.size() + " faces, " + m.texVertices.size() + " texture coordinates");

		return m;
	}

	//TODO probably not the best idea implementing my own search; i should try and maybe use a HashMap
	private static Material findMaterialByName(String materialName, List<Material> materials) {
		for(Material material : materials) {
			if(material.getName().equals(materialName)) {
				return material;
			}
		}
		return null;
	}

	private static List<Material> loadMaterialsFromMtl(File f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));

		List<Material> materials = new ArrayList<Material>();

		String line;
		while((line=reader.readLine()) != null) {
			if(line.startsWith("newmtl ")) {
				Material material = new Material(line.split(" ")[1]);
				
				//this assumes that materials are separated by an empty line
				while((line=reader.readLine()) != null && !line.equals("")) {
					line = line.trim();
					while(line.contains("  ")) {
						line = line.replace("  ", " ");
					}
					
					
					if(line.startsWith("Ka ")) {
						//ambient color
						int r = (int) (Float.valueOf(line.split(" ")[1])*256);
						int g = (int) (Float.valueOf(line.split(" ")[2])*256);
						int b = (int) (Float.valueOf(line.split(" ")[3])*256);
						material.setAmbientColor(new Color(r, g, b));
						
					} else if(line.startsWith("Kd ")) {
						//diffuse color
						int r = (int) (Float.valueOf(line.split(" ")[1])*256);
						int g = (int) (Float.valueOf(line.split(" ")[2])*256);
						int b = (int) (Float.valueOf(line.split(" ")[3])*256);
						material.setDiffuseColor(new Color(r, g, b));
						
					} else if(line.startsWith("Ks ")) {
						//specular color
						int r = (int) (Float.valueOf(line.split(" ")[1])*256);
						int g = (int) (Float.valueOf(line.split(" ")[2])*256);
						int b = (int) (Float.valueOf(line.split(" ")[3])*256);
						material.setSpecularColor(new Color(r, g, b));
						
					} else if(line.startsWith("Ns ")) {
						//specular coefficient
						material.setSpecularCoefficient(Float.valueOf(line.split(" ")[1]));
						
					} else if(line.startsWith("d ") || line.startsWith("Tr ")) {
						//dissolve, or transparency
						material.setDissolve(Float.valueOf(line.split(" ")[1]));
						
					} else if(line.startsWith("illum ")) {
						//illumination model
						material.setIlluminationModel(Integer.valueOf(line.split(" ")[1]));
						
//					} else if(line.startsWith("map_Kd ")) {
//						//diffuse map
//						String textureName = line.split(" ")[1];
//						Texture texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(f.getParent() + "/" + textureName)));
//						TextureMap diffuseMap = new TextureMap(texture.getTextureID());
//						material.setDiffuseMap(diffuseMap);
//						Console.debug(textureName + " => assigned ID " + texture.getTextureID());
					}
					//TODO: support all texture maps in Material

				}
				materials.add(material);
			} 
		}
		reader.close();
		
		Console.debug(f.getName() + " loaded, " + materials.size() + " materials found");
		
		return materials;
	}
}
