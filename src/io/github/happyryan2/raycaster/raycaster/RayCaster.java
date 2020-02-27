package io.github.happyryan2.raycaster.raycaster;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.lang.Math;

import io.github.happyryan2.raycaster.game.Player;
import io.github.happyryan2.raycaster.game.LevelDemo;
import io.github.happyryan2.raycaster.utilities.Utils;
import io.github.happyryan2.raycaster.utilities.Screen;
import io.github.happyryan2.raycaster.utilities.MousePos;

public class RayCaster {
	public static List content = new ArrayList(); // a list containing complex objects - cube, polyhedron, cylinder, etc.
	public static List<Triangle3d> triangularContent = new ArrayList(); // a list containing 3-dimensional triangles
	public static List<Pixel> pixels = new ArrayList(); // used to render all pixels at once (for double buffering)

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
		pixels = new ArrayList();
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
				rotatePosToPlayerView(g, x, y, 1);
			}
		}
		triangularContent.clear();
		/* clear previous frame + draw this frame */
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, 800, 800);
		for(int i = 0; i < pixels.size(); i ++) {
			Pixel pixel = pixels.get(i);
			pixel.display(g);
		}
	}
	public static void rotatePosToPlayerView(Graphics g, float x, float y, float z) {
		Point3d beforeRotation = new Point3d(x, y, z);
		Point3d rotatedY = beforeRotation.rotateX(Player.viewY);
		Point3d rotatedX = rotatedY.rotateY(-Player.viewX);
		rayX = rotatedX.x;
		rayY = rotatedX.y;
		rayZ = rotatedX.z;
		raycast(g, rayX, rayY, rayZ);
	}
	public static void raycast(Graphics g, double dirX, double dirY, double dirZ) {
		/* Create the first branch of a k-d tree (makes it ~8 times faster by removing 7/8ths of all the triangles)*/
		// createKDTree();
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
		}
		/* Display intersected triangle's color on the screen */
		if(intersections.size() == 0) {
			// the ray doesn't intersect anything
			return;
		}
		else if(intersections.size() == 1) {
			// the ray intersects one object, so render it
			Triangle3d tri = triangles.get(0);
			pixels.add(new Pixel(new Point2d(relX, relY), tri.color));
		}
		else {
			/* the ray intersects multiple objects, so render the closest one */
			int lowestIndex = 0;
			float lowestDistSq = -1;
			for(int i = 0; i < intersections.size(); i ++) {
				Point3d intersection = intersections.get(i);
				float dx = intersection.x - Player.x;
				float dy = intersection.y - Player.y;
				float dz = intersection.z - Player.z;
				float distSq = (dx * dx) + (dy * dy) + (dz * dz);
				if(distSq < lowestDistSq || lowestDistSq == -1) {
					lowestIndex = i;
					lowestDistSq = distSq;
				}
			}
			Triangle3d tri = triangles.get(lowestIndex);
			pixels.add(new Pixel(new Point2d(relX, relY), tri.color));
		}
	}
	public static void createKDTree() {
		/*
		Creates only the first branch of a kd-tree (non-recursively) by removing triangles that we know won't intersect the ray since they are on opposite sides of an axis.
		OPTIMIZATION: this function can be run once at the beginning, creating 8 seperate arrays instead of doing it over and over again for each ray.
		*/
		triangularContentCopy = new ArrayList();
		for(int i = 0; i < triangularContent.size(); i ++) {
			Triangle3d original = triangularContent.get(i);
			Triangle3d copy = new Triangle3d(original);
			triangularContentCopy.add(copy);
		}
		if(rayX < 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.x > Player.x && tri.b.x > Player.x && tri.c.x > Player.x) {
					triangularContentCopy.remove(i);
				}
			}
		}
		else if(rayX > 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.x < Player.x && tri.b.x < Player.x && tri.c.x < Player.x) {
					triangularContentCopy.remove(i);
				}
			}
		}
		if(rayY < 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.y > Player.y && tri.b.y > Player.y && tri.c.y > Player.y) {
					triangularContentCopy.remove(i);
				}
			}
		}
		else if(rayY > 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.y < Player.y && tri.b.y < Player.y && tri.c.y < Player.y) {
					triangularContentCopy.remove(i);
				}
			}
		}
		if(rayZ < 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.z > Player.z && tri.b.z > Player.z && tri.c.z > Player.z) {
					triangularContentCopy.remove(i);
				}
			}
		}
		else if(rayZ > 0) {
			for(int i = 0; i < triangularContentCopy.size(); i ++) {
				Triangle3d tri = triangularContentCopy.get(i);
				if(tri.a.z < Player.z && tri.b.z < Player.z && tri.c.z < Player.z) {
					triangularContentCopy.remove(i);
				}
			}
		}
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
