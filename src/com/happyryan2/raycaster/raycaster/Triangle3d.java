package com.happyryan2.raycaster.raycaster;

public class Triangle3d {
	public Point3d a; // a, b, and c are the triangle's vertices
	public Point3d b;
	public Point3d c;
	public java.awt.Color color; // the color of the triangle / polyhedron

	public Triangle3d(Point3d a, Point3d b, Point3d c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public Triangle3d(Triangle3d copy) {
		this.a = new Point3d(copy.a);
		this.b = new Point3d(copy.b);
		this.c = new Point3d(copy.c);
	}

	public boolean equals(Triangle3d tri) {
		return (this.a.equals(tri.a) && this.b.equals(tri.b) && this.c.equals(tri.c));
	}

	public String toString() {
		return "A: " + this.a.toString() + ", B: " + this.b.toString() + ", C: " + this.c.toString();
	}
}
