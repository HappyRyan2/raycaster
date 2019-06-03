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
    public static List<Atom> atomicContent = new ArrayList(); // a list containing only points (atoms) w/ no dimensions - only location and color
    public static float relX;
    public static float relY;
    public static int visualX;
    public static int visualY;
    public static List sinTable = new ArrayList();
    public static List cosTable = new ArrayList();
    public static boolean initialized = false;

    public static void initTrig() {
        initialized = true;
        System.out.println("initializing trigonometry tables!");
        for(short deg = 0; deg < 360; deg ++) {
            double rad = deg / 180 * Math.PI;
            sinTable.add(Math.sin(rad));
            cosTable.add(Math.cos(rad));
        }
    }
    public static void run() {
        if(!initialized) {
            initTrig();
        }
        Player.input();
        atomicContent = new ArrayList();
        LevelDemo.exist();
        sortAtomsByDistance();
    }
    public static void render(Graphics g) {
        // if(true) { return; }
        if(atomicContent.size() == 0) {
            return;
        }
        System.out.println("---------------------------------");
        System.out.println("Player's Rotation: (" + Player.viewX + ", " + Player.viewY + ")");
        // Player.viewY = 90;
        for(float x = (float) (-Player.fov / 2); x < Player.fov / 2; x += Player.res) {
            for(float y = (float) (-Player.fov / 2); y < Player.fov / 2; y += Player.res) {
                relX = x * 10;
                relY = y * 10;
                // System.out.println("starting to prepare to cast a ray w/ slope (" + x + ", " + y + ", 1)");
                rotatePosToPlayerView(g, x / 10, y / 10, 0.1f);
            }
        }
    }
    public static void rotatePosToPlayerView(Graphics g, float x, float y, float z) {
        Point3D rotated = Utils.rotate3d(x, y, z, Math.round(Player.viewX), Math.round(Player.viewY), 0);
        raycast(g, rotated.x, rotated.y, rotated.z);
        /*

        */
    }
    public static void raycast(Graphics g, double dirX, double dirY, double dirZ) {
        // System.out.println("atomic content size: " + atomicContent.size());
        /*
        Sends out a ray from the player's position in the direction specified by the arguments
        */
        boolean casting = true;
        double x = Player.x;
        double y = Player.y;
        double z = Player.z;
        int numIterations = 0;
        // System.out.println("casting a ray from the player with a slope of (" + dirX + ", " + dirY + ", " + dirZ + ")");
        while(casting) {
            numIterations ++;
            // System.out.println("moving the rays along. it is at (" + x + ", " + y + ", " + z + ")");
            x += dirX;
            y += dirY;
            z += dirZ;
            int rX = (int) Math.round(x);
            int rY = (int) Math.round(y);
            int rZ = (int) Math.round(z);
            int index = getIndex(rX, rY, rZ);
            if(index != -1) {
                // found an intersection between the ray and some matter
                Atom atom = atomicContent.get(index);
                Color color = atom.color;
                // System.out.println("found something! drawing on the 2d screen at (" + (relX * (800 / (Player.fov * Player.res)) + 400) + ", " + (relY * (800 / Player.fov) + 400) + ")");
                casting = false;
                // if(new Color(255, 0, 0).equals(color)) {
                //     System.out.println("drawing red! at coords (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                // }
                // else if(new Color(0, 255, 0).equals(color)) {
                //     System.out.println("drawing green! at coords (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                // }
                // else {
                //     System.out.println("drawing blue! at coords (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                // }
                g.setColor(color);
                g.fillRect(Math.round(relX * (800 / (Player.fov / Player.res)) + 400), Math.round(relY * (800 / (Player.fov / Player.res * 2)) + 400), Math.round(Player.fov / Player.res * 2), Math.round(Player.fov / Player.res));
            }
            if(numIterations >= Player.renderDistance) {
                // System.out.println("too far... delete ray. final position: (" + rX + ", " + rY + ", " + rZ + ")");
                casting = false;
            }
        }
    }

    public static int getIndex(int x, int y, int z) {
        /*
        Uses binary search to return the index of the point with coordinates ('x', 'y', 'z') or -1 if that point does not exist.
        Assumes array is already sorted by distance to player, see method sortAtomsByDistance()
        */
        int low = 0;
        int high = atomicContent.size();
        // System.out.println("atomic content size is " + high);
        int targetDistSq = (Player.x - x) * (Player.x - x) + (Player.y - y) * (Player.y - y) + (Player.z - z) * (Player.z - z);
        // System.out.println("looking for something at (" + x + ", " + y + ", " + z + "), which is " + targetDistSq + " atoms (squared) from the player");
        boolean searching = true;
        while(searching) {
            int mid = Math.round((high + low) / 2);
            if(mid == high || mid == low) {
                return -1;
            }
            Atom atom = (Atom) atomicContent.get(mid);
            // System.out.println("binary search checking index " + mid + ", which is halfway between " + low + " and " + high);
            if(atom.distSq >= targetDistSq - 1 && atom.distSq < targetDistSq + 1) {
                if(atom.x >= x - 1 && atom.x <= x + 3 && atom.y >= y - 1 && atom.y <= y + 1 && atom.z >= z - 1 && atom.z <= z + 1) {
                    // if you've found the correct atom, return the index
                    // System.out.println("for the coordinates (" + x + ", " + y + ", " + z + "), found an atom at (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                    // System.out.println("did it intersect the correct polygon? " + atom.frontFace);
                    return mid;
                }
                else {
                    // otherwise (due to rounding / equadistant points) go to the adjacent indices and check them until you find it
                    int num = 0;
                    while(true) {
                        num ++;
                        boolean sameDist = false;
                        if(mid - num > 0) {
                            Atom atomCloser = atomicContent.get(mid - num);
                            if(atomCloser.distSq == targetDistSq) {
                                sameDist = true;
                                if(atomCloser.x == x && atomCloser.y == y && atomCloser.z == z) {
                                    // System.out.println("found an atom! coordinates (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                                    // System.out.println("did it intersect the correct polygon? " + atom.frontFace);
                                    return mid - num;
                                }
                            }
                        }
                        if(mid + num < atomicContent.size() - 1) {
                            Atom atomFarther = atomicContent.get(mid + num);
                            if(atomFarther.distSq == targetDistSq) {
                                sameDist = true;
                                if(atomFarther.x == x && atomFarther.y == y && atomFarther.z == z) {
                                    // System.out.println("found an atom! coordinates (" + atom.x + ", " + atom.y + ", " + atom.z + ")");
                                    // System.out.println("did it intersect the correct polygon? " + atom.frontFace);
                                    return mid + num;
                                }
                            }
                        }
                        if(!sameDist) {
                            return -1; // there is no atom w/ same distance and coordinates
                        }
                    }
                }
            }
            else if(atom.distSq < targetDistSq) {
                high = mid;
            }
            else if(atom.distSq > targetDistSq) {
                low = mid;
            }
            if(mid == 0 || mid == atomicContent.size()) {
                return -1;
            }
        }
        return -1;
    }
    public static void sortAtomsByDistance() {
        java.util.Collections.sort(atomicContent, new DistSorter());
    }

    public static void cube(int x, int y, int z, int w, int h, int d) {
        for(int i = x; i < x + w; i ++) {
            for(int j = y; j < y + h; j ++) {
                atomicContent.add(new Atom(i, j, z, new Color(255, 0, 0)));
                atomicContent.get(atomicContent.size() - 1).frontFace = true;
                atomicContent.add(new Atom(i, j, z + d, new Color(255, 0, 0)));
            }
        }
        for(int i = x; i < x + w; i ++) {
            for(int j = z; j < z + d; j ++) {
                atomicContent.add(new Atom(i, y, j, new Color(0, 255, 0)));
                atomicContent.add(new Atom(i, y + h, j, new Color(255, 0, 0)));
            }
        }
        for(int i = y; i < y + h; i ++) {
            for(int j = z; j < z + d; j ++) {
                atomicContent.add(new Atom(x, i, j, new Color(255, 0, 0)));
                atomicContent.add(new Atom(x + w, i, j, new Color(255, 0, 0)));
            }
        }
    }
}
class DistSorter implements Comparator<Atom> {
    // used for sorting atoms by distance so we can use binary search to increase performance
    public int compare(Atom a, Atom b) {
        int distSqA = (a.x - Player.x) * (a.x - Player.x) + (a.y - Player.y) * (a.y - Player.y) + (a.z - Player.z) * (a.z - Player.z);
        a.distSq = distSqA;
        int distSqB = (b.x - Player.x) * (b.x - Player.x) + (b.y - Player.y) * (b.y - Player.y) + (b.z - Player.z) * (b.z - Player.z);
        b.distSq = distSqB;
        return (distSqA == distSqB) ? 0 : ((distSqA > distSqB) ? -1 : 1);
    }
}
