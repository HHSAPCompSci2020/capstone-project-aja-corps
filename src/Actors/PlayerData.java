package Actors;

import java.util.ArrayList;

/**
 * 
 *  The class you store in the database must fit 2 simple constraints:

	The class must have a default constructor that takes no arguments.
	The class must define public getters for the properties to be assigned. Properties without a public getter will be set to their default value when an instance is deserialized.
	
	Classes from the Java library will often not fit these requirements, so you may need to make simpler classes
	yourself.
	
	
	
	I recommend that you *only* use this class for database posts. Don't use this class for storing
	real data in your program. Just create these objects at the moment you want to put something in the
	database, and when you read from the database, quickly turn these objects into some other form that
	is more useful.
	
	
 * 
 * 
 * @author john_shelby
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
	
	public PlayerData() {
		
	}
	
	public boolean getRight() {
		return right;
	}
	
	public boolean hasBall() {
		return hasBall;
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
