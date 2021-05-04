import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.JFrame;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

public class Drawings {
	
	 
	

	// MAIN - Typically in a class by itself, but included here for simplicity.
	
	 
	 public static void main(String args[]) {
		DrawingSurface drawing = new DrawingSurface();
		PApplet.runSketch(new String[]{""}, drawing);
		PSurfaceAWT surf = (PSurfaceAWT) drawing.getSurface();
		PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
		JFrame window = (JFrame)canvas.getFrame();
		window.setSize(1000, 1000);
		window.setMinimumSize(new Dimension(100,100));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setVisible(true);
		canvas.requestFocus();
		
		
	}


}