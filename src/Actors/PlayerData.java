package Actors;

import java.util.ArrayList;

/**
 * 
 * @author anirudhv
 *
 */
public class PlayerData {
	
	public String username;
	public double x, y;
	public boolean right;
	public boolean hasBall;
	
	// One thing that is interesting is that the Firebase database cannot store arrays.
	// So, if you want to use a library class that uses arrays (the Color class is one such example), then
	// you need to store the data a different, simpler way yourself.
	// Note that ArrayLists *can* be stored.
	
	/**
	 * Instantiates playerdata to be stored in the firebase
	 */
	public PlayerData() {
		
	}
	
	/**
	 * Gets the direction of the player
	 * 
	 * @return true if the player is right facing, false otherwise
	 */
	public boolean getRight() {
		return right;
	}
	
	/**
	 * Checks if player has the ball
	 * 
	 * @return true if the player has the ball, false otherwise
	 */
	public boolean hasBall() {
		return hasBall;
	}
	
	/**
	 * Gets the username of the player
	 * 
	 * @return the username of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the x position of the player
	 * 
	 * @return the x position of the player
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y position of the player
	 * 
	 * @return the y position of the player
	 */
	public double getY() {
		return y;
	}
	
}
