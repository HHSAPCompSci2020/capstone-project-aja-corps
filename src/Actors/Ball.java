package Actors;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;

import Data.BallData;
import Graphics.Home;
import Graphics.PlayerStats;

/**
 * The Ball class represents a real life basketball in the Game.
 * 
 * @author josh_choi
 *
 */
public class Ball extends MovingImage {

	private double xVelocity, yVelocity, shotx, shoty;
	private boolean dribbling = false;
	private Player playerDribbling;
	private boolean shooting;
	private boolean inAir;
	private boolean bounce = false;

	private String uniqueID;
	private boolean dataUpdated;
	private boolean onGround = true;

	private final double CONSTANT = 0.3;
	private double probability;
	private int bounceCount = 0;

	private double factor = 0;

	private double[] equation;
	private boolean blocked;

	private String username;

	/**
	 * Instantiates a new Ball at initial coordinates (x, y) with the specified size
	 * 
	 * @param x        Initial X-Coordinate of the ball
	 * @param y        Initial Y-Coordinate of the ball
	 * @param width    Width of the basketball
	 * @param height   Height of the basketball
	 * @param username Name of the ball
	 * @param uniqueID A string used to uniquely identify the ball
	 */
	public Ball(int x, int y, int width, int height, String username, String uniqueID) {
		super("img/basketball.png", x, y, width, height);
		xVelocity = 0;
		yVelocity = 4;
		this.uniqueID = uniqueID;
		this.username = username;
	}

	/**
	 * Executes actions that allow the players to block each other's shots
	 * 
	 * @param p       Player currently shooting the ball
	 * @param player2 Opponent who is able to block the shot
	 * @param floorY  The Y coordinate of the floor
	 * @post The ball is either blocked or continues on its path
	 */
	public void block(Player p, Player player2, double floorY) {
		if (p.isShooting()) {
			if (this.intersects(player2)) {
				p.setShooting(false);
				inAir = false;
				onGround = false;
				shooting = false;
				playerDribbling = null;
			}
		} else {
			if (this.intersects(p) && inAir) {
				this.setPlayer(p);
				this.setDribbling(true);
				inAir = false;
				y = 280;
			}
		}
	}

	/**
	 * Executes ball actions, Calculates velocities of the ball, and updates
	 * coordinates of the ball according to movement or action
	 * 
	 * @param p      The player that is doing the action upon the ball
	 * @param floorY The Y-Coordinate of the floor
	 * @post X-coordinate, Y-Coordinate, X velocity, Y velocity, and appropriate
	 *       booleans are changed according to action
	 */
	public void act(Player p, double floorY) {
		if (this.x < 45) {
			this.x = 45;
		}

		if (this.x > 700) {
			this.x = 700;
		}
		if ((this.intersects(p) && (onGround))) {
			System.out.println("intersection!");
			p.setHasBall(true);
			this.setPlayer(p);
			this.setDribbling(true);
			inAir = false;
			onGround = false;
			shooting = false;
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
			bounce(p);
		}

		if (y > floorY)
			moveToGround();
		this.dataUpdated = true;
	}

	/**
	 * Updates the player who is dribbling to null and gets rid of ownership of the
	 * ball
	 * 
	 * @post Player currently dribbling the ball is null
	 */
	public void setPlayerDribbling() {
		this.playerDribbling = null;
		this.playerDribbling.setHasBall(false);
	}

	/**
	 * Updates the dribbling status of the ball
	 * 
	 * @param x True if the ball should start dribbling and false if not
	 * @post The ball stops horizontally moving but vertically by default moves down
	 */
	public void setDribbling(boolean x) {
		bounceCount = 0;
		xVelocity = 0;
		yVelocity = 4;
		this.dribbling = x;
		this.bounce = false;
		inAir = false;
	}

	/**
	 * Checks to see if ball is being controlled by player or not
	 * 
	 * @return True if there is a player and false if not
	 */
	public boolean hasPlayer() {
		if (playerDribbling == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * The action of the ball bouncing
	 * 
	 * @param p The player that was bouncing the ball
	 * @post The x and y coordinates are updated as well as the appropriate
	 *       velocities of the ball
	 */
	public void bounce(Player p) {
		if (bounceCount >= 5) {
			yVelocity = 0;
			moveToGround();
			p.setHasBall(false);
			p.setShooting(false);
			playerDribbling = null;
			inAir = false;
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
		} else {
			yVelocity += CONSTANT;
		}
	}

	/**
	 * Moves ball to the ground
	 * 
	 * @post The y coordinate of the ball is 280
	 */
	public void moveToGround() {
		this.y = 280;
	}

	/**
	 * The action of the ball being dribbled by a player
	 * 
	 * @param floorY The y coordinate of the floor
	 * @pre The player dribbling the ball is not null
	 * @post Appropriate x, y, and velocities are updated
	 */
	public void dribble(double floorY) {

		floorY = 300;
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

	/**
	 * The action of the ball being shot by a player
	 * 
	 * @param hoopx The x coordinate of the hoop
	 * @param hoopy The y coordinate of the hoop
	 * @post The ball is on its trajectory in the arc motion towards the hoop. X, Y,
	 *       and velocities are updated
	 */
	public void shoot(double hoopx, double hoopy) {
		if (playerDribbling.playerType == 1 && !playerDribbling.getDirection()) {
			return;
		} else if (playerDribbling.playerType == 2 && playerDribbling.getDirection()) {
			return;
		}

		boolean close = false;
		boolean midrange = false;
		if (dribbling) {
			shotx = playerDribbling.getX() + 25;
			shoty = hoopy;
			x = shotx;
			y = shoty;
			dribbling = false;

			if (playerDribbling.getDirection()) {
				if (shotx < 275) {
					xVelocity = 6;
					probability = 0.25;
				} else if (shotx >= 275 && shotx < 382) {
					xVelocity = 4;
					probability = 0.3;
				} else if (shotx >= 382 && shotx < 487) {
					xVelocity = 3;
					probability = 0.5;
				} else if (shotx >= 487 && playerDribbling.getX() <= 570) {
					xVelocity = 2;
					probability = 0.85;
					midrange = true;
				} else if (shotx >= 570 && playerDribbling.getX() < 627) {
					xVelocity = 0.5;
					probability = 0.9;
					close = true;
				} else if (playerDribbling.getX() >= 627) {
					dribbling = true;
					return;
				}
			} else {
				if (playerDribbling.getX() <= 125) {
					dribbling = true;
					return;
				} else if (playerDribbling.getX() < 210) {
					close = true;
					xVelocity = -0.5;
					probability = 0.9;
				} else if (shotx <= 275) {
					xVelocity = -2;
					probability = 0.85;
					midrange = true;
				} else if (shotx >= 275 && shotx < 382) {
					xVelocity = -3;
					probability = 0.5;
				} else if (shotx >= 382 && shotx < 487) {
					xVelocity = -4;
					probability = 0.3;
				} else if (shotx >= 487) {
					xVelocity = -6;
					probability = 0.25;
				}
			}

			if (close)
				layupMotion(hoopx, hoopy);
			else if (midrange)
				midrangeMotion(hoopx, hoopy);
			else
				calculateParabola(hoopx, hoopy);
			shooting = true;
		}
		if (shooting == true) {
			playerDribbling.setHasBall(false);
			inAir = true;
			x += xVelocity;
			y = f(x);
		}

		if (playerDribbling.getDirection() && x >= hoopx) {
			if (makeShot()) {
				if (probability <= 0.5)
					playerDribbling.increaseScore(3);
				else
					playerDribbling.increaseScore(2);
				xVelocity = 0;
			} else {
				x -= 20;
				xVelocity = -1.5;
			}
			bounce = true;
			shooting = false;
			yVelocity = 5;
		} else if (!playerDribbling.getDirection() && x <= hoopx) {
			if (makeShot()) {
				if (probability <= 0.5)
					playerDribbling.increaseScore(3);
				else
					playerDribbling.increaseScore(2);
				xVelocity = 0;
			} else {
				x += 20;
				xVelocity = 1.5;
			}
			bounce = true;
			shooting = false;
			yVelocity = 5;
		}

	}

	/**
	 * 
	 * @param hoopx the hoop at which the shot starts
	 * @param hoopy the hoop at which the shot ends
	 */
	private void midrangeMotion(double hoopx, double hoopy) {
		equation = new double[3];
		if (playerDribbling.getDirection()) {
			x = 550;
			shotx = x;
			y = hoopy;
			shoty = y;
		} else {
			x = 225;
			shotx = x;
			y = hoopy;
			shoty = y;
		}

		double h = (hoopx + shotx) / 2;
		double a = (shoty - 50) / Math.pow(shotx - h, 2);
		double k = 50;
		equation[0] = a;
		equation[1] = h;
		equation[2] = k;
	}

	private void layupMotion(double hoopx, double hoopy) {
		equation = new double[3];
		if (playerDribbling.getDirection()) {
			x = 620;
			shotx = x;
			y = hoopy;
			shoty = y;
		} else {
			x = 150;
			shotx = x;
			y = hoopy;
			shoty = y;
		}

		double h = (hoopx + shotx) / 2;
		double a = (shoty - 50) / Math.pow(shotx - h, 2);
		double k = 50;
		equation[0] = a;
		equation[1] = h;
		equation[2] = k;
	}

	/**
	 * Finds and gets to see if the ball is currently in the shooting process or not
	 * 
	 * @return True if the ball is shooting and false if not
	 */
	public boolean isShooting() {
		return shooting;
	}

	/**
	 * 
	 * @return returns wether or not the shot will go in
	 */
	private boolean makeShot() {
		double random = (Math.random());
		probability += factor;
		if (random < probability)
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

	/**
	 * 
	 * @param hoopx the x coord of the hoop
	 * @param hoopy the y coord of the hoop
	 */

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

	/**
	 * Sets the player to having possession of the ball
	 * 
	 * @param p The player who possesses the ball
	 * @post The player who owns the ball is updated
	 */
	public void setPlayer(Player p) {
		if (playerDribbling != null)
			playerDribbling.setHasBall(false);
		playerDribbling = p;
		p.setHasBall(true);
		this.setDribbling(true);
		onGround = false;
		inAir = false;
		this.dataUpdated = true;
	}

	/**
	 * Gets the data about the ball object for the online services
	 * 
	 * @return BallData object containing data about the ball object
	 */
	public BallData getDataObject() {
		dataUpdated = false;
		BallData p = new BallData();
		p.username = username;
		p.x = x;
		p.y = y;
		p.onGround = onGround;
		p.inAir = inAir;
		return p;
	}

	/**
	 * Syncs the ball with the current ball data object
	 * 
	 * @param data The ball data object used
	 * @post Data about ball is updated
	 */
	public void syncWithDataObject(BallData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.onGround = data.onGround;
		this.inAir = data.inAir;
		this.username = data.username;
	}

	/**
	 * Checks to see if the id matches
	 * 
	 * @param uid The user id to check for matching
	 * @return True if it is a match and false if not
	 */
	public boolean idMatch(String uid) {
		return this.uniqueID.equals(uid);
	}

	/**
	 * Gets the ID of the ball object
	 * 
	 * @return A String containing the ball ID
	 */
	public String getID() {
		return this.uniqueID;
	}

	/**
	 * Checks to see if data is changed
	 * 
	 * @return true if data is updated and false if not
	 */
	public boolean isDataChanged() {
		return dataUpdated;
	}

	/**
	 * 
	 * @return returns true if the ball is being dribbled
	 */
	public boolean getDribbling() {
		return dribbling;
	}

	/**
	 * Gets the username of the ball
	 * 
	 * @return String containing the name of the ball
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Finds and gets the X coordinate of the ball
	 * 
	 * @return double containing the x coordinate of the ball
	 */
	public double getX() {
		return x;
	}

	/**
	 * Finds and gets the Y coordinate of the ball
	 * 
	 * @return double containing the y coordinate of the ball
	 */
	public double getY() {
		return y;
	}

	/**
	 * Increases the probability of the player making a shot
	 * 
	 * @post the percentage is increased by 20 percent
	 */
	public void increaseProbability() {
		factor = 0.2;
	}

	/**
	 * Returns the probability of making a shot back to normal
	 * 
	 * @post the percentage of making a shot is back to normal
	 */
	public void decreaseProbability() {
		factor = 0;
	}

	/**
	 * Checks to see if the ball is on the ground or not
	 * 
	 * @return True if it is and false if not
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Checks to see if the ball is in the air or not
	 * 
	 * @return True if the ball is in air, and false if not
	 */
	public boolean isInAir() {
		return inAir;
	}

	@Override
	public void draw(Graphics g, ImageObserver io) {
		try {
			g.drawImage(ImageIO.read(getClass().getClassLoader().getResource("img/basketball.png")), (int) x, (int) y,
					(int) width, (int) height, io);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}