package com.lightsaber;

import java.io.Serializable;

public class RotationVector implements Serializable {

	private static final long serialVersionUID = 22L;
	public float pitch;
	public float yaw;
	public float roll;
	public float inclination;

	public void setValues(float pitch, float yaw, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
}
