package Actors;
import java.awt.Image;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Ball extends MovingImage {
	
	private double xVelocity, yVelocity;
	private double shotX, shotY;
	private double[] equation;
	private double friction;
	private double gravity;
	private boolean bounced;
	private boolean pickedUp = false;
	private boolean shot = false;
	
	public Ball(int x, int y) {
		super("basketball.png", x, y, 20, 20);
		xVelocity = 0;
		yVelocity = 0;
		gravity = 0.7;
		friction = .85;
	}
	
	public void act(ArrayList<Shape> obstacles, double playerX, double playerY, Shape ground) {
		//obstacles ArrayList contains shape representing backboard, wall, and players
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
				if(s.intersects(stretchY) && s instanceof Player) {
					shot = false;
					pickedUp = true;
					xCoord2 = playerX;
					yCoord2 = playerY;
				} else if (s.intersects(stretchY)) {
					shot = false;
					yVelocity = -yVelocity;
				}

			Rectangle2D.Double stretchX = new Rectangle2D.Double(Math.min(xCoord, xCoord2), yCoord2,
					width + Math.abs(xVelocity), height);
			if(!pickedUp) {
				for (Shape s : obstacles)
					if(s.intersects(stretchX) && s instanceof Player) {
						shot = false;
						pickedUp = true;
						xCoord2 = playerX;
						yCoord2 = playerY;
					} else if (s.intersects(stretchX)) {
						shot = false;
						xVelocity = -xVelocity;
					}
			}
			if(shot && pickedUp)
				shootingMotion(shotX, shotY);
			moveToLocation(xCoord2, yCoord2);
		} else if(pickedUp){
			bounce(playerX, playerY, ground);
		}
	}
	
	public void pickUp() {
		pickedUp = true;
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
//	public void shoot(double pX, double pY) {
//		shot = true;
//		shotX = pX;
//		shotY = pY;
//		equation = findEquation(pX, pY);
//	}
	private void shootingMotion(double beginningX, double beginningY) {
		/*
		 * in order to find what the velocity should equal, we need to find the
		 * derivative of the parabola or the path of the shot. This way, we can find
		 * what the xvelocity and yvelocity are supposed equal
		 */
		
		double x = getX();
		double y = getY();
		
		
	}
	
//	private double findSlopeAtPoint(double x) {
//		
//	}
//	private double findRise(double slope) {
//		
//	}
//	private double findRun(double slope) {
//		
//	}
//	private double[] findEquation(double x, double y) {
//		//first element is the 'a' value, second is 'b', and third is 'c' of parabolic equation
//		double[] result = new double[3];
//		
//		
//	}
}
