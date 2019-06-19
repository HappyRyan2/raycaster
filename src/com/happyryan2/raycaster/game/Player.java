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
	public static float fov = 4;
	public static float screenSize = 400;
	public static float res = fov / screenSize;
	/*
	res * ite = fov
	(fov / 800) * ite = fov
	(fov / 800) * 800 = fov
	fov = fov
	*/
	public static float renderDistance = 500;
	public static void input() {
		if(KeyInputs.keyA) {
			viewX -= 5;
			if(viewX < 0) {
				viewX += 360;
			}
		}
		else if(KeyInputs.keyD) {
			viewX += 5;
			if(viewX > 360) {
				viewX -= 360;
			}
		}
		if(KeyInputs.keyW) {
			Point dir = Utils.rotate2d(0, 5, -viewX);
			x += dir.x;
			z += dir.y; // < not a typo
		}
		else if(KeyInputs.keyS) {
			Point dir = Utils.rotate2d(0, 5, -viewX);
			x -= dir.x;
			z -= dir.y; // < not a typo
		}
	}
}
