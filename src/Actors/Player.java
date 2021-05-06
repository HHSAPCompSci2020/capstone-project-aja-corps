package Actors;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import processing.core.PShape;

public class Player extends MovingImage {

	public static final int MARIO_WIDTH = 40;
	public static final int MARIO_HEIGHT = 60;

	private double xVelocity, yVelocity;
	private boolean onASurface;
	private double friction;
	private double gravity;
	private double jumpStrength;
	private boolean speedPowerup = false;
	private boolean jumpPowerup = false;
	private boolean shotPowerup = false;
	
//	public String host;
	
	private String uniqueID;
	
	private String username;
	private double x, y;
	
	private PShape shape;
	
	private boolean dataUpdated;

	public Player(int x, int y, String username, String uniqueID) {
		super("player.png", x, y, MARIO_WIDTH, MARIO_HEIGHT);
		xVelocity = 0;
		yVelocity = 0;
		onASurface = false;
		gravity = 0.7;
		friction = .85;
		jumpStrength = 10;
		this.username = username;
		this.uniqueID = uniqueID;
		dataUpdated = false;
	}

	
	/**
	 * method toggles the speed power up
	 * @post changes speedPowerup field
	 */
	
	public void speedPowerup() {
		speedPowerup = true;
	}
	/**
	 * toggles the jump power up
	 * @post changed jumpPower up field
	 */
	public void jumpPowerup() {
		jumpPowerup = true;
	}
	/**
	 * toggles the shot power up
	 * @post changes shoot power up field 
	 */
	public void shotPowerup() {
		shotPowerup = true;
}
	
	
	
	
	
	// METHODS
	public void walk(int dir) {
		if (xVelocity <= 10 && xVelocity >= -10)
			
			if(speedPowerup) {
				xVelocity += dir;
				speedPowerup = false;
				return;
			}
			
			
			xVelocity += (0.5)*(double)dir;
	}

	public void jump() {
		if (onASurface) {
			
			if(jumpPowerup) {
			yVelocity -= 2*jumpStrength;
			jumpPowerup = false;
			return;
			}
	
			yVelocity -= jumpStrength;
		
		}
	}

	public void act(ArrayList<Shape> obstacles) {
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();

		// ***********Y AXIS***********

		yVelocity += gravity; // GRAVITY
		double yCoord2 = yCoord + yVelocity;

		Rectangle2D.Double strechY = new Rectangle2D.Double(xCoord,Math.min(yCoord,yCoord2),width,height+Math.abs(yVelocity));

		onASurface = false;

		if (yVelocity > 0) {
			Shape standingSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechY)) {
					onASurface = true;
					standingSurface = s;
					yVelocity = 0;
				}
			}
			if (standingSurface != null) {
				Rectangle r = standingSurface.getBounds();
				yCoord2 = r.getY()-height;
			}
		} else if (yVelocity < 0) {
			Shape headSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechY)) {
					headSurface = s;
					yVelocity = 0;
				}
			}
			if (headSurface != null) {
				Rectangle r = headSurface.getBounds();
				yCoord2 = r.getY()+r.getHeight();
			}
		}

		if (Math.abs(yVelocity) < .2)
			yVelocity = 0;

		// ***********X AXIS***********


		xVelocity *= friction;

		double xCoord2 = xCoord + xVelocity;

		Rectangle2D.Double strechX = new Rectangle2D.Double(Math.min(xCoord,xCoord2),yCoord2,width+Math.abs(xVelocity),height);

		if (xVelocity > 0) {
			Shape rightSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechX)) {
					rightSurface = s;
					xVelocity = 0;
				}
			}
			if (rightSurface != null) {
				Rectangle r = rightSurface.getBounds();
				xCoord2 = r.getX()-width;
			}
		} else if (xVelocity < 0) {
			Shape leftSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechX)) {
					leftSurface = s;
					xVelocity = 0;
				}
			}
			if (leftSurface != null) {
				Rectangle r = leftSurface.getBounds();
				xCoord2 = r.getX()+r.getWidth();
			}
		}


		if (Math.abs(xVelocity) < .2)
			xVelocity = 0;
		
		this.x = xCoord2;
		this.y = yCoord2;
		
		dataUpdated = true;


		moveToLocation(xCoord2,yCoord2);

	}
	
	public PlayerData getDataObject() {
		dataUpdated = false;
		PlayerData p = new PlayerData();
		p.username = username;
		p.x = x;
		p.y = y;
		return p;
	}
	
	public void syncWithDataObject(PlayerData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.username = data.username;
	}
	
	public boolean idMatch(String uid) {
		return this.uniqueID.equals(uid);
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
