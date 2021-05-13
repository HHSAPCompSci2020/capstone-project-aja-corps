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

import Actors.OldBall;
import Actors.Ball;
import Actors.BallData;
import Actors.Player;
import Actors.PlayerData;

import java.util.*;

import java.util.Queue;
import java.io.Serializable;

public class GamePanel extends JPanel implements Runnable {
	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 322;

	private Rectangle screenRect;

	// private Ball ball;
	private Image backgroundImage;
	private ArrayList<Shape> obstacles;

	private KeyHandler keyControl;
	private ArrayList<Player> entities = new ArrayList<>();

	private Player me;
	private Ball ball;
	private ArrayList<Player> players;
	private ArrayList<Ball> balls;

	// Database stuff
	private DatabaseReference roomRef; // This is the database entry for the whole room
	private DatabaseReference myUserRef; // This is the database entry for just our user's data. This allows us to more
											// easily update ourselves.
	private DatabaseReference myBallRef;

	private boolean currentlySending; // These field allows us to limit database writes by only sending data once
										// we've received confirmation the previous data went through.

	public GamePanel(DatabaseReference roomRef) {
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

//		DatabaseReference x = roomRef.child("users");

		myUserRef = roomRef.child("users").push();
		myBallRef = roomRef.child("balls").push();

		players = new ArrayList<Player>();
		balls = new ArrayList<Ball>();

		me = new Player(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 50, "TestPlayer", myUserRef.getKey(), false);
//		ball = new Ball(300, 288, 20, 20, "TestBall", myBallRef.getKey());

		myUserRef.setValueAsync(me.getDataObject());
//		myBallRef.setValueAsync(ball.getDataObject());

		System.out.println(roomRef.child("users"));

		new Thread(this).start();
	}

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

		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).draw(g2, this);
		}

		if (ball != null) {
			ball.draw(g2, this);
		}
		me.draw(g2, this);
		g2.setTransform(at);

		// TODO Add any custom drawings here
	}

	public void spawnNewPlayer() {

	}

	public void spawnNewBall() {
		ball = new Ball(300, 288, 20, 20, "TestBall", myBallRef.getKey());
		myBallRef.setValueAsync(ball.getDataObject());
	}

	public KeyHandler getKeyHandler() {
		return keyControl;
	}

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
			me.dash();
		}

		if (keyControl.isPressed(KeyEvent.VK_SPACE)) {
			ball.shoot(640, 140);
			me.shoot();
		}

		if (keyControl.isPressed(KeyEvent.VK_ENTER)) {
			spawnNewBall();
		}
	}

	public void run() {
		while (true) { // Modify this to allow quitting
			long startTime = System.currentTimeMillis();

			enableKeys();

			if (ball != null) {
				ball.getPlayer(me);
				ball.dribble(300);
			}

			for (Player p : players) {
				p.act(obstacles, me);
			}

			if (players.size() > 0) {
				me.act(obstacles, players.get(0));
			} else {
				me.act(obstacles, null);
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

		public KeyHandler() {
			keys = new ArrayList<Integer>();
		}

		public void keyPressed(KeyEvent e) {
			keys.add(e.getKeyCode());
		}

		public void keyReleased(KeyEvent e) {
			Integer code = e.getKeyCode();
			while (keys.contains(code))
				keys.remove(code);
		}

		public void keyTyped(KeyEvent e) {

		}

		public boolean isPressed(int code) {
//			System.out.println(keys);
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
//			if (ball.idMatch(arg0.getKey())) {
//				System.out.println("My ball!");
//				return;
//			}

//			System.out.println("Ball added!");
			Ball b = new Ball(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 250, 20, 20, null, arg0.getKey());
			b.syncWithDataObject(arg0.getValue(BallData.class));
			ball = b;
//			balls.add(b);
		}

		@Override
		public void onChildChanged(DataSnapshot arg0, String arg1) {
//			if (me.idMatch(arg0.getKey()))
//				return;

//			for (int i = 0; i < balls.size(); i++) {
//				Ball b = balls.get(i);
//				if (b.idMatch(arg0.getKey())) {
//					b.syncWithDataObject(arg0.getValue(BallData.class));
//				} 
//			}

			if (!me.hasBall()) {
//				if (ball.idMatch(arg0.getKey())) {
//					return;
//				}

				ball.syncWithDataObject(arg0.getValue(BallData.class));
			} 
//			else if (me.hasBall()) {
//				System.out.println(me.getID() + " has the ball!");
////				if (ball.idMatch(arg0.getKey())) {
////					ball.syncWithDataObject(arg0.getValue(BallData.class));
////				}
//			}

//			Ball b = new Ball(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 250, 20, 20, null, arg0.getKey());
//			ball = b;
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
//			System.out.println(me.getX() + ", " + me.getY());
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
