import java.util.Random;
import java.util.Scanner;


import processing.core.PApplet;

public class DrawingSurface extends PApplet {

	// FIELDS
	
	private House h;
	private Person p;
	private float increasefactor;
	private float decreasefactor;
	private boolean trueting = true;
	private int backcolor;
	
	
	
	// CONSTRUCTOR - Initialize any added fields here.
	public DrawingSurface() {
		h = new House();
		p = new Person();
		backcolor = 180;
		increasefactor = (float)1.1;
		decreasefactor = (float)0.9;
		
	}
	
	


	// METHODS
	// Add processing methods here. See the documentation at processing.org for reference.
	public void setup() {

	}

	public void draw() {
		h.draw(this);
	}
	
	
	
	

	public void mousePressed() {
		h.move(mouseX, mouseY);
	}
	
	public void keyPressed() {
		Random rand = new Random();
		
		if(keyCode == RIGHT) {
			p.move(p.getx()+50, p.gety());
			backcolor = rand.nextInt(255);
		}else if (keyCode == LEFT) {
			p.move(p.getx()-50,p.gety() );
			backcolor = rand.nextInt(255);
			
		}
		
		if(keyCode == DOWN) {
			p.move(p.getx(), p.gety()+50);
			backcolor = rand.nextInt(255);
			
		}else if(keyCode == UP) {
			p.move(p.getx(), p.gety()-50);
			backcolor = rand.nextInt(255);
			
		}
		
		if(keyCode == 66) {				// keycode for letter b
			h.resize(increasefactor);
		}else if(keyCode == 83) {			// keycode for the letter s
			h.resize(decreasefactor);
		}
	}
	
	public static void main(String args[]) {
		

		
		
		
		
	}
}

	// Add methods fors user interaction. Check the processing reference for info on this (http://processing.org).
	// We'll also review how to get mouse/keys working in class.

	

