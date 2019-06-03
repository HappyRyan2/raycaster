package com.happyryan2.raycaster.game;

import java.awt.Point;

import com.happyryan2.raycaster.utilities.KeyInputs;
import com.happyryan2.raycaster.utilities.Utils;

public class Player {
    public static int x = 0;
    public static int y = 0;
    public static int z = 0;
    public static float viewX = 0;
    public static float viewY = 0;
    public static float fov = 3;
    public static float res = 0.05f;
    public static float renderDistance = 500;
    public static void input() {
        if(KeyInputs.keyA) {
            viewX --;
            if(viewX < 0) {
                viewX += 360;
            }
        }
        else if(KeyInputs.keyD) {
            viewX ++;
            if(viewX > 360) {
                viewX -= 360;
            }
        }
        if(KeyInputs.keyW) {
            Point dir = Utils.rotate2d(0, 1, viewX);
            x += dir.x;
            z += dir.y; // < not a typo
            // System.out.println("moving the player with a velocity of (" + dir.x + ", " + dir.y + ")");
        }
        else if(KeyInputs.keyS) {
            System.out.println("moving the player backwards");
            Point dir = Utils.rotate2d(0, 1, viewX);
            x -= dir.x;
            z -= dir.y; // < not a typo
            // System.out.println("moving the player with a velocity of (" + dir.x + ", " + dir.y + ")");
        }
    }
}
