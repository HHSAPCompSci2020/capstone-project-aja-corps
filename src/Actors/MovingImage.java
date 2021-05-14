package Actors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

import javax.swing.*;

/**
 * 
 * @author anirudhv
 *
 */
 
public class MovingImage extends Rectangle2D.Double {
	
	// FIELDS
	private Image image;
	
	/**
	 * Constructs a Moving Image
	 * 
	 * @param filename Filename of image to draw
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param w Width
	 * @param h Height
	 */
	public MovingImage(String filename, int x, int y, int w, int h) {
		this((new ImageIcon(filename)).getImage(),x,y,w,h);
	}
	
	
	/**
	 * Constructs a Moving Image
	 * 
	 * @param img Image to draw
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param w Width
	 * @param h Height
	 */
	public MovingImage(Image img, int x, int y, int w, int h) {
		super(x,y,w,h);
		image = img;
	}
	
	
	/**
	 * Moves to location x,y
	 * 	
	 * @param x x coordinate of location
	 * @param y y coordinate of location
	 */
	public void moveToLocation(double x, double y) {
		super.x = x;
		super.y = y;
	}
	
	/**
	 * Moves by a certain amount
	 * 
	 * @param x x amount to move by
	 * @param y y amount to move by
	 */
	public void moveByAmount(double x, double y) {
		super.x += x;
		super.y += y;
	}
	
	/**
	 * Applies bounds to the image based on the window
	 * 
	 * @param windowWidth width of the window
	 * @param windowHeight height of the window
	 */
	public void applyWindowLimits(int windowWidth, int windowHeight) {
		x = Math.min(x,windowWidth-width);
		y = Math.min(y,windowHeight-height);
		x = Math.max(0,x);
		y = Math.max(0,y);
	}
	
	/**
	 * Draws the moving image
	 * 
	 * @param g Graphics needed to draw it
	 * @param io Image observer needed to draw the image
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(image,(int)x,(int)y,(int)width,(int)height,io);
	}
	
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	
	
}










