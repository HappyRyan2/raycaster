/*
This utility gets the mouse position on the screen.
*/

package io.github.happyryan2.raycaster.utilities;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;

public class MousePos {
	public static int x = 0;
	public static int y = 0;

	public static void update() {
		PointerInfo mouse = MouseInfo.getPointerInfo();
		Point pos = mouse.getLocation();
		Point offset = Screen.canvas.getLocationOnScreen();
		x = pos.x - offset.x;
		y = pos.y - offset.y;
	}
}
