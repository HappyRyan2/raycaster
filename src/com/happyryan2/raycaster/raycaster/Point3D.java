package com.happyryan2.raycaster.raycaster;

public class Point3D {
	public float x;
	public float y;
	public float z;
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3D vector() {
		return new Vector3D(this.x, this.y, this.z);
	}
}
