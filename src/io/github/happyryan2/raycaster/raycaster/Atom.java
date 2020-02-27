package io.github.happyryan2.raycaster.raycaster;

import java.awt.Color;

public class Atom {
	public int x;
	public int y;
	public int z;
	public int distSq;
	public Color color;
	public boolean frontFace; // just for a specific test, don't forget to delete later
	public Atom(int x, int y, int z, Color color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
	}
}
