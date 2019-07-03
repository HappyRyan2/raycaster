package com.happyryan2.raycaster.raycaster;

import java.lang.Math;

public class Vector3d {
	public float x;
	public float y;
	public float z;

	public Vector3d() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	public Vector3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3d add(Vector3d vector) {
		return new Vector3d(
			this.x + vector.x,
			this.y + vector.y,
			this.z + vector.z
		);
	}
	public Vector3d sub(Vector3d vector) {
		return new Vector3d(
			this.x - vector.x,
			this.y - vector.y,
			this.z - vector.z
		);
	}
	public Vector3d scale(float scaleFactor) {
		return new Vector3d(
			this.x * scaleFactor,
			this.y * scaleFactor,
			this.z * scaleFactor
		);
	}
	public float dot(Vector3d vector) {
		return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
	}
	public Vector3d cross(Vector3d vector) {
		return new Vector3d(
			(this.y * vector.z) - (this.z * vector.y),
			(this.z * vector.x) - (this.x * vector.z),
			(this.x * vector.y) - (this.y * vector.x)
		);
	}

	public float magnitude() {
		float distSq = ((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
		return (float) Math.sqrt((float) distSq);
	}
	public Point3d point() {
		return new Point3d(this.x, this.y, this.z);
	}
}
