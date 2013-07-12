package tang.helper.obj;


public class TextureMap {
	private int textureId;
	
	//there's more stuff
	//http://en.wikipedia.org/wiki/Wavefront_.obj_file#File_format

	/*
	 * So, blender cannot do relative paths? So if a texture exists in a directory not a child of the current
	 * directory, it will create an absolute path, but the OBJLoader is capable of relative paths
	 * I guess that I'll just have to suck it up and manually change it until I figure out what to do
	 */
	
	public TextureMap(int textureId) {
		this.textureId = textureId;
	}
	
	public int getTextureId() {
		return this.textureId;
	}
}
