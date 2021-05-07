package Actors;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PApplet;

public class Ball extends MovingImage{

	private double xVelocity, yVelocity;
	private boolean dribbling = true;
	private Rectangle2D.Double playerDribbling;
	
	public Ball(int x, int y, int width, int height) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 3;
	}
	public void act(ArrayList<Rectangle2D.Double> players, double floorY) {
		/*for(Rectangle2D.Double player : players) {
			double px = player.getX();
			double py = player.getY();
			double width = player.getWidth();
			double height = player.getHeight();
			
			Rectangle2D.Double ballBounds = new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
			
			if(player != playerDribbling && ballBounds.intersects(player)) {
				dribbling = true;
				playerDribbling = player;
			}
			
			if(dribbling && playerDribbling != null)
				dribble(floorY);
			
		}*/
	}
	
	public void dribble(double floorY) {
		floorY = 300; //hardcoded for now
		if(y >= floorY) {
			yVelocity = -yVelocity;
		} else if(y <= playerDribbling.getY()+15) {
			yVelocity = Math.abs(yVelocity);
		}
		y += yVelocity;
		x = playerDribbling.getX()+25;
	}
	public void getPlayer(Player p) {
		playerDribbling = p;
	}
}
