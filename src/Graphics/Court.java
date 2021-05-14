package Graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import Actors.Ball;
import Actors.BallData;
import Actors.Player;
import Actors.PlayerData;

import java.util.*;

import java.util.Queue;
import java.io.Serializable;

/**
 * Represents the court for the game
 * 
 * @author anirudhv
 *
 */
public class Court extends JPanel implements Runnable {
	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 322;

	private Rectangle screenRect;
	private int timeCounter;
	private int barCounter;
	private int powerCounter;
	private int stopCounter;

	// private Ball ball;
	private Image backgroundImage;
	private ArrayList<Shape> obstacles;

	private KeyHandler keyControl;
	private ArrayList<Player> entities = new ArrayList<>();

	protected Player me;
	protected Ball ball;
	private ArrayList<Player> players;
	private ArrayList<Ball> balls;
	private Scoreboard scoreboard;

	// Database stuff
	private DatabaseReference roomRef; // This is the database entry for the whole room
	private DatabaseReference myUserRef; // This is the database entry for just our user's data. This allows us to more
											// easily update ourselves.
	private DatabaseReference myBallRef;

	private boolean currentlySending; // These field allows us to limit database writes by only sending data once
										// we've received confirmation the previous data went through.

	/**
	 * Instantiates a new court at the database reference in Firebase and with the
	 * name of the player
	 * 
	 * @param roomRef    Firebase database reference
	 * @param playerName Player username
	 */
	public Court(DatabaseReference roomRef, String playerName) {
		super();

		try {
			backgroundImage = ImageIO.read(new File("img/court.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		keyControl = new KeyHandler();
		setBackground(Color.CYAN);
		screenRect = new Rectangle(0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		obstacles.add(new Rectangle(0, 300, 800, 22));

		this.roomRef = roomRef;
		currentlySending = false;

		roomRef.child("users").addChildEventListener(new UserChangeListener());
		roomRef.child("balls").addChildEventListener(new BallChangeListener());

		// DatabaseReference x = roomRef.child("users");

		myUserRef = roomRef.child("users").push();
		myBallRef = roomRef.child("balls").push();

		players = new ArrayList<Player>();
		balls = new ArrayList<Ball>();

		me = new Player(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 50, playerName, myUserRef.getKey(), false);
		// ball = new Ball(300, 288, 20, 20, "TestBall", myBallRef.getKey());

		myUserRef.setValueAsync(me.getDataObject());
		// myBallRef.setValueAsync(ball.getDataObject());

		System.out.println(roomRef.child("users"));

		scoreboard = new Scoreboard();

		new Thread(this).start();
	}

	/**
	 * Overridden method in order to draw necessary components of the court (player,
	 * ball, etc)
	 * 
	 * @param Graphics g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call JPanel's paintComponent method to paint the background

		Graphics2D g2 = (Graphics2D) g;

		int width = getWidth();
		int height = getHeight();

		double ratioX = (double) width / DRAWING_WIDTH;
		double ratioY = (double) height / DRAWING_HEIGHT;

		AffineTransform at = g2.getTransform();
		g2.scale(ratioX, ratioY);

		g.setColor(new Color(205, 102, 29));
		for (Shape s : obstacles) {
			g2.fill(s);
		}

		g.drawImage(backgroundImage, 0, 0, this);

		g.drawRect(130, 140, 20, 20); // ball class needs this for debugging, KEEP THIS IN
		g.drawRect(640, 140, 20, 20);
		g.drawLine(0, 50, width, 50);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).draw(g2, this);
		}

		// for (int i = 0; i < balls.size(); i++) {
		// balls.get(i).draw(g2, this);
		// }

		if (ball != null) {
			ball.draw(g2, this);
		}

		me.draw(g2, this);
		scoreboard.draw(g2);
		g2.setTransform(at);

		// TODO Add any custom drawings here
	}

	/**
	 * Spawns a new ball in the court
	 */
	public void spawnNewBall() {
		ball = new Ball(300, 288, 20, 20, "TestBall", myBallRef.getKey());
		myBallRef.setValueAsync(ball.getDataObject());
	}

	/**
	 * Gets the key handler necessary to use the keyboard to make movements
	 * 
	 * @return keyControl (handler of keyboard movements)
	 */
	public KeyHandler getKeyHandler() {
		return keyControl;
	}

	/**
	 * Listener for any key events that determine player movements
	 * 
	 */
	public void enableKeys() {
		me.setShooting(false);
		if (keyControl.isPressed(KeyEvent.VK_LEFT)) {
			me.setDirection(false);
			me.walk(-1);
		}

		if (keyControl.isPressed(KeyEvent.VK_RIGHT)) {
			me.setDirection(true);
			me.walk(1);
		}

		if (keyControl.isPressed(KeyEvent.VK_UP)) {
			me.jump();
		}

		if (keyControl.isPressed(KeyEvent.VK_SHIFT)) {
			me.dash(ball);
		}

		if (keyControl.isPressed(KeyEvent.VK_SPACE)) {

			if (me.getEnergy() == 1) {
				me.shoot();
				if (me.getDirection())
					ball.shoot(640, 140);
				else
					ball.shoot(130, 140);
				me.shoot();
			}
		}

		if (keyControl.isPressed(KeyEvent.VK_ENTER)) {
			spawnNewBall();
		}
	}

	/**
	 * Overide method from the Runnable class that allows the game to run over and
	 * over again in this method
	 * 
	 */
	public void run() {

		while (true) { // Modify this to allow quitting
			long startTime = System.currentTimeMillis();

			// System.out.println(me.hasBall());
			timeCounter++;
			barCounter++;
			powerCounter++;
			stopCounter++;

			if (me.getPower() == false) {
				stopCounter = 0;
			}

			if (timeCounter == 1) {
				me.spawnPowerup();
			}

			if (powerCounter % 1500 == 0) {
				me.spawnPowerup();

			}

			if (stopCounter == 300) {
				me.powerOff();
			}

			if (timeCounter == 1) {
				spawnNewBall();
			}

			if (me.getEnergy() == 1) {
				barCounter = 0;
				me.updateState(3);
			}

			if (me.getEnergy() == 0 && barCounter <= 70 && barCounter > 0) {
				me.updateState(0);
			}

			if (me.getEnergy() == 0 && barCounter < 140 && barCounter > 70) {
				me.updateState(1);
			}

			if (me.getEnergy() == 0 && barCounter < 210 && barCounter >= 140) {

				me.updateState(2);
			}

			if (timeCounter % 210 == 0) {
				me.regenerate();
			}

			enableKeys();

			for (Player p : players) {
				p.act(obstacles, me);
			}

			if (players.size() > 0) {
				me.act(obstacles, players.get(0));
			} else {
				me.act(obstacles, null);
			}

			if (ball != null) {
				if (ball.isOnGround()) {
					ball.act(me, 300);
				} else if (me.hasBall()) {
					ball.act(me, 300);
				}
			}

			me.act(obstacles, null);

			if (!currentlySending && me.isDataChanged()) {
				currentlySending = true;
				myUserRef.setValue(me.getDataObject(), new CompletionListener() {

					@Override
					public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
						currentlySending = false;
					}

				});
			}

			if (ball != null) {
				if (ball.isDataChanged()) {
					currentlySending = true;
					myBallRef.setValue(ball.getDataObject(), new CompletionListener() {

						@Override
						public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
							currentlySending = false;
						}

					});
				}
			}

			repaint();

			long waitTime = 17 - (System.currentTimeMillis() - startTime);
			try {
				if (waitTime > 0)
					Thread.sleep(waitTime);
				else
					Thread.yield();
			} catch (InterruptedException e) {
			}
		}
	}

	public class KeyHandler implements KeyListener {

		private ArrayList<Integer> keys;

		/**
		 * Instantiates a KeyHandler which listens for key movements
		 */
		public KeyHandler() {
			keys = new ArrayList<Integer>();
		}

		/**
		 * Overrides method from KeyListener to determine when the key is pressed
		 * 
		 * @KeyEvent e the KeyEvent occurring
		 */
		public void keyPressed(KeyEvent e) {
			keys.add(e.getKeyCode());
		}

		/**
		 * Overrides method from KeyListener to determine when the key is released
		 * 
		 * @KeyEvent e the KeyEvent occurring
		 */
		public void keyReleased(KeyEvent e) {
			Integer code = e.getKeyCode();
			while (keys.contains(code))
				keys.remove(code);
		}

		public void keyTyped(KeyEvent e) {

		}

		/**
		 * Determines if a key is currently being pressed
		 * 
		 * @param code keyCode of key to check
		 * @return true or false if the key is pressed or not respectively
		 */
		public boolean isPressed(int code) {
			// System.out.println(keys);
			return keys.contains(code);
		}
	}

	class BallChangeListener implements ChildEventListener {
		@Override
		public void onCancelled(DatabaseError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			Ball b = new Ball(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 250, 20, 20, null, arg0.getKey());
			b.syncWithDataObject(arg0.getValue(BallData.class));
			ball = b;
		}

		@Override
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (!me.hasBall()) {
				ball.syncWithDataObject(arg0.getValue(BallData.class));
			}
		}

		@Override
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onChildRemoved(DataSnapshot arg0) {
			if (ball.idMatch(arg0.getKey()))
				return;
			for (int i = 0; i < balls.size(); i++) {
				if (balls.get(i).idMatch(arg0.getKey())) {
					balls.remove(i);
					break;
				}
			}
		}
	}

	class UserChangeListener implements ChildEventListener {

		@Override
		public void onCancelled(DatabaseError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (me.idMatch(arg0.getKey()))
				return;

			System.out.println(arg0);
			Player p = new Player(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 50, null, arg0.getKey(), false);
			p.syncWithDataObject(arg0.getValue(PlayerData.class));
			System.out.println(arg0.getValue(PlayerData.class));
			players.add(p);
		}

		@Override
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (me.idMatch(arg0.getKey()))
				return;

			for (int i = 0; i < players.size(); i++) {
				Player p = players.get(i);
				if (p.idMatch(arg0.getKey())) {
					p.syncWithDataObject(arg0.getValue(PlayerData.class));
				}
			}
		}

		@Override
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onChildRemoved(DataSnapshot arg0) {
			if (me.idMatch(arg0.getKey()))
				return;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).idMatch(arg0.getKey())) {
					players.remove(i);
					break;
				}
			}
		}

	}

}
