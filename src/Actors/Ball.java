package Actors;

import Graphics.Scoreboard;

public class Ball extends MovingImage {

	private double xVelocity, yVelocity, shotx, shoty;
	private boolean dribbling = true;
	private Player playerDribbling;
	private boolean shooting;
	private boolean bounce = false;

	private String uniqueID;
	private boolean dataUpdated;
	private boolean onGround = true;

	private double CONSTANT = 0.3;
	private int bounceCount = 0;

	private double[] equation;

	private String username;

	public Ball(int x, int y, int width, int height, String username, String uniqueID) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 4;
		this.uniqueID = uniqueID;
		this.username = username;
	}

	public void act(Player p, double floorY) {
		if (this.intersects(p) && onGround) {
			p.setHasBall(true);
			this.setPlayer(p);
			this.setDribbling(true);
			onGround = false;
			this.dataUpdated = true;
		}

		if (playerDribbling != null) {
			if (dribbling)
				dribble(floorY);
			else if (shooting) {
				if (playerDribbling.getDirection())
					shoot(640, 140);
				else
					shoot(130, 140);
			}
		}

		if (bounce) {
//			System.out.println("bouncing");
			bounce(p);
		}
		
		if(y > floorY)
			moveToGround();
		
		if(x > 800)
			//setDribbling(true);
			
		this.dataUpdated = true;
	}

	public void setDribbling(boolean x) {
		bounceCount = 0;
		xVelocity = 0;
		yVelocity = 4;
		this.dribbling = x;
		this.bounce = false;
	}

	public boolean hasPlayer() {
		if (playerDribbling == null) {
			return false;
		} else {
			return true;
		}
	}

	public void bounce(Player p) {
		if (bounceCount >= 5) {
			yVelocity = 0;
			moveToGround();
			p.setHasBall(false);
			playerDribbling = null;
			dataUpdated = true;
			onGround = true;
			dribbling = false;
			playerDribbling = null;
			return;
		}
		x += xVelocity;
		y += yVelocity;

		if (y >= 280) {
			bounceCount++;
			y = 270;
			yVelocity = -(yVelocity * 0.7);
			// System.out.println(yVelocity);
		} else {
			yVelocity += CONSTANT;
		}
	}

	public void moveToGround() {
		this.y = 280;
	}

	public void dribble(double floorY) {

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

	public void shoot(double hoopx, double hoopy) {
		if (dribbling) {
			shotx = playerDribbling.getX() + 25;
			shoty = hoopy;
			x = shotx;
			y = shoty;
			dribbling = false;
			if (playerDribbling.getDirection())
				xVelocity = 5;
			else
				xVelocity = -5;
			calculateParabola(hoopx, hoopy);
			shooting = true;
		}
		if (shooting == true) {
			x += xVelocity;
			y = f(x);
		}

		if (playerDribbling.getDirection() && x >= hoopx) {
			if (makeShot()) {
				Scoreboard.score2++;
				xVelocity = 0;
			} else {
				x -= 20;
				xVelocity = -1;
			}
			bounce = true;
			shooting = false;
			yVelocity = 5;
		} else if (!playerDribbling.getDirection() && x <= hoopx) {
			if (makeShot()) {
				Scoreboard.score1++;
				xVelocity = 0;
			} else {
				x += 20;
				xVelocity = 1;
			}
			bounce = true;
			shooting = false;
			yVelocity = 5;
		}
	}

	private boolean makeShot() {
		int r = (int) (Math.random() * 2);
		if (r == 0)
			return true;
		else
			return false;
	}

	// this method will calculate the y coordinate based off of a function
	// (parabola) representing the arc of the shot
	private double f(double x) {
		double a = equation[0];
		double h = equation[1];
		double k = equation[2];
		return (a * Math.pow(x - h, 2) + k);
	}

	private void calculateParabola(double hoopx, double hoopy) {
		equation = new double[3];
		if (playerDribbling.getDirection()) {
			if (shotx < 275)
				shotx += 75;
			else if (shotx >= 275 && shotx < 382)
				shotx += 50;
			else if (shotx >= 382 && shotx < 487)
				shotx += 25;
			else if (shotx >= 487)
				shotx += 10;
		} else {
			if (shotx < 275)
				shotx -= 75;
			else if (shotx >= 275 && shotx < 382)
				shotx -= 50;
			else if (shotx >= 382 && shotx < 487)
				shotx -= 25;
			else if (shotx >= 487)
				shotx -= 10;
		}

		double h = (hoopx + shotx) / 2;
		double a = (shoty - 50) / Math.pow(shotx - h, 2);
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

	public void setPlayer(Player p) {
		if (playerDribbling != null)
			playerDribbling.setHasBall(false);
		playerDribbling = p;
		p.setHasBall(true);
		this.setDribbling(true);
		onGround = false;
		this.dataUpdated = true;
	}

	public BallData getDataObject() {
		dataUpdated = false;
		BallData p = new BallData();
		p.username = username;
		p.x = x;
		p.y = y;
		p.onGround = onGround;
		return p;
	}

	public void syncWithDataObject(BallData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.onGround = data.onGround;
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

	public boolean isOnGround() {
		return onGround;
	}
}