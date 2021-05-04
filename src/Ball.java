import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Ball extends MovingImage {
	
	private double xVelocity, yVelocity;
	private double friction;
	private double gravity;
	private boolean bounced;
	private boolean pickedUp;
	
	public Ball(int x, int y) {
		super("basketball.png", x, y, 20, 20);
		xVelocity = 0;
		yVelocity = 0;
		gravity = 0.7;
		friction = .85;
	}
	
	public void act(ArrayList<Shape> obstacles, double playerX, double playerY, Shape ground) {
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();
		
		if(pickedUp == false) {
			double yCoord2 = yCoord + yVelocity;
			double xCoord2 = xCoord + xVelocity;
			
			Rectangle2D.Double stretchY = new Rectangle2D.Double(xCoord, Math.min(yCoord, yCoord2), width,
					height + Math.abs(yVelocity));
			for (Shape s : obstacles)
				if (s.intersects(stretchY))
					yVelocity = -yVelocity;

			Rectangle2D.Double stretchX = new Rectangle2D.Double(Math.min(xCoord, xCoord2), yCoord2,
					width + Math.abs(xVelocity), height);
			for (Shape s : obstacles)
				if (s.intersects(stretchX))
					xVelocity = -xVelocity;
			moveToLocation(xCoord2, yCoord2);
		} else if(pickedUp){
			bounce(playerX, playerY, ground);
		}
	}
	
	private void bounce(double pX, double pY, Shape ground) {
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();
		double yCoord2 = yCoord + yVelocity;
		
		if(yCoord == pY)
			yVelocity = -yVelocity;
		
		Rectangle2D.Double stretchY = new Rectangle2D.Double(xCoord, Math.min(yCoord, yCoord2), width,
				height + Math.abs(yVelocity));
		if (ground.intersects(stretchY))
			yVelocity = -yVelocity;
		double xCoord2 = pX;
		moveToLocation(xCoord2, yCoord2);
	}
	public void shoot(double pX, double pY) {
		
	}
}
