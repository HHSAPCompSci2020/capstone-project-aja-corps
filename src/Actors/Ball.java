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

	private String uniqueID;
	private boolean dataUpdated;
	private boolean hasBall;

	private String username;

	public Ball(int x, int y, int width, int height, String username, String uniqueID) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 4;
		this.uniqueID = uniqueID;
		this.username = username;
	}

	public void act(ArrayList<Rectangle2D.Double> players, double floorY) {
		/*
		 * for(Rectangle2D.Double player : players) { double px = player.getX(); double
		 * py = player.getY(); double width = player.getWidth(); double height =
		 * player.getHeight();
		 * 
		 * Rectangle2D.Double ballBounds = new Rectangle2D.Double(getX(), getY(),
		 * getWidth(), getHeight());
		 * 
		 * if(player != playerDribbling && ballBounds.intersects(player)) { dribbling =
		 * true; playerDribbling = player; }
		 * 
		 * if(dribbling && playerDribbling != null) dribble(floorY);
		 * 
		 * }
		 */
	}

	public void dribble(double floorY) {
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
			shoty = playerDribbling.getY() - 15;
			dribbling = false;
			xVelocity = 5;
//			calculateParabola();
			// calculateRateOfDecrease();
			playerDribbling = null;
		} else
			xVelocity = xVelocity * 0.85;

		if (x == hoopx && shooting) {
			shooting = false;
			xVelocity = 0;
		} else {
			x += xVelocity;
//			y = f(x);
			// y += yVelocity;
			// yVelocity -= rateOfDecrease;
		}
	}

	// this method will calculate the y coordinate based off of a function
	// (parabola) representing the arc of the shot
	/*
	 * private double f(double x) {
	 * 
	 * }
	 * 
	 * private void calculateParabola() { double k = 20; double }
	 */
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
