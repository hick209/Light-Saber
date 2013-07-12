package tang.helper.obj;




public class Face {
	public FaceIndices vertex;
	public FaceIndices normal;
	public FaceIndices texture;
	private Material material;
	
	public Face(FaceIndices vertex, FaceIndices normal, FaceIndices texture, Material material) {
		this.vertex = vertex;
		this.normal = normal;
		this.texture = texture;
		this.material = material;
	}
	public boolean isQuad() {
		//check if the value for the D indices is not the default 3d indices value
		return vertex.getD() != -1.0f;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
}
