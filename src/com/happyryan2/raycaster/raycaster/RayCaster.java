package com.happyryan2.raycaster.raycaster;

// when facing 0 degrees, slope of first ray is -5, -5, 1
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

import com.happyryan2.raycaster.game.Player;
import com.happyryan2.raycaster.game.LevelDemo;
import com.happyryan2.raycaster.utilities.Utils;

public class RayCaster {
	public static List content = new ArrayList(); // a list containing complex objects - cube, polyhedron, cylinder, etc.
	public static List<Triangle3D> triangularContent = new ArrayList(); // a list containing 3-dimensional triangles
	public static int relX;
	public static int relY;
	public static int visualX;
	public static int visualY;
	public static List sinTable = new ArrayList();
	public static List cosTable = new ArrayList();
	public static boolean initialized = false;
	public static Color polyhedronColor;

	public static void initTrig() {
		initialized = true;
		for(short deg = 0; deg < 360; deg ++) {
			double rad = (((float) deg) / 180) * Math.PI;
			sinTable.add(Math.sin(rad));
			cosTable.add(Math.cos(rad));
		}
	}
	public static void run() {
		if(!initialized) {
			initTrig();
		}
		Player.input();
	}
	public static void render(Graphics g) {
		if(triangularContent.size() != 0) {
			return;
		}
		LevelDemo.exist();
		int xIncreases = 0;
		int yIncreases = 0;
		for(float x = -Player.fov / 2; x < Player.fov / 2; x += Player.res) {
			xIncreases ++;
			yIncreases = 0;
			for(float y = -Player.fov / 2; y < Player.fov / 2; y += Player.res) {
				yIncreases ++;
				relX = (int) (xIncreases * (800 / Player.screenSize));
				relY = (int) (yIncreases * (800 / Player.screenSize));
				rotatePosToPlayerView(g, x / 10, y / 10, 0.1f);
			}
		}
		/*
		res * iterations = fov
		iterations = fov / res
		800 = fov / res
		800 * res = fov
		res = fov / 800
		*/
		triangularContent.clear();
	}
	public static void rotatePosToPlayerView(Graphics g, float x, float y, float z) {
		Point3D rotated = Utils.rotate3d(x, y, z, Math.round(Player.viewX), Math.round(Player.viewY), 0);
		raycast(g, rotated.x, rotated.y, rotated.z);
		/*

		*/
	}
	public static void raycast(Graphics g, double dirX, double dirY, double dirZ) {
		List<Point3D> intersections = new ArrayList();
		List<Triangle3D> triangles = new ArrayList();
		for(int i = 0; i < triangularContent.size(); i ++) {
			Triangle3D tri = triangularContent.get(i);
			if(tri == null) {
				continue;
			}
			Point3D intersection = Utils.rayTriangleIntersection(new Point3D(Player.x, Player.y, Player.z), new Vector3D((float) dirX, (float) dirY, (float) dirZ), tri.a, tri.b, tri.c);
			if(intersection == null) {
				continue;
			}
			intersections.add(intersection);
			triangles.add(tri);
		}
		if(intersections.size() == 0) {
			// the ray doesn't intersect anything
			float rX = Math.round(dirX * 100) / 100.0f;
			float rY = Math.round(dirY * 100) / 100.0f;
			float rZ = Math.round(dirZ * 100) / 100.0f;
			return;
		}
		else if(intersections.size() == 1) {
			// the ray intersects one object, so render it
			Triangle3D tri = triangles.get(0);
			Color col = tri.color;
			g.setColor(col);
			g.fillRect(relX, relY, (int) (800 / Player.screenSize), (int) (800 / Player.screenSize));
		}
		else {
			// the ray intersects multiple objects, so render the first one
			int lowestIndex = 0;
			float lowestDistSq = 0;
			for(int i = 0; i < intersections.size(); i ++) {
				Point3D intersection = intersections.get(i);
				float dx = intersection.x - Player.x;
				float dy = intersection.y - Player.y;
				float dz = intersection.z - Player.z;
				float distSq = (dx * dx) + (dy * dy) + (dz * dz);
				if(distSq < lowestDistSq) {
					lowestIndex = i;
					lowestDistSq = distSq;
				}
			}
			Triangle3D tri = triangles.get(lowestIndex);
			Color col = tri.color;
			g.setColor(col);
			g.fillRect(relX, relY, (int) (800 / Player.screenSize), (int) (800 / Player.screenSize));
		}
	}

	public static void triangle(Point3D a, Point3D b, Point3D c) {
		triangularContent.add(new Triangle3D(a, b, c));
		triangularContent.get(triangularContent.size() - 1).color = polyhedronColor;
	}
	public static void quadrilateral(Point3D a, Point3D b, Point3D c, Point3D d) {
		triangle(a, b, c);
		triangle(b, c, d);
	}
	public static void cube(int x, int y, int z, int w, int h, int d) {
		// front & back = z, left & right = x
		Point3D ftl = new Point3D(x, y, z);
		Point3D ftr = new Point3D(x + w, y, z);
		Point3D fbl = new Point3D(x, y + h, z);
		Point3D fbr = new Point3D(x + w, y + h, z);
		Point3D btl = new Point3D(x, y, z + d);
		Point3D btr = new Point3D(x + w, y, z + d);
		Point3D bbl = new Point3D(x, y + h, z + d);
		Point3D bbr = new Point3D(x + w, y + h, z + d);
		quadrilateral(ftl, ftr, fbr, fbl); // front face
		quadrilateral(btl, btr, bbr, bbl); // back face
		quadrilateral(ftl, fbl, bbl, btl); // left face
		quadrilateral(ftr, fbr, bbr, btr); // right face
		quadrilateral(ftl, ftr, btr, btl); // top face
		quadrilateral(fbl, fbr, bbr, bbl); // bottom face
	}
}
