package com.happyryan2.raycaster.raycaster;

import java.lang.Math;

public class Vector3D {
	public float x;
	public float y;
	public float z;

	public Vector3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D add(Vector3D vector) {
		return new Vector3D(
			this.x + vector.x,
			this.y + vector.y,
			this.z + vector.z
		);
	}
	public Vector3D sub(Vector3D vector) {
		return new Vector3D(
			this.x - vector.x,
			this.y - vector.y,
			this.z - vector.z
		);
	}
	public Vector3D scale(float scaleFactor) {
		return new Vector3D(
			this.x * scaleFactor,
			this.y * scaleFactor,
			this.z * scaleFactor
		);
	}
	public float dot(Vector3D vector) {
		return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
	}
	public Vector3D cross(Vector3D vector) {
		return new Vector3D(
			(this.y * vector.z) - (this.z * vector.y),
			(this.z * vector.x) - (this.x * vector.z),
			(this.x * vector.y) - (this.y * vector.x)
		);
	}

	public float magnitude() {
		float distSq = ((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
		return (float) Math.sqrt((float) distSq);
	}
	public Point3D point() {
		return new Point3D(this.x, this.y, this.z);
	}
}
