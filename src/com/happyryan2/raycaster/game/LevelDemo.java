package com.happyryan2.raycaster.game;

import com.happyryan2.raycaster.raycaster.RayCaster;

public class LevelDemo {
	public static void exist() {
		RayCaster.polyhedronColor = new java.awt.Color(255, 0, 0);
		RayCaster.cube(-50, -50, 50, 100, 100, 100);
		RayCaster.polyhedronColor = new java.awt.Color(0, 255, 0);
		RayCaster.cube(-50, -50, -150, 100, 100, 100); // -150 to -50
		RayCaster.polyhedronColor = new java.awt.Color(0, 0, 255);
		RayCaster.cube(-1000, 50, -1000, 2000, 100, 2000);
	}
}
