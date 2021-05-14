package Actors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.*;

import javax.swing.ImageIcon;

import processing.core.PShape;

/**
 * 
 * 
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

	public Player(int x, int y, String username, String uniqueID, boolean hasBall) {
		super(filename, x, y, MARIO_WIDTH, MARIO_HEIGHT);
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
		energy = 1;
	}

	public void spawnPowerup() {
		double x = (Math.random());
		if (x > 0.5) {
			speedPowerup = true;

		} else {
			jumpPowerup = true;

		}

	}

	public boolean getPower() {

		if (speedBoost == true || jumpBoost == true) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isDashing() {
		return dash;
	}
	private void setDash(boolean dash) {
		this.dash = dash;
	}
	public void powerOff() {
		if (speedBoost) {
			speedBoost = false;
		}

		if (jumpBoost) {
			jumpBoost = false;
		}
	}

	public void updateState(int count) {
		energyState = count;
	}

	public void regenerate() {
		// System.out.println("Called");

		if (energy < 1) {
			energy++;
		}
	}

	/**
	 * makes player dash
	 */
	public void dash(Ball ball) {
		// System.out.println(energy);

		dash = true;
		if (energy > 0) {

			if (right)
				x += 80;
			else
				x -= 80;
			dataUpdated = true;

			energy--;
		}
		if(right) {
			if(x - 80 <= ball.getX() && x >= ball.getX())
				ball.setPlayer(this);
		} else {
			if(x + 80 >= ball.getX() && x <= ball.getX())
				ball.setPlayer(this);
		}

	}

	public boolean getDirection() {
		return right;
	}

	public void setDirection(boolean dir) {
		right = dir;
		dataUpdated = true;
	}

	/**
	 * 
	 * @param dir the magnitude of the step distance
	 * 
	 *            This method represents the person taking a step. This method also
	 *            includes the implementation of the powerup
	 */
	public void walk(int dir) {

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
	 * this method represents the person jumping. it also accounts for the powerups
	 */
	public void jump() {
		if (onASurface) {

			if (jumpBoost) {
				yVelocity -= (1.3) * jumpStrength;
				return;
			}

			yVelocity -= jumpStrength;

		}
	}

	public void act(ArrayList<Shape> obstacles, Player player2) {

		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();

		// 383, 260, 30, 30,false);
		if (x > 383 & x < 413 & y > 230 & y < 270 & speedPowerup) {
			speedPowerup = false;
			speedBoost = true;
		}

		if (x > 383 & x < 413 & y > 230 & y < 270 & jumpPowerup) {
			jumpPowerup = false;
			jumpBoost = true;
		}

		// ***********Y AXIS***********

		yVelocity += gravity; // GRAVITY
		double yCoord2 = yCoord + yVelocity;

		Rectangle2D.Double strechY = new Rectangle2D.Double(xCoord, Math.min(yCoord, yCoord2), width,
				height + Math.abs(yVelocity));

		onASurface = false;

		if (player2 != null) {
			if (strechY.intersects(player2)) {
				// System.out.println("Intersection!");
				xVelocity = 0;
				// if (this.hasBall) {
				// hasBall = false;
				// }
			}
		}

		// if (b != null && !b.hasPlayer()) {
		//// System.out.println("Ball on the ground!");
		// if (b.intersects(this)) {
		// System.out.println("Intersection with " + this.getID());
		// this.hasBall = true;
		// b.getPlayer(this);
		// b.setDribbling(true);
		// dataUpdated = true;
		// System.out.println(this.hasBall);
		// }
		// }
		//
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

	public PlayerData getDataObject() {
		dataUpdated = false;
		PlayerData p = new PlayerData();
		p.username = username;
		p.x = x;
		p.y = y;
		p.right = right;
		p.hasBall = hasBall;
		return p;
	}

	public void syncWithDataObject(PlayerData data) {
		dataUpdated = false;
		this.x = data.x;
		this.y = data.y;
		this.username = data.username;
		this.right = data.right;
		this.hasBall = data.hasBall;
	}

	public boolean idMatch(String uid) {
		return this.uniqueID.equals(uid);
	}

	public boolean getSpeedPowerup() {
		return this.speedPowerup;
	}
	
	public boolean getJumpPowerup() {
		return this.jumpPowerup;
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

	public String getID() {
		return uniqueID;
	}

	public boolean hasBall() {
		return hasBall;
	}

	public void setHasBall(boolean x) {
		this.hasBall = x;
	}

	public int getEnergy() {
		return energy;
	}

	public void shoot() {
		if (energy > 0) {

			this.shooting = true;

			energy--;
		}
	}

	public void setShooting(boolean x) {
		this.shooting = x;
	}

	public boolean isShooting() {
		return shooting;
	}
	
	public int getEnergyState() {
		return this.energyState;
	}

	@Override
	public void draw(Graphics g, ImageObserver io) {

		// g.drawImage(img, x, y, width, height, observer)

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
			g.fillRect((int) x + 10, (int) y - 30, 20, 10);
		}

		if (energyState == 2) {

			g.fillRect((int) x + 10, (int) y - 40, 20, 20);
		}

		if (energyState == 3) {
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
		} else if (shooting) {
			g.drawImage((new ImageIcon("img/playershoot.png")).getImage(), (int) x, (int) y, (int) width, (int) height,
					io);
		} else if (right) {
			g.drawImage((new ImageIcon(filename)).getImage(), (int) x, (int) y, (int) width, (int) height, io);
		} else {
			g.drawImage((new ImageIcon("img/player2.png")).getImage(), (int) x, (int) y, (int) width, (int) height, io);
		}

	}

}
