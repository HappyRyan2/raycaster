/*
This class provides functions for calculating the frame rate.
*/

package io.github.happyryan2.raycaster.utilities;

import java.lang.Math;

public class FPS {
	public static int prevFrames = 0;
	public static int frames = 0;
	public static long prevMillis = 0;
	public static long millis = 0;
	public static int calcFPS() {
		/*
		Calculates the number of frames / the number of seconds, averaged for the last 5 seconds.
		*/
		millis = System.nanoTime() - Screen.startTime;
		long millisSinceLastCall = millis - prevMillis;
		frames = Screen.frameCount;
		int framesSinceLastCall = frames - prevFrames;

		prevFrames = frames;
		prevMillis = millis;

		return Math.round(framesSinceLastCall / millisSinceLastCall * 1000);
	}
}
