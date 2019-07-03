package com.happyryan2.raycaster.raycaster;

// when facing 0 degrees, slope of first ray is -5, -5, 1
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.lang.Math;

import com.happyryan2.raycaster.game.Player;
import com.happyryan2.raycaster.game.LevelDemo;
import com.happyryan2.raycaster.utilities.Utils;
import com.happyryan2.raycaster.utilities.Screen;

public class RayCaster {
	public static List content = new ArrayList(); // a list containing complex objects - cube, polyhedron, cylinder, etc.
	public static List<Triangle3d> triangularContent = new ArrayList(); // a list containing 3-dimensional triangles
	public static int relX;
	public static int relY;
	public static int visualX;
	public static int visualY;
	public static double rayX;
	public static double rayY;
	public static double rayZ;
	public static List<Triangle3d> triangularContentCopy = new ArrayList();
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
		if(true) { return; }
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
		System.out.println("finished rendering");
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
		/*
		1. Calculate the angle at which you are facing:
		let a = atan2(x, z).inDegrees()
		2. Turn your head horizontally until your nose is on the x-axis.
		let onAxis = OP.rotate2d(a)
		3. Rotate on the xy axis by yaw degrees.
		let rotatedToYaw = onAxis.rotate2d(yaw)
		4. Turn your head horizontally until your nose is back where it started, but lower / higher. (inverse of step 2)
		let rotatedToYaw2 = rotatedToYaw.rotate2d(-a);
		5. Rotate according to horizontal rotation value.
		return rotatedToYaw2.rotate2d(pitch);
		*/
		Point3d beforeRotation = new Point3d(x, y, z);
		double angleRad = Math.atan2(z, x);
		double angleDeg = Math.toDegrees(angleRad);
		Point3d onAxis = beforeRotation.rotateY(-angleDeg); // x >= 0 and z = 0. always on the positive x-axis
		Point3d rotatedToViewY = onAxis.rotateZ(-Player.viewY);
		Point3d offAxis = rotatedToViewY.rotateY(angleDeg);
		Point3d afterRotation = offAxis.rotateY(-Player.viewX);
		rayX = afterRotation.x;
		rayY = afterRotation.y;
		rayZ = afterRotation.z;
		System.out.println("rotated point: (" + rayX + ", " + rayY + ", " + rayZ + ")");
		// raycast(g, afterRotation.x, afterRotation.y, afterRotation.z);
	}
	public static void raycast(Graphics g, double dirX, double dirY, double dirZ) {
		/* Create the first branch of a k-d tree (makes it ~8 times faster by removing 7/8ths of all the triangles)*/
		createKDTree();
		// System.out.println("number of triangles: " + triangularContentCopy.size());
		/* Make a list of all triangle intersections for this ray */
		List<Point3d> intersections = new ArrayList();
		List<Triangle3d> triangles = new ArrayList();
		for(int i = 0; i < triangularContent.size(); i ++) {
			Triangle3d tri = triangularContent.get(i);
			if(tri == null) {
				System.out.println("something went wrong...");
				continue;
			}
			Point3d intersection = Utils.rayTriangleIntersection(new Point3d(Player.x, Player.y, Player.z), new Vector3d((float) dirX, (float) dirY, (float) dirZ), tri.a, tri.b, tri.c);
			if(intersection == null) {
				continue;
			}
			intersections.add(intersection);
			triangles.add(tri);
			System.out.println("HIT SOMETHING");
		}
		/* Display intersected triangle's color on the screen */
		if(intersections.size() == 0) {
			// the ray doesn't intersect anything
			float rX = Math.round(dirX * 100) / 100.0f;
			float rY = Math.round(dirY * 100) / 100.0f;
			float rZ = Math.round(dirZ * 100) / 100.0f;
			return;
		}
		else if(intersections.size() == 1) {
			// the ray intersects one object, so render it
			Triangle3d tri = triangles.get(0);
			Color col = tri.color;
			g.setColor(col);
			g.fillRect(relX, relY, (int) (800 / Player.screenSize), (int) (800 / Player.screenSize));
		}
		else {
			/* the ray intersects multiple objects, so render the first one */
			int lowestIndex = 0;
			float lowestDistSq = 0;
			for(int i = 0; i < intersections.size(); i ++) {
				Point3d intersection = intersections.get(i);
				float dx = intersection.x - Player.x;
				float dy = intersection.y - Player.y;
				float dz = intersection.z - Player.z;
				float distSq = (dx * dx) + (dy * dy) + (dz * dz);
				if(distSq < lowestDistSq) {
					lowestIndex = i;
					lowestDistSq = distSq;
				}
			}
			Triangle3d tri = triangles.get(lowestIndex);
			Color col = tri.color;
			g.setColor(col);
			g.fillRect(relX, relY, (int) (800 / Player.screenSize), (int) (800 / Player.screenSize));
		}
	}
	public static void createKDTree() {
		/*
		Creates only the first branch of a kd-tree (non-recursively) by removing triangles that we know won't intersect the ray since they are on opposite sides of an axis.
		*/
		triangularContentCopy = new ArrayList();
		for(int i = 0; i < triangularContent.size(); i ++) {
			Triangle3d original = triangularContent.get(i);
			Triangle3d copy = new Triangle3d(original);
			System.out.println("old: " + original.toString());
			System.out.println("new: " + copy.toString());
			triangularContentCopy.add(copy);
		}
		// triangularContentCopy = triangularContent;
		if(true) { return; }
		// if(rayX < 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.x > Player.x && tri.b.x > Player.x && tri.c.x > Player.x) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
		// else if(rayX > 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.x < Player.x && tri.b.x < Player.x && tri.c.x < Player.x) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
		// if(rayY < 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.y > Player.y && tri.b.y > Player.y && tri.c.y > Player.y) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
		// else if(rayY > 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.y < Player.y && tri.b.y < Player.y && tri.c.y < Player.y) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
		// if(rayZ < 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.z > Player.z && tri.b.z > Player.z && tri.c.z > Player.z) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
		// else if(rayZ > 0) {
		// 	for(int i = 0; i < triangularContentCopy.size(); i ++) {
		// 		Triangle3d tri = triangularContentCopy.get(i);
		// 		if(tri.a.z < Player.z && tri.b.z < Player.z && tri.c.z < Player.z) {
		// 			triangularContentCopy.remove(i);
		// 		}
		// 	}
		// }
	};

	public static void triangle(Point3d a, Point3d b, Point3d c) {
		triangularContent.add(new Triangle3d(a, b, c));
		triangularContent.get(triangularContent.size() - 1).color = polyhedronColor;
	}
	public static void quadrilateral(Point3d a, Point3d b, Point3d c, Point3d d) {
		triangle(a, b, c);
		triangle(a, d, c);
	}
	public static void cube(int x, int y, int z, int w, int h, int d) {
		// front & back = z, left & right = x
		int left = x;
		int right = x + w;
		int top = y;
		int bottom = y + h;
		int front = z;
		int back = z + d;
		quadrilateral(new Point3d(left, top, front), new Point3d(right, top, front), new Point3d(right, bottom, front), new Point3d(left, bottom, front)); // front face
		quadrilateral(new Point3d(left, top, back), new Point3d(right, top, back), new Point3d(right, bottom, back), new Point3d(left, bottom, back)); // back face
		quadrilateral(new Point3d(left, top, front), new Point3d(left, bottom, front), new Point3d(left, bottom, back), new Point3d(left, top, back)); // left face
		quadrilateral(new Point3d(right, top, front), new Point3d(right, bottom, front), new Point3d(right, bottom, back), new Point3d(right, top, back)); // left face
		quadrilateral(new Point3d(left, top, front), new Point3d(right, top, front), new Point3d(right, top, back), new Point3d(left, top, back)); // top face
		quadrilateral(new Point3d(left, bottom, front), new Point3d(right, bottom, front), new Point3d(right, bottom, back), new Point3d(left, bottom, back)); // bottom face
	}
}
