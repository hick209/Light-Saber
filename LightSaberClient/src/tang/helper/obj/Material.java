package tang.helper.obj;

import org.lwjgl.util.Color;



public class Material {
	private String name;
	private Color Ka, Kd, Ks;
	private float Ns;
	private float d;
	private int illum;
	private TextureMap map_Ka, map_Kd, map_Ks, map_Ns, map_d, map_bump;

	public Material(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Color getAmbientColor() {
		return Ka;
	}
	public void setAmbientColor(Color ambient) {
		this.Ka = ambient;
	}
	public Color getDiffuseColor() {
		return Kd;
	}
	public void setDiffuseColor(Color diffuse) {
		this.Kd = diffuse;
	}
	public Color getSpecularColor() {
		return Ks;
	}
	public void setSpecularColor(Color specular) {
		this.Ks = specular;
	}
	public float getSpecularCoefficient() {
		return Ns;
	}
	public void setSpecularCoefficient(float specularCoefficient) {
		this.Ns =
				Math.max(
				0.0f,
				Math.min(specularCoefficient,
				1000.0f));
	}
	public float getTransparency() {
		return d;
	}
	public void setTransparency(float transparency) {
		this.d = transparency;
	}
	public float getDissolve() {
		return this.getTransparency();
	}
	public void setDissolve(float dissolve) {
		this.setTransparency(dissolve);
	}
	public int getIlluminationModel() {
		return illum;
	}
	public void setIlluminationModel(int illuminationModel) {
		this.illum =
				Math.max(
				0,
				Math.min(illuminationModel,
				10));
	}
	
	public TextureMap getAmbientMap() {
		return map_Ka;
	}

	public void setAmbientMap(TextureMap ambientMap) {
		this.map_Ka = ambientMap;
	}

	public TextureMap getDiffuseMap() {
		return map_Kd;
	}

	public void setDiffuseMap(TextureMap diffuseMap) {
		this.map_Kd = diffuseMap;
	}

	public TextureMap getSpecularMap() {
		return map_Ks;
	}

	public void setSpecularMap(TextureMap specularMap) {
		this.map_Ks = specularMap;
	}

	public TextureMap getSpecularHighlightMap() {
		return map_Ns;
	}

	public void setSpecularHighlightMap(TextureMap specularHighlightMap) {
		this.map_Ns = specularHighlightMap;
	}

	public TextureMap getAlphaMap() {
		return map_d;
	}

	public void setAlphaMap(TextureMap alphaMap) {
		this.map_d = alphaMap;
	}

	public TextureMap getBumpMap() {
		return map_bump;
	}

	public void setBumpMap(TextureMap bumpMap) {
		this.map_bump = bumpMap;
	}
	
}
