package Actors;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PApplet;

public class Ball extends MovingImage {

	private double xVelocity, yVelocity, shotx, shoty;
	private double rateOfDecrease;
	private final double friction = 0.85;
	private boolean dribbling = true;
	private Player playerDribbling;
	private boolean shooting;
	private boolean bounce = false;
	private double bounceHeight = 140;

	private String uniqueID;
	private boolean dataUpdated;
	private boolean hasBall;
	
	private double[] equation;

	private String username;

	public Ball(int x, int y, int width, int height, String username, String uniqueID) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 4;
		this.uniqueID = uniqueID;
		this.username = username;
	}

	public void act(double floorY) {
		if(dribbling)
			dribble(floorY);
		else if(shooting) {
			if(playerDribbling.getDirection())
				shoot(640, 140);
			else
				shoot(130, 140);
		} else if(bounce) {
			bounce();
		}
			
	}

	public void bounce() {
		x += xVelocity;
		y += yVelocity;
		
		if(y >= 300) {
			y = 290;
			yVelocity = -yVelocity;
		} else if(y <= bounceHeight) {
			yVelocity = -yVelocity;
			y = bounceHeight+10;
		}
		if(bounceHeight <= 280) {
			bounceHeight += 0.5;
		}
		if(bounceHeight >= 280) {
			yVelocity = 0;
		}
	}
	public void dribble(double floorY) {
		//System.out.println("dribbling...");
		if (playerDribbling.intersects(this)) {
			hasBall = true;
			playerDribbling.setHasBall(true);
		}

		if (hasBall) {
			floorY = 300; // hardcoded for now
			if (y >= floorY) {
				yVelocity = -yVelocity;
			} else if (y <= playerDribbling.getY() + 15) {
				yVelocity = Math.abs(yVelocity);
			}
			y += yVelocity;
			if (playerDribbling.getDirection()) {
				x = playerDribbling.getX() + 25;
			} else {
				x = playerDribbling.getX() - 10;
			}
		}
		dataUpdated = true;
	}

	public void shoot(double hoopx, double hoopy) {
		if (dribbling) {
			shotx = playerDribbling.getX() + 25;
			//shoty = playerDribbling.getY() - 15;
			shoty = hoopy;
			x = shotx;
			y = shoty;
			dribbling = false;
			if(playerDribbling.getDirection())
				xVelocity = 5;
			else
				xVelocity = -5;
			calculateParabola(hoopx, hoopy);
			// calculateRateOfDecrease();
			//playerDribbling = null;
			shooting = true;
		} else
//			xVelocity = xVelocity * 0.85;

		if (y >= 300 && !shooting) {
			xVelocity = 0;
			dribbling = true;
			y = 200;
		} else if(shooting == true){
			x += xVelocity;
			y = f(x);
			System.out.println(x + ", " + y);
			// y += yVelocity;
			// yVelocity -= rateOfDecrease;
		}
		if(y >= shoty) {
			if(playerDribbling.getDirection() && x >= hoopx) {
				bounce = true;
				shooting = false;
				xVelocity = 0;
				yVelocity = 3;
			}
			else if(!playerDribbling.getDirection() && x <= hoopx) {
				bounce = true;
				shooting = false;
				xVelocity = 0;
				yVelocity = 5;
			}
		}
		
		dataUpdated = true;
	}

	// this method will calculate the y coordinate based off of a function
	// (parabola) representing the arc of the shot
	private double f(double x) {
		double a = equation[0];
		double h = equation[1];
		double k = equation[2];
		return (a*Math.pow(x-h, 2) + k);
	}

	private void calculateParabola(double hoopx, double hoopy) {
		equation = new double[3];
		if(playerDribbling.getDirection()) {
			if(shotx < 275)
				shotx += 75;
			else if(shotx >= 275 && shotx < 382)
				shotx += 50;
			else if(shotx >= 382 && shotx < 487)
				shotx += 25;
			else if(shotx >= 487)
				shotx += 10;
		} else {
			if(shotx < 275)
				shotx -= 75;
			else if(shotx >= 275 && shotx < 382)
				shotx -= 50;
			else if(shotx >= 382 && shotx < 487)
				shotx -= 25;
			else if(shotx >= 487)
				shotx -= 10;
		}
		
		double h = (hoopx+shotx)/2;
		double a = (shoty-50)/Math.pow(shotx-h, 2);
		double k = 50;
		equation[0] = a;
		equation[1] = h;
		equation[2] = k;
	}
	/*
	 * private void calculateRateOfDecrease() {
	 * 
	 * } private double y() {
	 * 
	 * }
	 */

	public void getPlayer(Player p) {
		playerDribbling = p;
	}

	public BallData getDataObject() {
		dataUpdated = false;
		BallData p = new BallData();
		p.username = username;
		p.x = x;
		p.y = y;
		return p;
	}

	public void syncWithDataObject(BallData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.username = data.username;
	}

	public boolean idMatch(String uid) {
		return this.uniqueID.equals(uid);
	}
	
	public String getID() {
		return this.uniqueID;
	}

	public boolean isDataChanged() {
		return dataUpdated;
	}

	public String getUsername() {
		return username;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}