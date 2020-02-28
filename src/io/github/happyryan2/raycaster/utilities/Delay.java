/*
This utility does all the work necessary for importing the Java timer and setting the fps.
*/

package io.github.happyryan2.raycaster.utilities;

import java.util.TimerTask;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Point;

import io.github.happyryan2.raycaster.raycaster.*;
import io.github.happyryan2.raycaster.utilities.*;

public class Delay extends TimerTask {
	public static boolean calculating = false;
	public static long frameTime = 0;

	public void run() {
		if(calculating) {
			System.out.println("a frame took too long");
			return;
		}
		System.out.println("this frame took " + (System.currentTimeMillis() - frameTime) + " milliseconds");
		frameTime = System.currentTimeMillis();
		calculating = true;
		Screen.cursor = "default";
		Screen.frameCount ++;

		/* update screen size */
		Screen.screenW = Screen.canvas.getWidth();
		Screen.screenH = Screen.canvas.getHeight();

		/* get inputs for this frame */
		MousePos.update();

		/* run the game */
		RayCaster.run();

		/* repaint to show changes */
		Screen.canvas.repaint();
		/* update mouse type */
		if(Screen.cursor == "hand") {
			Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else {
			if(Screen.frame.isFocused())  {
				BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

				/* create a new blank cursor */
				Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
				Screen.canvas.setCursor(blankCursor);
			}
			else {
				Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		calculating = false;
	}

}
