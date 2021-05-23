package Actors;

import Data.BallData;
import Data.Sound;
import Data.SoundEffect;
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
	private boolean dribbling = true;
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

	private double[] equation;
	private boolean blocked;
	
	private SoundEffect soundEffect;

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
		
		//soundEffect = new SoundEffect();
	}
	
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
//				p.setShooting(false);
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
//			System.out.println("bouncing");
			bounce(p);
		}

		if (y > floorY)
			moveToGround();

		// if(x > 800)
		// setDribbling(true);

		this.dataUpdated = true;
	}

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
			//soundEffect.soundEffect(0);
			// System.out.println(yVelocity);
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
	 * @post Appropriate x, y, and velocities are updated
	 */
	public void dribble(double floorY) {

		floorY = 300; // hardcoded for now
		if (y >= floorY) {
			yVelocity = -yVelocity;
			//soundEffect.soundEffect(0);
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
		if (dribbling) {
			shotx = playerDribbling.getX() + 25;
			shoty = hoopy;
			x = shotx;
			y = shoty;
			dribbling = false;
			
			if (playerDribbling.getDirection()) {
				if (shotx < 275) {
					xVelocity = 6;
					probability = 0.4;
				} else if (shotx >= 275 && shotx < 382) {
					xVelocity = 4;
					probability = 0.5;
				} else if (shotx >= 382 && shotx < 487) {
					xVelocity = 3;
					probability = 0.75;
				} else if (shotx >= 487) {
					xVelocity = 1;
					probability = 0.9;
				}
			} else {
				if (shotx < 275) {
					xVelocity = -4;
					probability = 0.9;
				} else if (shotx >= 275 && shotx < 382) {
					xVelocity = -4;
					probability = 0.75;
				} else if (shotx >= 382 && shotx < 487) {
					xVelocity = -4;
					probability = 0.5;
				} else if (shotx >= 487) {
					xVelocity = -6;
					probability = 0.4;
				}
			}
			
			calculateParabola(hoopx, hoopy);
			//if(playerDribbling.getDirection())
			shooting = true;
		}
//		Player p = playerDribbling;
		if (shooting == true) {
//			playerDribbling = null;
			playerDribbling.setHasBall(false);
			inAir = true;
			x += xVelocity;
//			x += 0.5;
			y = f(x);
		}

		if (playerDribbling.getDirection() && x >= hoopx) {
			if (makeShot()) {
//				PlayerStats.score2++;
				playerDribbling.increaseScore();
				xVelocity = 0;
			} else {
				x -= 20;
				xVelocity = -1.5;
			}
			bounce = true;
//			playerDribbling.setShooting(false);
			shooting = false;
			yVelocity = 5;
		} else if (!playerDribbling.getDirection() && x <= hoopx) {
			if (makeShot()) {
//				PlayerStats.score1++;
				playerDribbling.increaseScore();
				xVelocity = 0;
			} else {
				x += 20;
				xVelocity = 1.5;
			}
			bounce = true;
//			playerDribbling.setShooting(false);
			shooting = false;
			yVelocity = 5;
		}
		
	}
	
	public boolean isShooting() {
		return shooting;
	}

	private boolean makeShot() {
		double random = (Math.random());
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
	 * Checks to see if the ball is on the ground or not
	 * 
	 * @return True if it is and false if not
	 */
	public boolean isOnGround() {
		return onGround;
	}
	
	public boolean isInAir() {
		return inAir;
	}
}