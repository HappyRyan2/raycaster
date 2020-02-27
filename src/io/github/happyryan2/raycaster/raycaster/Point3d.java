package io.github.happyryan2.raycaster.raycaster;

import io.github.happyryan2.raycaster.utilities.Utils;

public class Point3d {
	public float x;
	public float y;
	public float z;

	public Point3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Point3d(Point3d copy) {
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
	}

	public boolean equals(Point3d point) {
		return (this.x == point.x && this.y == point.y && this.z == point.z);
	}

	public Point3d rotateX(double deg) {
		/*
		Returns the new coordinates of this point after being rotated 'deg' degrees about the x-axis.
		*/
		Point2d newPos = Utils.rotate2d(this.y, this.z, deg);
		return new Point3d(
			this.x,
			newPos.x,
			newPos.y
		);
	}
	public Point3d rotateY(double deg) {
		/*
		Returns the new coordinates of this point after being rotated 'deg' degrees about the y-axis.
		*/
		Point2d newPos = Utils.rotate2d(this.x, this.z, deg);
		return new Point3d(
			newPos.x,
			this.y,
			newPos.y
		);
	}
	public Point3d rotateZ(double deg) {
		/*
		Returns the new coordinates of this point after being rotated 'deg' degrees about the z-axis.
		*/
		Point2d newPos = Utils.rotate2d(this.x, this.y, deg);
		return new Point3d(
			newPos.x,
			newPos.y,
			this.z
		);
	}

	public Vector3d vector() {
		/*
		Convert to a Vector3d
		*/
		return new Vector3d(this.x, this.y, this.z);
	}
	public String toString() {
		/*
		Convert to a string for debugging
		*/
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
	public String toString(boolean rounded) {
		/*
		Convert to a string for debugging (coordinates rounded to nearest 0.01)
		*/
		if(rounded) {
			float rX = Math.round(this.x * 1000) / 1000.0f;
			float rY = Math.round(this.y * 1000) / 1000.0f;
			float rZ = Math.round(this.z * 1000) / 1000.0f;
			return new Point3d(rX, rY, rZ).toString();
		}
		else {
			return this.toString();
		}
	}
}
