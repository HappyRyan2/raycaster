/*
This utility does all the work necessary for importing the Java timer and setting the fps.
*/

package com.happyryan2.raycaster.utilities;

import java.util.TimerTask;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Point;

import com.happyryan2.raycaster.raycaster.*;
import com.happyryan2.raycaster.utilities.*;
// at the beginning it's (-5, -1, -5)
public class Delay extends TimerTask {
	public void run() {
		Screen.cursor = "default";
		Screen.frameCount ++;

		//update screen size
		Screen.screenW = Screen.canvas.getWidth();
		Screen.screenH = Screen.canvas.getHeight();

		//get inputs for this frame
		MousePos.update();

		//run the game
		RayCaster.run();

		//repaint to show changes
		Screen.canvas.repaint();
		//update mouse type
		if(Screen.cursor == "hand") {
			Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if(false) {
			if(Screen.frame.isFocused())  {
				BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

				// Create a new blank cursor.
				Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
				Screen.canvas.setCursor(blankCursor);
			}
			else {
				Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

}
