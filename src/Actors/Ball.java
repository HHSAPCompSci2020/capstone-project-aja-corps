package Actors;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PApplet;

public class Ball extends MovingImage{

	private double xVelocity, yVelocity;
	private boolean dribbling = false;
	private Rectangle2D.Double playerDribbling;
	
	public Ball(int x, int y, int width, int height) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 5;
	}
	public void act(ArrayList<Rectangle2D.Double> players, double floorY) {
		for(Rectangle2D.Double player : players) {
			double px = player.getX();
			double py = player.getY();
			double width = player.getWidth();
			double height = player.getHeight();
			
			Rectangle2D.Double ballBounds = new Rectangle2D.Double(getX(), getY(), getX()+getWidth(), getY()+getHeight());
			
			if(ballBounds.intersects(player)) {
				dribbling = true;
				playerDribbling = player;
			}
		}
	}
	
	private void dribble() {
		
	}
}
