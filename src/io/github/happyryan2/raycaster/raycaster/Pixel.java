package io.github.happyryan2.raycaster.raycaster;

import java.awt.Color;

import io.github.happyryan2.raycaster.game.Player;

public class Pixel {
	public Point2d location;
	public Color color;

	public Pixel(Point2d location, Color color) {
		this.location = location;
		this.color = color;
	}
	public void display(java.awt.Graphics g) {
		g.setColor(this.color);
		g.fillRect((int) this.location.x, (int) this.location.y, (int) (800 / Player.screenSize), (int) (800 / Player.screenSize));
	}
}
