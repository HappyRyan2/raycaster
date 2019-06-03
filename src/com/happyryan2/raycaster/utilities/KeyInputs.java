/*
This utility interprets the user's key presses.
*/

package com.happyryan2.raycaster.utilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInputs extends KeyAdapter {
	public static String[] keys;
	public static boolean keyW = false;
	public static boolean keyA = false;
	public static boolean keyS = false;
	public static boolean keyD = false;
	@Override
	public void keyPressed(KeyEvent event) {
		char pressed = event.getKeyChar();
		if(pressed == 'w') {
			keyW = true;
		}
		if(pressed == 'a') {
			keyA = true;
		}
		if(pressed == 's') {
			keyS = true;
		}
		if(pressed == 'd') {
			keyD = true;
		}
	}
	public void keyReleased(KeyEvent event) {
		char released = event.getKeyChar();
		if(released == 'w') {
			keyW = false;
		}
		else if(released == 'a') {
			keyA = false;
		}
		else if(released == 's') {
			keyS = false;
		}
		else if(released == 'd') {
			keyD = false;
		}
	}
}
