package io.github.happyryan2.raycaster.utilities;

import io.github.happyryan2.raycaster.raycaster.Point3d;
import io.github.happyryan2.raycaster.raycaster.RayCaster;

import java.awt.Point;
import java.lang.Math;

import io.github.happyryan2.raycaster.raycaster.Point3d;
import io.github.happyryan2.raycaster.raycaster.Point2d;
import io.github.happyryan2.raycaster.raycaster.Vector3d;

public class Utils {
	private static final double EPSILON = 0.0000001;
	public static float dist3d(Point3d a, Point3d b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		float distSq = (dx * dx) + (dy * dy) + (dz * dz);
		return (float) Math.sqrt(distSq);
	}
	public static float dist3dSq(Point3d a, Point3d b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		float distSq = (dx * dx) + (dy * dy) + (dz * dz);
		return distSq;
	}
	public static Point2d rotate2d(double x, double y, double deg) {
		double rad = deg / 180 * Math.PI;
		// return new Point(
		//	 (int) Math.round((double) (x * ((double) RayCaster.cosTable.get(Math.round((int) deg))) - y * (double) RayCaster.sinTable.get(Math.round((int) deg)))),
		//	 (int) Math.round((double) (x * ((double) RayCaster.sinTable.get(Math.round((int) deg))) + y * (double) RayCaster.cosTable.get(Math.round((int) deg))))
		// ); // uses tables
		return new Point2d(
			(float) (x * Math.cos(rad) - y * Math.sin(rad)),
			(float) (x * Math.sin(rad) + y * Math.cos(rad))
		);
	}
	public static Point3d rotate3d(float x, float y, float z, float yaw, float pitch, float roll) {
		if(!RayCaster.initialized) {
			RayCaster.initTrig();
		}
		while(pitch < 0) {
			pitch += 360;
		}
		while(pitch >= 360) {
			pitch -= 360;
		}
		while(yaw < 0) {
			yaw += 360;
		}
		while(yaw >= 360) {
			yaw -= 360;
		}
		while(roll < 0) {
			roll += 360;
		}
		while(roll >= 360) {
			roll -= 360;
		}
		double x1 = x * (double) (RayCaster.cosTable.get(Math.round(roll))) - y * (double) (RayCaster.sinTable.get(Math.round(roll)));
		double y1 = x * (double) (RayCaster.sinTable.get(Math.round(roll))) + y * (double) (RayCaster.cosTable.get(Math.round(roll)));
		double z1 = z;

		double z2 = z1 * (double) (RayCaster.cosTable.get(Math.round(yaw))) - x1 * (double) (RayCaster.sinTable.get(Math.round(yaw)));
		double x2 = z1 * (double) (RayCaster.sinTable.get(Math.round(yaw))) + x1 * (double) (RayCaster.cosTable.get(Math.round(yaw)));
		double y2 = y1;

		double y3 = y2 * (double) (RayCaster.cosTable.get(Math.round(pitch))) - z2 * (double) (RayCaster.sinTable.get(Math.round(pitch)));
		double z3 = y2 * (double) (RayCaster.sinTable.get(Math.round(pitch))) + z2 * (double) (RayCaster.cosTable.get(Math.round(pitch)));
		double x3 = x2;

		return new Point3d((float) x3, (float) y3, (float) z3);
	}
	public static Point3d rayTriangleIntersection(Point3d rayOrigin, Vector3d rayVector, Point3d vertex0, Point3d vertex1, Point3d vertex2) {
		Vector3d edge1 = (vertex1.vector()).sub(vertex2.vector());
		Vector3d edge2 = (vertex2.vector()).sub(vertex0.vector());
		Vector3d h = new Vector3d();
		Vector3d s = new Vector3d();
		Vector3d q = new Vector3d();
		double a, f, u, v;
		edge1 = vertex1.vector().sub(vertex0.vector());
		edge2 = vertex2.vector().sub(vertex0.vector());
		h = rayVector.cross(edge2);
		a = edge1.dot(h);
		if(a > -EPSILON && a < EPSILON) {
			return null;
		}
		f = 1.0 / a;
		s = rayOrigin.vector().sub(vertex0.vector());
		u = f * (s.dot(h));
		if(u < 0.0 || u > 1.0) {
			return null;
		}
		q = s.cross(edge1);
		v = f * rayVector.dot(q);
		if(v < 0.0 || u + v > 1.0) {
			return null;
		}
		double t = f * edge2.dot(q);
		if(t > EPSILON) {
			return rayOrigin.vector().add(rayVector.scale((float) t)).point();
		}
		else {
			return null;
		}
	}
	public static boolean segmentIntersectsTriangle(Point3d a, Point3d b, Point3d x, Point3d y, Point3d z) {
		/*
		Returns true if line segment 'ab' intersects triangle 'xyz'.
		*/
		Point3d rayOrigin = a;
		Vector3d rayVector = (b.vector().sub(a.vector()));
		Point3d intersection = rayTriangleIntersection(rayOrigin, rayVector, x, y, z);
		if(intersection == null) {
			return false;
		}
		else {
			/* the ray intersects the triangle, so check to see if the intersection is close enough to be on the segment */
			float maxDist = dist3dSq(a, b); // length of segment
			float dist = dist3dSq(a, intersection); // length between point of ray intersection and beginning of ray (one endpoint of segment)
			return (dist < maxDist);
		}
	}
}
