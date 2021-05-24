package Data;

/**
 * 
 * @author anirudhv
 *
 */
public class BallData {

	public String username;
	public double x, y;
	public boolean onGround;
	public boolean inAir;

	// One thing that is interesting is that the Firebase database cannot store
	// arrays.
	// So, if you want to use a library class that uses arrays (the Color class is
	// one such example), then
	// you need to store the data a different, simpler way yourself.
	// Note that ArrayLists *can* be stored.

	/**
	 * Instantiates a ball data object to store in the firebase
	 */
	public BallData() {

	}

	/**
	 * Gets the username of the ball
	 * 
	 * @return username of the ball
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the x position of the ball
	 * 
	 * @return the x position of the ball
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y position of the ball
	 * 
	 * @return the y position of the ball
	 */
	public double getY() {
		return y;
	}

	/**
	 * Checks if the ball is on the ground
	 * 
	 * @return boolean if the ball is on the ground or not
	 */
	public boolean isOnGround() {
		return onGround;
	}
	
	/**
	 * 
	 * @return returns true if ball is in the air
	 */
	public boolean isInAir() {
		return inAir;
	}

}