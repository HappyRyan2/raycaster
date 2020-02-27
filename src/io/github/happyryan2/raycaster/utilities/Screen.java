/*
This file creates the screen that the game is played on, and is also the application entry point.
*/

package io.github.happyryan2.raycaster.utilities;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.lang.Math;
import java.io.File;
import java.net.URL;

import io.github.happyryan2.raycaster.raycaster.RayCaster;

public class Screen extends JPanel {
	public static JFrame frame = new JFrame("RayCaster");
	public static JPanel canvas = new Screen();
	public static int screenW = 0;
	public static int screenH = 0;
	public static String cursor = "default";
	public static Font fontRighteous;
	public static Robot robot;
	public static int frameCount = 0;
	public static long startTime;
	public static int fps = 0;

	public static void main(String[] args) {
		/* create JFrame + canvas for graphics */
		canvas.setDoubleBuffered(true);
		canvas.setSize(400, 400);
		frame.add(canvas);
		frame.pack();
		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setVisible(true);
		canvas.setFocusable(true);

		/* Record when the program started */
		startTime = System.nanoTime();

		/* schedule framerate interval function */
		Delay delay = new Delay();
		Timer timer = new Timer(true); // true = asynchronous
		timer.scheduleAtFixedRate(delay, 0, 1000 / 15);

		/* Listen for user inputs */
		canvas.addKeyListener(new KeyInputs());
		canvas.addMouseListener(new MouseClick());

		/* Load resources */
		try {
			robot = new Robot();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		// io.github.happyryan2.raycaster.game.Player.viewX = 45;
		// io.github.happyryan2.raycaster.game.Player.viewY = 90; // 90 = up, 0 = forward, -90 = down
		// RayCaster.rotatePosToPlayerView(g, -5, 0, 0);
		/* clear background */
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, 800, 800);
		/* Draw game graphics */
		RayCaster.render(g);
		/* Display framerate */
		fps = FPS.calcFPS();
		g.setColor(new Color(0, 0, 0));
		centerText(g, 50, 40, "FPS: " + fps);
	}

	public static void centerText(Graphics g, float x, float y, String text) {
		int width = g.getFontMetrics().stringWidth(text);
		g.drawString(text, (int) x - (width / 2), (int) y);
	}
}
