package com.happyryan2.raycaster.utilities;

import com.happyryan2.raycaster.raycaster.Point3D;
import com.happyryan2.raycaster.raycaster.RayCaster;

import java.awt.Point;
import java.lang.Math;

import com.happyryan2.raycaster.raycaster.Point3D;
import com.happyryan2.raycaster.raycaster.Vector3D;

public class Utils {
	private static final double EPSILON = 0.0000001;
	public static float dist3d(Point3D a, Point3D b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		float distSq = (dx * dx) + (dy * dy) + (dz * dz);
		return (float) Math.sqrt(distSq);
	}
	public static float dist3dSq(Point3D a, Point3D b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		float distSq = (dx * dx) + (dy * dy) + (dz * dz);
		return distSq;
	}
	public static Point rotate2d(float x, float y, double deg) {
		double rad = deg / 180 * Math.PI;
		// return new Point(
		//	 (int) Math.round((double) (x * ((double) RayCaster.cosTable.get(Math.round((int) deg))) - y * (double) RayCaster.sinTable.get(Math.round((int) deg)))),
		//	 (int) Math.round((double) (x * ((double) RayCaster.sinTable.get(Math.round((int) deg))) + y * (double) RayCaster.cosTable.get(Math.round((int) deg))))
		// ); // uses tables
		return new Point(
			(int) Math.round(x * Math.cos(rad) - y * Math.sin(rad)),
			(int) Math.round(x * Math.sin(rad) + y * Math.cos(rad))
		);
	}
	public static Point3D rotate3d(float x, float y, float z, float yaw, float pitch, float roll) {
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

		// double x1 = x * Math.cos(roll) - y * Math.sin(roll);
		// double y1 = x * Math.sin(roll) + y * Math.cos(roll);
		// double z1 = z;
		//
		// double z2 = z1 * Math.cos(yaw) - x1 * Math.sin(yaw);
		// double x2 = z1 * Math.sin(yaw) + x1 * Math.cos(yaw);
		// double y2 = y1;
		//
		// double y3 = y2 * Math.cos(pitch) - z2 * Math.sin(pitch);
		// double z3 = y2 * Math.sin(pitch) + z2 * Math.cos(pitch);
		// double x3 = x2;

		return new Point3D((float) x3, (float) y3, (float) z3);
	}
	public static Point3D rayTriangleIntersection(Point3D rayOrigin, Vector3D rayVector, Point3D vertex0, Point3D vertex1, Point3D vertex2) {
		Vector3D edge1 = (vertex1.vector()).sub(vertex2.vector());
		Vector3D edge2 = (vertex2.vector()).sub(vertex0.vector());
		Vector3D h = new Vector3D();
		Vector3D s = new Vector3D();
		Vector3D q = new Vector3D();
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
	public static boolean segmentIntersectsTriangle(Point3D a, Point3D b, Point3D x, Point3D y, Point3D z) {
		/*
		Returns true if line segment 'ab' intersects triangle 'xyz'.
		*/
		Point3D rayOrigin = a;
		Vector3D rayVector = (b.vector().sub(a.vector()));
		Point3D intersection = rayTriangleIntersection(rayOrigin, rayVector, x, y, z);
		if(intersection == null) {
			return false;
		}
		else {
			// the ray intersects the triangle, so check to see if the intersection is close enough to be on the segment
			float maxDist = dist3dSq(a, b); // length of segment
			float dist = dist3dSq(a, intersection); // length between point of ray intersection and beginning of ray (one endpoint of segment)
			return (dist < maxDist);
		}
	}
}
