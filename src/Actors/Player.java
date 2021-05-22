package Actors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.*;

import javax.swing.ImageIcon;

import Data.PlayerData;
import processing.core.PShape;

/**
 * The Player class represents a real life player in the game
 * 
 * @author adityapanikkar
 *
 */
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
	private int score;
	private boolean intersectsPlayer;
	private boolean stolen;

	private boolean right = true;
	private boolean shooting;

	private int energy;

	private int energyState;

	private String uniqueID;
	private String username;
	private double x, y;

	private boolean speedBoost = false;;
	private boolean jumpBoost = false;;

	private boolean dataUpdated;
	private boolean hasBall;
	private static String filename = "img/player.png";
	private boolean dash = false;
	private int shots;
	private int dashes;
	private int walks;
	private int jumps;

	/**
	 * Instantiates a new Player at (x, y) with username, a unique identifier, and a
	 * boolean that is true if the player possesses the ball or not
	 * 
	 * @param x        X coordinate of the player
	 * @param y        Y-coordinate of the player
	 * @param username Name of each player
	 * @param uniqueID The unique identifier of each player
	 * @param hasBall  True if player has ball and false if not
	 */
	public Player(int x, int y, String username, String uniqueID, boolean hasBall) {
		super(filename, x, y, MARIO_WIDTH, MARIO_HEIGHT);
		this.x = x;
		this.y = y;
		this.hasBall = hasBall;
		xVelocity = 0;
		yVelocity = 0;
		onASurface = false;
		gravity = 0.7;
		friction = .85;
		jumpStrength = 10;
		this.username = username;
		this.uniqueID = uniqueID;
		dataUpdated = false;
		energy = 2;
	}

	/**
	 * 
	 * @return returns the number of shots taken by the player
	 */
	public int getShots() {
		return shots;
	}
	
	public void setStolen(boolean x) {
		stolen = x;
	}

	/**
	 * 
	 * @return returns the number of times the player dashed
	 */
	public int getDashes() {
		return dashes;
	}

	/**
	 * 
	 * @return returns number of times the player took a step
	 */
	public int getWalks() {
		return walks;
	}

	/**
	 * 
	 * @return returns number of times a player has jumped
	 */
	public int getJumps() {
		return jumps;
	}
	
	public boolean hasStolen() {
		if (stolen) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Randomly spawns one of two possible powerups
	 * 
	 * @post Spawns a new random power up and player's speed or jump height is
	 *       increased
	 */
	public void spawnPowerup() {
		double x = (Math.random());
		if (x > 0.5) {
			speedPowerup = true;

		} else {
			jumpPowerup = true;

		}

	}

	/**
	 * Checks to see if the player currently has a powerup or does not
	 * 
	 * @return True if the powerup is on or false if not
	 */
	public boolean getPower() {

		if (speedBoost == true || jumpBoost == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return returns true if the player is currently dashing, otherwise returns
	 *         false
	 */
	private boolean isDashing() {
		return dash;
	}

	/**
	 * sets the dash variable to what is passed in as the parameter
	 * 
	 * @param dash the new value of the dash variable
	 * @post changes the dash variable
	 */
	private void setDash(boolean dash) {
		this.dash = dash;
	}

	/**
	 * Turns power up off when the timer ends
	 * 
	 * @post The power up is turned off
	 */
	public void powerOff() {
		if (speedBoost) {
			speedBoost = false;
		}

		if (jumpBoost) {
			jumpBoost = false;
		}
	}

	/**
	 * Changes the energy state
	 * 
	 * @param count What the energy state should be changed too
	 * @post Energy state is updated
	 */
	public void updateState(int count) {
		energyState = count;
	}

	/**
	 * The player fully regenerates it's energy
	 * 
	 * @post Energy is updated
	 */
	public void regenerate() {
		// System.out.println("Called");

		if (energy < 2) {
			energy++;
		}
	}

	/**
	 * Makes player dash
	 * 
	 * @post Dash is true and energy is decreased
	 */
	public void dash(Ball ball, Player player2) {
		// System.out.println(energy);
		dashes++;
		dash = true;
		if (energy > 0) {

			if (player2 == null) {
				if (right)
					x += 80;
				else
					x -= 80;
			} else {
				if (right) {
					x = player2.getX() + 80;
//					player2.setHasBall(false);
//					System.out.println(this.hasBall);
//					if (!hasBall) {
////						player2.setHasBall(false);
//					}
//					System.out.println(player2.hasBall());
//					if (!this.hasBall) {
//						player2.setHasBall(false);
//						this.hasBall = true;
//						ball.setPlayer(this);
//						dataUpdated = true;
//					}
				} else {
					x = player2.getX() - 80;
//					player2.setHasBall(false);
//					System.out.println(this.hasBall);
//					player2.setHasBall(false);
//					this.hasBall = true;
//					ball.setPlayer(this);
//					ball.setDribbling(true);
//					if (!hasBall) {
////						ball.setPlayerDribbling();
//						player2.setHasBall(false);
//					}
//					System.out.println(player2.hasBall());
//					if (!this.hasBall) {
//						player2.setHasBall(false);
//						this.hasBall = true;
//						ball.setPlayer(this);
//						dataUpdated = true;
//					}
				}
			}

			dataUpdated = true;

			energy--;
		}
//		if (right) {
//			if (x - 80 <= ball.getX() && x >= ball.getX())
//				ball.setPlayer(this);
//		} else {
//			if (x + 80 >= ball.getX() && x <= ball.getX())
//				ball.setPlayer(this);
//		}

	}

	/**
	 * Find and returns direction player is facing
	 * 
	 * @return True if facing right, false if facing left
	 */
	public boolean getDirection() {
		return right;
	}

	/**
	 * Sets the direction the player is facing
	 * 
	 * @param dir Direction to be set, true if right and false if left
	 * @post Direction of player is updated
	 */
	public void setDirection(boolean dir) {
		right = dir;
		dataUpdated = true;
	}

	/**
	 * This method represents the person taking a step and also includes the
	 * implementation of powerups
	 * 
	 * @param dir the magnitude of the step distance
	 * @post X, Y, xvelocity, and yvelocity are updated according to actions
	 *       executed
	 */
	public void walk(int dir) {
		walks++;
		intersectsPlayer = false;

		if (xVelocity <= 10 && xVelocity >= -10)

			if (this.x < 45) {
				this.x = 45;
			}

		if (this.x > 700) {
			this.x = 700;
		}

		if (speedBoost) {
			xVelocity += (double) dir;
		} else {

			xVelocity += (0.5) * (double) dir;
		}
	}

	/**
	 * This method represents the person jumping. it also accounts for the jump
	 * powerup
	 * 
	 * @post Y velocity of the player is updated
	 */
	public void jump() {

		if (onASurface) {
			jumps++;
			if (jumpBoost) {
				yVelocity -= (1.3) * jumpStrength;
				return;
			}

			yVelocity -= jumpStrength;

		}
	}

	public void increaseScore() {
		this.score++;
		this.dataUpdated = true;
	}

	/**
	 * This method represents the player acting and is called each time the game
	 * cycles
	 * 
	 * @param obstacles A list containing shapes representing obstacles in the game
	 * @param player2   Player in the game
	 * @post X, Y, and velocities are updated according to actions executed
	 */
	public void act(ArrayList<Shape> obstacles, Player player2) {
//		if (player2 != null) {
//			if (player2.dash && this.hasBall) {
//				this.hasBall = false;
//				System.out.println(player2.dash);
//				this.dataUpdated = true;	
//			}
//		}
		
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();

		if (x > 383 & x < 413 & y > 230 & y < 300 & speedPowerup) {
			speedPowerup = false;
			speedBoost = true;
		}

		if (x > 383 & x < 413 & y > 230 & y < 300 & jumpPowerup) {
			jumpPowerup = false;
			jumpBoost = true;
		}

		yVelocity += gravity; // GRAVITY
		double yCoord2 = yCoord + yVelocity;

		Rectangle2D.Double strechY = new Rectangle2D.Double(xCoord, Math.min(yCoord, yCoord2), width,
				height + Math.abs(yVelocity));

		onASurface = false;

		if (player2 != null) {
			if (strechY.intersects(player2)) {
//				this.hasBall = false;
				xVelocity = -(xVelocity);
			}
//			if (player2.getX() - 80 == x) {
//				this.hasBall = false;
//			}
		}

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
				yCoord2 = r.getY() - height;
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
				yCoord2 = r.getY() + r.getHeight();
			}
		}

		if (Math.abs(yVelocity) < .2)
			yVelocity = 0;

		// ***********X AXIS***********

		xVelocity *= friction;

		double xCoord2 = xCoord + xVelocity;

		Rectangle2D.Double strechX = new Rectangle2D.Double(Math.min(xCoord, xCoord2), yCoord2,
				width + Math.abs(xVelocity), height);

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
				xCoord2 = r.getX() - width;
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
				xCoord2 = r.getX() + r.getWidth();
			}
		}

		if (Math.abs(xVelocity) < .2)
			xVelocity = 0;

		this.x = xCoord2;
		this.y = yCoord2;

		dataUpdated = true;

		moveToLocation(xCoord2, yCoord2);

	}

	/**
	 * Gets the PlayerData object containing data about the player
	 * 
	 * @return PlayerData object containing data
	 */
	public PlayerData getDataObject() {
		dataUpdated = false;
		PlayerData p = new PlayerData();
		p.username = username;
		p.x = x;
		p.y = y;
		p.right = right;
		p.hasBall = hasBall;
		p.score = score;
		return p;
	}

	/**
	 * Syncs the player with the player data object
	 * 
	 * @param data Object containing player data
	 * @post Player is updated and synced with player data given
	 */
	public void syncWithDataObject(PlayerData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.username = data.username;
		this.right = data.right;
		this.hasBall = data.hasBall;
		this.score = data.score;
	}

	/**
	 * Checks to see if the player id matches
	 * 
	 * @param uid The ID to check with
	 * @return True if it is a match, false if otherwise
	 */
	public boolean idMatch(String uid) {
		return this.uniqueID.equals(uid);
	}

	/**
	 * Finds and gets whether the speed powerup is on or not
	 * 
	 * @return True if yes, false if not
	 */
	public boolean getSpeedPowerup() {
		return this.speedPowerup;
	}

	/**
	 * Gets whether the jump powerup is on or not
	 * 
	 * @return True if on, false if not
	 */
	public boolean getJumpPowerup() {
		return this.jumpPowerup;
	}

	/**
	 * Checks to see whether the data is changed or not
	 * 
	 * @return True if data has been updated, false if not
	 */
	public boolean isDataChanged() {
		return dataUpdated;
	}

	/**
	 * Gets the username of the player
	 * 
	 * @return String representing the username of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Finds the X coordinate of the player
	 * 
	 * @return The x coordinate of the player
	 */
	public double getX() {
		return x;
	}

	/**
	 * Finds the y coordinate of the player
	 * 
	 * @return The y coordinate of the player
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the player ID
	 * 
	 * @return String containing the unique player ID
	 */
	public String getID() {
		return uniqueID;
	}

	/**
	 * Gets whether the player has the ball or not
	 * 
	 * @return True if the player does, false if not
	 */
	public boolean hasBall() {
		return hasBall;
	}

	/**
	 * Sets whether the player has the ball or not
	 * 
	 * @param x True if the player should have the ball and false if not
	 * @post The state whether the player has the ball or not is updated
	 */
	public void setHasBall(boolean x) {
		this.hasBall = x;
		dataUpdated = true;
	}

	/**
	 * Finds and returns the energy of the player
	 * 
	 * @return the current energy of the player
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * This method represents the player taking a shot
	 * 
	 * @post shooting state of the player is updated as well as energy is decreased
	 */
	public void shoot() {
		
		
		if (energy > 0) {
	
			shots++;
			this.shooting = true;

			energy--;
		}
	}

	/**
	 * Sets the shooting state of the player
	 * 
	 * @param x True if the player should be shooting and false if otherwise
	 * @post Shooting condition is updated of the player
	 */
	public void setShooting(boolean x) {
		this.shooting = x;
	}

	/**
	 * Checks to see if the player is shooting or not
	 * 
	 * @return True if the player is, false if not
	 */
	public boolean isShooting() {
		return shooting;
	}

	/**
	 * Finds the energy state of the player
	 * 
	 * @return The energy state of the current player
	 */
	public int getEnergyState() {
		return this.energyState;
	}

	public int getScore() {
		return this.score;
	}

	/**
	 * Draws powerups and usernames of the player
	 * 
	 * @param g  The Graphics needed to draw components to screen
	 * @param io Necessary to draw images
	 * @post The screen is updated with drawn components
	 */
	public void draw(Graphics g, ImageObserver io) {
//		System.out.println((int) x + " " + (int) y);
		// g.drawImage(img, x, y, width, height, observer)
		g.setColor(Color.white);
		g.drawString(this.username, (int) x, (int) y);

		if (speedPowerup) {
			g.setColor(Color.red);
			g.fill3DRect(383, 260, 30, 30, false);
		}

		if (jumpPowerup) {
			g.setColor(Color.blue);
			g.fill3DRect(383, 260, 30, 30, false);
		}

		g.setColor(Color.green);

		g.drawRect((int) x + 10, (int) y - 50, 20, 30);

		if (energyState == 1) {
			g.fillRect((int) x + 10, (int) y - 35, 20, 15);
		}

		if (energyState == 2) {

			g.fillRect((int) x + 10, (int) y - 50, 20, 30);
		}

		if (!hasBall) {
			if (right) {
				g.drawImage((new ImageIcon("img/player3.png")).getImage(), (int) x, (int) y, (int) width, (int) height,
						io);
			} else {
				g.drawImage((new ImageIcon("img/player4.png")).getImage(), (int) x, (int) y, (int) width, (int) height,
						io);
			}
//		} else if (shooting) {
//			g.drawImage((new ImageIcon("img/playershoot.png")).getImage(), (int) x, (int) y, (int) width, (int) height,
//					io);
		} else if (right) {
			g.drawImage((new ImageIcon(filename)).getImage(), (int) x, (int) y, (int) width, (int) height, io);
		} else {
			g.drawImage((new ImageIcon("img/player2.png")).getImage(), (int) x, (int) y, (int) width, (int) height, io);
		}

	}

}
