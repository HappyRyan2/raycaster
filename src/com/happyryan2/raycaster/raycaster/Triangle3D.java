package com.happyryan2.raycaster.raycaster;

public class Triangle3D {
	public Point3D a; // a, b, and c are the triangle's vertices
	public Point3D b;
	public Point3D c;
	public Triangle3D(Point3D a, Point3D b, Point3D c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public java.awt.Color color; // the color of the triangle / polyhedron
}
