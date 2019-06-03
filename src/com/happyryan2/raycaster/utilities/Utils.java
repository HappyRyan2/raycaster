package com.happyryan2.raycaster.utilities;

import com.happyryan2.raycaster.raycaster.Point3D;
import com.happyryan2.raycaster.raycaster.RayCaster;

import java.awt.Point;
import java.lang.Math;

public class Utils {
    public static Point rotate2d(float x, float y, double deg) {
        double rad = deg / 180 * Math.PI;
        // return new Point(
        //     (int) Math.round((double) (x * ((double) RayCaster.cosTable.get(Math.round((int) deg))) - y * (double) RayCaster.sinTable.get(Math.round((int) deg)))),
        //     (int) Math.round((double) (x * ((double) RayCaster.sinTable.get(Math.round((int) deg))) + y * (double) RayCaster.cosTable.get(Math.round((int) deg))))
        // ); // uses tables
        return new Point(
            (int) (x * Math.cos(rad) - y * Math.sin(rad)),
            (int) (x * Math.sin(rad) + y * Math.cos(rad))
        );
    }
    public static Point3D rotate3d(float x, float y, float z, float yaw, float pitch, float roll) {
        pitch = (float) (pitch / 180 * Math.PI);
        yaw = (float) (yaw / 180 * Math.PI);
        roll = (float) (roll / 180 * Math.PI);

        // double x1 = x * (double) (RayCaster.cosTable.get(Math.round(roll))) - y * (double) (RayCaster.sinTable.get(Math.round(roll)));
        // double y1 = x * (double) (RayCaster.sinTable.get(Math.round(roll))) + y * (double) (RayCaster.cosTable.get(Math.round(roll)));
        // double z1 = z;

        // double z2 = z1 * (double) (RayCaster.cosTable.get(Math.round(yaw))) - x1 * (double) (RayCaster.sinTable.get(Math.round(yaw)));
        // double x2 = z1 * (double) (RayCaster.sinTable.get(Math.round(yaw))) + x1 * (double) (RayCaster.cosTable.get(Math.round(yaw)));
        // double y2 = y1;

        // double y3 = y2 * (double) (RayCaster.cosTable.get(Math.round(pitch))) - z2 * (double) (RayCaster.sinTable.get(Math.round(pitch)));
        // double z3 = y2 * (double) (RayCaster.sinTable.get(Math.round(pitch))) + z2 * (double) (RayCaster.cosTable.get(Math.round(pitch)));
        // double x3 = x2;

        double x1 = x * Math.cos(roll) - y * Math.sin(roll);
        double y1 = x * Math.sin(roll) + y * Math.cos(roll);
        double z1 = z;
        
        double z2 = z1 * Math.cos(yaw) - x1 * Math.sin(yaw);
        double x2 = z1 * Math.sin(yaw) + x1 * Math.cos(yaw);
        double y2 = y1;
        
        double y3 = y2 * Math.cos(pitch) - z2 * Math.sin(pitch);
        double z3 = y2 * Math.sin(pitch) + z2 * Math.cos(pitch);
        double x3 = x2;

        return new Point3D((float) x3, (float) y3, (float) z3);
    }
}
