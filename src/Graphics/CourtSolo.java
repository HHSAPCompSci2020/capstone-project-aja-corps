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

import java.util.Timer;

import java.util.*;

import java.util.Queue;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents the solo court for the game
 * 
 * @author anirudhv
 * @author adityapanikkar
 * @author josh_choi
 *
 */
public class CourtSolo extends JPanel implements Runnable {
	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 322;

	private Rectangle screenRect;
	private int timeCounter;
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
	private JLabel instructions;
	private boolean waiting;
	private long joinTime;
	private int playerType;

	private Image won;
	private Image lost;

	private JButton quitButton;
	private JButton seeStats;
	private Referee referee;

	private boolean picked;
	private boolean holding;

	private long pickTime;
	private boolean forced;

	/**
	 * Instantiates a new court at the database reference in Firebase and with the
	 * name of the player
	 * 
	 * @param roomRef    Firebase database reference
	 * @param playerName Player username
	 */
	public CourtSolo(String playerName) {
		super();
		try {
			backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("img/court.jpg"));
			pauseImage = ImageIO.read(getClass().getClassLoader().getResource("img/PauseScreen.png"));
			jumpImage = ImageIO.read(getClass().getClassLoader().getResource("img/JumpPowerUp.png"));
			speedImage = ImageIO.read(getClass().getClassLoader().getResource("img/SpeedPowerUp.png"));
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

		referee = new Referee(383, 180);
		me = new Player(50, 288, playerName, "Testplayer", false, 0);
		spawnNewBall();

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

				

					scoreBoard = new PlayerStats(me.getShots(), me.getDashes(), me.getWalks(), me.getJumps(), me.getShotsMade(),
							startTime);
					String[] arr;
					arr = new String[3];

					arr = scoreBoard.statString();

					g.drawString(arr[0], 100, 220);
					g.drawString(arr[1], 250, 240);
					g.drawString(arr[2], 200, 260);
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

			if (me.getJumpPowerup()) {

				g.drawImage(jumpImage, (int) randX, 260, 35, 35, this);
			}

			if (me.getSpeedPowerup()) {

				g.drawImage(speedImage, (int) randX, 260, 35, 35, this);
			}

			if (!waiting && first) {
				Referee.blowWhistle();
				first = false;
			}
			referee.draw(g2, this);

			if (ball != null) {
				ball.draw(g2, this);
			}

			me.draw(g2, this, me);
			g2.drawString(Integer.toString(me.getScore()), 415, 68);

			if (picked) {
				double shotTime = (currentTime - pickTime);

				int realtime = (int) (21 - (shotTime / 1000));

				if (realtime <= 0) {
					realtime = 0;

					if (me.getEnergy() > 0 && ball != null && ball.getDribbling()) {
						timeCounter = 0;
						me.shoot();
						if (me.getDirection())
							ball.shoot(640, 140);
						else {
							ball.shoot(130, 140);
						}

					}
					shotCounter = -10;
				} else {

				}

				if (realtime >= 10) {
					g.drawString(Integer.toString(realtime), (int) me.getX() + 11, (int) (me.getY() - 60));
				} else {
					g.drawString(Integer.toString(realtime), (int) me.getX() + 16, (int) (me.getY() - 60));
				}

			}

			g2.setTransform(at);
		}

	}

	/**
	 * Spawns a new ball in the court
	 * 
	 * @post Instantiates the field ball
	 */
	public void spawnNewBall() {
		ball = new Ball(383, 288, 20, 20, "TestBall", "test");
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

				me.dash(ball, null);

				dashCounter = -10;
			}

			if (keyControl.isPressed(KeyEvent.VK_SPACE) && shotCounter > 0 && ball != null && ball.getDribbling()) {

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

			

			paused = !paused;
			iPaused = !iPaused;

			if (ball != null) {
				x = ball.getCenterX();
				y = ball.getCenterY();
			}

			pauseCounter = -10;
			stats = false;

		}

		if (keyControl.isPressed(KeyEvent.VK_S) && paused) {
			stats = true;
		}

		
	}

	/**
	 * Overide method from the Runnable class that allows the game to run over and
	 * over again in this method
	 * 
	 */
	public void run() {

		while (!quit) { // Modify this to allow quitting
			if (ball != null && !picked && ball.getDribbling()) {
				picked = true;
				pickTime = System.currentTimeMillis();
			}

			if (ball != null && ball.getDribbling()) {
				holding = true;
			} else {
				holding = false;
				picked = false;
			}

			startTime = System.currentTimeMillis();
			pauseCounter++;
			if (paused == false) {
				timeCounter++;
				barCounter++;
				powerCounter++;
				stopCounter++;
				dashCounter++;
				shotCounter++;

				if (me.getPower() == false) {
					stopCounter = 0;
				}
				
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

			enableKeys();


			if (ball != null) {
				ball.act(me, 300);
			}

			me.act(obstacles, null);

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

	/**
	 * 
	 * @author anirudhv
	 *
	 */
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
			return keys.contains(code);
		}
	}

}
