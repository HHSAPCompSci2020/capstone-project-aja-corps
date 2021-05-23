package Graphics;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import Actors.Ball;
import Actors.Player;
import Actors.Referee;
import Data.BallData;
import Data.PlayerData;
import Data.SoundEffect;

import java.util.Timer;

import java.util.*;

import java.util.Queue;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

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
	// private int barCounter;
	private int powerCounter;
	private int stopCounter;
	private int pauseCounter;
	private int dashCounter;
	private int shotCounter;
	private int barCounter;
	private boolean paused = false;
	private boolean stats = false;
	private boolean iPaused = false;
	private int timesPaused = 0;
	private int pauseTime;
	private Timer clock;
	protected long startTime;

	private double x = 450;
	private double y = 260;

	private double randX;
	
	private boolean first = true;
	
	private long initialTime;
	private long currentTime;
	private int minutes;
	private int seconds;

	private boolean quit = false;

	private int score;
	private boolean chose;
	// private Ball ball;
	private Image backgroundImage;
	private Image pauseImage;
	private Image jumpImage;
	private Image speedImage;
	private ArrayList<Shape> obstacles;

	private KeyHandler keyControl;
	private ArrayList<Player> entities = new ArrayList<>();

	protected Player me;
	protected Ball ball;
	private ArrayList<Player> players;
	private ArrayList<Ball> balls;
	private PlayerStats scoreBoard;
	private SoundEffect data;
	private JLabel instructions;
	private boolean waiting;
	private long joinTime;
	private int playerType;
	
	private Image won;
	private Image lost;
	
	private JButton quitButton;
	private JButton seeStats;
	private Referee referee;
//	private boolean gameStart;
	
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
	public Court(DatabaseReference roomRef, String playerName, int playerType) {
		super();
//		data = new SoundEffect();

		try {
			backgroundImage = ImageIO.read(new File("img/court.jpg"));
			pauseImage = ImageIO.read(new File("img/PauseScreen.png"));
			jumpImage = ImageIO.read(new File("img/JumpPowerUp.png"));
			speedImage = ImageIO.read(new File("img/SpeedPowerUp.png"));
			won = ImageIO.read(new File("img/Won.png"));
			lost = ImageIO.read(new File("img/Lost.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		clock = new Timer();
		keyControl = new KeyHandler();
		setBackground(Color.CYAN);
		screenRect = new Rectangle(0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		obstacles.add(new Rectangle(0, 300, 800, 22));
		initialTime = System.currentTimeMillis();
		this.roomRef = roomRef;
		currentlySending = false;

		roomRef.child("users").addChildEventListener(new UserChangeListener());
		roomRef.child("balls").addChildEventListener(new BallChangeListener());

		// DatabaseReference x = roomRef.child("users");

		myUserRef = roomRef.child("users").push();
		myBallRef = roomRef.child("balls").push();

		players = new ArrayList<Player>();
		balls = new ArrayList<Ball>();
		
		referee = new Referee(383, 180);

		if (playerType == 1) {
			me = new Player(300, 288, playerName, myUserRef.getKey(), false);
			waiting = true;
			instructions = new JLabel("Waiting for players...");
			add(instructions);
		} else {
			me = new Player(500, 288, playerName, myUserRef.getKey(), false);
			joinTime = System.currentTimeMillis();
			me.setDirection(false);
			spawnNewBall();
		}
		// ball = new Ball(300, 288, 20, 20, "TestBall", myBallRef.getKey());

		myUserRef.setValueAsync(me.getDataObject());
		// myBallRef.setValueAsync(ball.getDataObject());

		System.out.println(roomRef.child("users"));
		this.playerType = playerType;

		new Thread(this).start();
	}

	/**
	 * Overridden method in order to draw necessary components of the court (player,
	 * ball, etc)
	 * 
	 * @param Graphics g
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		if (paused) {
			g.drawImage(pauseImage, 0, 0, this);
			g.setColor(Color.white);
			g.drawString(" Click esc to return to the game", 300, 160);
			g.drawString(" Click s to return to see your statistics", 285, 180);

			if (!iPaused)
				g.drawString("Your Opponent Paused the Game", 305, 60);

			if (stats) {

				g.drawString(
						" Press 1 if you are shooting on the left hoop, and press 2 if you are shooting on the right hoop",
						104, 200);

				if (chose) {

					scoreBoard = new PlayerStats(me.getShots(), me.getDashes(), me.getWalks(), me.getJumps(), score,
							startTime);
					String[] arr;
					arr = new String[3];

					arr = scoreBoard.statString();

					g.drawString(arr[0], 100, 220);
					g.drawString(arr[1], 250, 240);
					g.drawString(arr[2], 200, 260);
				}
			}

		}

		if (!paused) {

			currentTime = System.currentTimeMillis();
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
			
		
			if(me.getJumpPowerup()) {
			
				
				g.drawImage(jumpImage,(int)randX, 260, 35, 35, this);
			}
			
			
			if(me.getSpeedPowerup()) {
				
					
					g.drawImage(speedImage,(int)randX, 260, 35, 35, this);
			}
			
		//	670 ;70

			

			

			

			

//			g.drawRect(130, 140, 20, 20); // ball class needs this for debugging, KEEP THIS IN
//			g.drawRect(640, 140, 20, 20);
//			g.drawLine(0, 50, width, 50);
			
			if(!waiting && first) {
				Referee.blowWhistle();
				first = false;
			}
			referee.draw(g2, this);
			
			for (int i = 0; i < players.size(); i++) {
				players.get(i).draw(g2, this, me);
			}

			// for (int i = 0; i < balls.size(); i++) {
			// balls.get(i).draw(g2, this);
			// }

			if (ball != null) {
				ball.draw(g2, this);
			}

			me.draw(g2, this, me);
			if (playerType == 1) {
				g2.drawString(Integer.toString(me.getScore()), 415, 68);
				for (int i = 0; i < players.size(); i++) {
					g.drawString(Integer.toString(players.get(i).getScore()), 370, 68);
				}
			} else {
				g2.drawString(Integer.toString(me.getScore()), 370, 68);
				for (int i = 0; i < players.size(); i++) {
					g.drawString(Integer.toString(players.get(i).getScore()), 415, 68);
				}
			}
			
			if (waiting) {
				g2.setColor(new Color(255, 255, 255));
				g2.fillRect(0, 0, 800, 30);
				// scoreBoard.draw(g2);
			}
			
			if (!waiting) {
				seconds = (int) (currentTime - joinTime) / 1000 - (59 * minutes);

				if (seconds >= 59 && timeCounter % 20 == 0) {
					minutes++;
					seconds = 0;
				}

				if (seconds >= 50) {
					g.setColor(Color.white);
					g.drawString(1-minutes + " : " + "0" + (59-seconds), 379, 30);
				} else {
					g.setColor(Color.white);
					g.drawString(1-minutes + " : " + (59-seconds), 379, 30);
				}
				if (minutes == 2) {
//					g.drawString(" Time is up, game over", 325, 258);
					if (me.getScore() > players.get(0).getScore()) {
						g.drawImage(won, 0, 0, this);
						quitButton = new JButton("Quit");
					    quitButton.setBounds(370, 200, 60, 30);
						add(quitButton);
					} else {
						g.drawImage(lost, 0, 0, this);
						quitButton = new JButton("Quit");
					    quitButton.setBounds(370, 200, 60, 30);
						add(quitButton);
					}
					// paused = true;
					quit = true;
				}
			}
			g2.setTransform(at);


			// TODO Add any custom drawings here

		}

	}

	/**
	 * Spawns a new ball in the court
	 */
	public void spawnNewBall() {
		ball = new Ball(383, 288, 20, 20, "TestBall", myBallRef.getKey());
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

		if (paused == false) {

			// me.setShooting(false);
			if (keyControl.isPressed(KeyEvent.VK_LEFT)) {
				me.setDirection(false);
				me.walk(-1);
			}

			if (keyControl.isPressed(KeyEvent.VK_RIGHT)) {
				me.setDirection(true);
				me.walk(1);
			}

			if (keyControl.isPressed(KeyEvent.VK_UP) && barCounter > 35) {
				me.jump();
				barCounter = 5;

			}

			if (keyControl.isPressed(KeyEvent.VK_SHIFT) && dashCounter > 0) {
				if (me.getEnergy() > 0) {
					timeCounter = 0;
				}

				if (players.size() > 0) {
					me.dash(ball, players.get(0));
				} else {
					me.dash(ball, null);
				}
				dashCounter = -10;
			}

			if (keyControl.isPressed(KeyEvent.VK_SPACE) && shotCounter > 0 && ball!= null &&ball.getDribbling()) {

				if (me.getEnergy() > 0) {
					timeCounter = 0;
					me.shoot();
					if (me.getDirection())
						ball.shoot(640, 140);
					else {
						ball.shoot(130, 140);
					}

				}
				shotCounter = -10;
			}

			if (keyControl.isPressed(KeyEvent.VK_ENTER)) {
				Referee.blowWhistle();
				spawnNewBall();

			}
		}

		if (keyControl.isPressed(KeyEvent.VK_ESCAPE) && pauseCounter > 0 && timesPaused < 6
				&& (ball == null || ball.getDribbling())) {
			timesPaused++;
			pauseTime = 0;

			if (paused && iPaused == false) {
				return;
			}

			paused = !paused;
			iPaused = !iPaused;

			if (ball != null) {
				x = ball.getCenterX();
				y = ball.getCenterY();
			}

			if (iPaused) {

				ball = null;
			}

			if (!iPaused && ball == null) {
				ball = new Ball((int) x, (int) y, 20, 20, "TestBall", myBallRef.getKey());
			}
			pauseCounter = -10;
			stats = false;

		}

		if (keyControl.isPressed(KeyEvent.VK_S) && paused) {
			stats = true;
		}

		if (keyControl.isPressed(KeyEvent.VK_1) && stats) {
			score = 1;
			chose = true;
		}

		if (keyControl.isPressed(KeyEvent.VK_2) && stats) {
			score = 2;
			chose = true;
		}
	}

	/**
	 * Overide method from the Runnable class that allows the game to run over and
	 * over again in this method
	 * 
	 */
	public void run() {

		while (!quit) { // Modify this to allow quitting
//			data.playSound();
//			SoundEffect audioPlayer;
//			try {
//				audioPlayer = new SoundEffect("./sounds/swish.wav");
//				audioPlayer.play();
//			} catch (UnsupportedAudioFileException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (LineUnavailableException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

			 
			startTime = System.currentTimeMillis();
			pauseCounter++;
			if (paused == false) {
				// System.out.println(me.hasBall());
				timeCounter++;
				barCounter++;
				powerCounter++;
				stopCounter++;
				dashCounter++;
				shotCounter++;

				// if (barCounter == 1) {
				// spawnNewBall();
				// }

				if (me.getPower() == false) {
					stopCounter = 0;
				}
				//
				if (barCounter == 1) {
	//				randX = me.getPowerLoc();
	//				me.spawnPowerup();
				}
				//
				if (powerCounter % 1200 == 0) {
					randX = me.getPowerLoc();
					me.spawnPowerup();
					//
				}

				if (stopCounter == 300) {
					me.powerOff();
				}

				if (me.getEnergy() == 0) {
					me.updateState(0);
				}

				if (me.getEnergy() == 1) {
					me.updateState(1);
				}

				if (me.getEnergy() == 2) {
					me.updateState(2);
				}

				if (timeCounter % 180 == 0) {
					me.regenerate();
				}
			}

			pauseTime++;

			if (ball != null && paused == true) {
				paused = false;
			}

			if (pauseTime > 750 && iPaused) {
				pauseTime = 0;
				timesPaused++;
				paused = false;
				iPaused = false;
				ball = new Ball((int) x, (int) y, 20, 20, "TestBall", myBallRef.getKey());
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
				if (players.size() > 0) {
					ball.block(me, players.get(0), 300);
				}

				if (me.hasBall() || me.isShooting() || ball.isOnGround()) {
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

			scoreBoard = new PlayerStats();

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

	public void removeInstructions() {
		waiting = false;
		joinTime = System.currentTimeMillis();
		if (instructions != null) {
			this.remove(instructions);
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
//			removeInstructions();
			players.add(p);
			removeInstructions();
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
