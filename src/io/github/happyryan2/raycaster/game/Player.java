package io.github.happyryan2.raycaster.game;

import java.awt.Point;

import io.github.happyryan2.raycaster.utilities.*;
import io.github.happyryan2.raycaster.raycaster.Point2d;

public class Player {
	public static int x = 0;
	public static int y = 0;
	public static int z = -300;
	public static float viewX = 0;
	public static float viewY = 0;
	public static float fov = 2f;
	public static float screenSize = 400;
	public static float res = fov / screenSize;
	public static float renderDistance = 500;
	public static boolean initialized = false;
	public static void input() {
		/* Get mouse inputs + adjust vision */
		if(Screen.frame.isFocused()) {
			Point screenPos = Screen.frame.getLocationOnScreen();
			if(!initialized) {
				Screen.robot.mouseMove(400, 400);
				initialized = true;
			}
			else {
				viewX += (MousePos.x - 397 - screenPos.x) / 2;
				viewY -= (MousePos.y - 374 - screenPos.y) / 2;
				if(viewY > 90) {
					viewY = 90;
				}
				else if(viewY < -90) {
					viewY = -90;
				}
				Screen.robot.mouseMove(400, 400);
			}
		}
		/* Get key inputs + move */
		if(KeyInputs.keyW) {
			Point2d dir = Utils.rotate2d(0, 5, -viewX);
			x += dir.x;
			z += dir.y; // < not a typo
		}
		else if(KeyInputs.keyS) {
			Point2d dir = Utils.rotate2d(0, 5, -viewX);
			x -= dir.x;
			z -= dir.y; // < not a typo
		}
		else if(KeyInputs.keyA) {
			Point2d dir = Utils.rotate2d(5, 0, -viewX);
			x -= dir.x;
			z -= dir.y; // < not a typo
		}
		else if(KeyInputs.keyD) {
			Point2d dir = Utils.rotate2d(-5, 0, -viewX);
			x -= dir.x;
			z -= dir.y; // < not a typo
		}
	}
}
