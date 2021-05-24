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
import Actors.Player;
import Data.BallData;
import Data.PlayerData;

import java.util.*;

import java.util.Queue;
import java.io.Serializable;

/**
 * Represents the court for the game
 * 
 * @author anirudhv
 *
 */
public class CourtTutorial extends JPanel implements Runnable {
	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 322;

	private int timeCounter;
	private int powerCounter;
	private int stopCounter;
	private int pauseCounter;
	private int dashCounter;
	private int shotCounter;
	private int barCounter;
	private boolean paused = false;
	private boolean stats = false;
	private boolean moveLeftAndRight;
	private boolean shot;
	private boolean dashed;
	private JLabel instructions;
	private boolean powerUp;
	private boolean quit = true;
	private boolean ePressed;
	private double randX;
	private boolean spawned = false;
	private boolean finished = false;
	private boolean taken;
	
	private int score;
	private boolean chose;
	private Image backgroundImage;
	private Image pauseImage;
	private Image jumpImage;
	private Image speedImage;
	private ArrayList<Shape> obstacles;

	private KeyHandler keyControl;

	protected Player me;
	protected Ball ball;
	private ArrayList<Player> players;
	private PlayerStats scoreBoard;

	/**
	 * Instantiates a new court at the database reference in Firebase and with the
	 * name of the player
	 * 
	 * @param roomRef    Firebase database reference
	 * @param playerName Player username
	 */
	public CourtTutorial(String playerName) {
		super();
		instructions = new JLabel(
				"Welcome to Basketball All Stars! Use the left and right arrow keys to move left and right and the up arrow key to jump.");
//
		add(instructions);

		try {
			backgroundImage = ImageIO.read(new File("img/court.jpg"));
			pauseImage = ImageIO.read(new File("img/PauseScreen.png"));
			jumpImage = ImageIO.read(new File("img/JumpPowerUp.png"));
			speedImage = ImageIO.read(new File("img/SpeedPowerUp.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		keyControl = new KeyHandler();
		setBackground(Color.CYAN);
		obstacles = new ArrayList<Shape>();
		obstacles.add(new Rectangle(0, 300, 800, 22));

		players = new ArrayList<Player>();

//		me = new Player(200, 50, playerName, "Test", false);
		spawnNewPlayer(playerName);
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
			g.drawString(" Press ESC to return to the game and E to exit", 260, 160);
		//	g.drawString(" Press S to see your statistics", 285, 180);
			
			if(ePressed) {
			g.drawString(" Thanks for playing the tutorial! Now that you know how to play the game, you can relaunch the game and begin online play!", 5, 200);
			}

			if (stats) {

				g.drawString(
						" type 1 if you are shooting on the left hoop, and type 2 if you are shooting on the right hoop",
						104, 200);

				if (chose) {

					scoreBoard = new PlayerStats(me.getShots(), me.getDashes(), me.getWalks(), me.getJumps(), score, 0);
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

			int width = getWidth();
			int height = getHeight();

			double ratioX = (double) width / DRAWING_WIDTH;
			double ratioY = (double) height / DRAWING_HEIGHT;

			AffineTransform at = g2.getTransform();
			g2.scale(ratioX, ratioY);

			g.setColor(new Color(205, 102, 29));
//			g.fill();
			for (Shape s : obstacles) {
				g2.fill(s);
			}

			g.drawImage(backgroundImage, 0, 0, this);

//			g.drawRect(130, 140, 20, 20); // ball class needs this for debugging, KEEP THIS IN
//			g.drawRect(640, 140, 20, 20);
//			g.drawLine(0, 50, width, 50);
			for (int i = 0; i < players.size(); i++) {
				players.get(i).draw(g2, this);
			}

			// for (int i = 0; i < balls.size(); i++) {
			// balls.get(i).draw(g2, this);
			// }

			if (ball != null) {
				ball.draw(g2, this);
			}
			
			if(me.getJumpPowerup()) {
			
				
				g.drawImage(jumpImage,(int)randX, 260, 35, 35, this);
			}
			
			
			if(me.getSpeedPowerup()) {
				
					
					g.drawImage(speedImage,(int)randX, 260, 35, 35, this);
			}

			me.draw(g2, this, me);
			scoreBoard.draw(g2);
			g2.setTransform(at);
			g2.setColor(new Color(255, 255, 255));
			g2.fillRect(0, 0, 800, 30);

			// TODO Add any custom drawings here

		}

	}

	/**
	 * Spawns a new ball in the court
	 */
	public void spawnNewBall() {
		ball = new Ball(300, 288, 20, 20, "TestBall", "TestBall");
//		myBallRef.setValueAsync(ball.getDataObject());
	}
	
	public void spawnNewPlayer(String playerName) {
		me = new Player(50, 288, playerName, "test", false);
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

			me.setShooting(false);
			if (keyControl.isPressed(KeyEvent.VK_LEFT)) {
				moveLeftAndRight = true;
				me.setDirection(false);
				me.walk(-1);
			}

			if (keyControl.isPressed(KeyEvent.VK_RIGHT)) {
				moveLeftAndRight = true;
				me.setDirection(true);
				me.walk(1);
			}

			if (keyControl.isPressed(KeyEvent.VK_UP) && barCounter > 25) {
				me.jump();
				barCounter = 5;
			}

			if (keyControl.isPressed(KeyEvent.VK_SHIFT) && dashCounter > 0) {
				dashed = true;
				if (me.getEnergy() > 0) {
					timeCounter = 0;
				}

				me.dash(ball, null);
				dashCounter = -10;
			}

			if (keyControl.isPressed(KeyEvent.VK_SPACE) && shotCounter > 0 && ball.getDribbling()) {

				shot = true;

				if (me.getEnergy() > 0) {
					timeCounter = 0;
					me.shoot();
					if (me.getDirection())
						ball.shoot(640, 140);
					else
						ball.shoot(130, 140);

				}
				shotCounter = -10;
			}

			if (keyControl.isPressed(KeyEvent.VK_ENTER)) {
				spawnNewBall();
			}
		}

		if (keyControl.isPressed(KeyEvent.VK_ESCAPE) && pauseCounter > 0) {
			paused = !paused;
			pauseCounter = -10;
			stats = false;
		}

		if (keyControl.isPressed(KeyEvent.VK_S) && paused) {
	//		stats = true;
		}
		
		if (keyControl.isPressed(KeyEvent.VK_E) && paused) {
		//	stats = true;
		}

		if (keyControl.isPressed(KeyEvent.VK_1) && stats) {
			score = 1;
			chose = true;
		}

		if (keyControl.isPressed(KeyEvent.VK_2) && stats) {
			score = 2;
			chose = true;
		}
		
		if (keyControl.isPressed(KeyEvent.VK_E) && paused) {
			ePressed = true;
			quit =false;
		}
		
		
	}

	/**
	 * Overide method from the Runnable class that allows the game to run over and
	 * over again in this method
	 * 
	 */
	public void run() {

		while (quit) {
			// Modify this to allow quitting
			
			long startTime = System.currentTimeMillis();
			pauseCounter++;
			if (moveLeftAndRight) {
				if (me.hasBall()) {
					instructions.setText(
							"To shoot the ball, press the space bar. Shooting takes up 1 energy and the energy bar is on top of you.");
					if (shot) {
						instructions.setText("Awesome! Now try dashing - press shift. Dashing also takes up 1 energy.");
					}
					if (shot && dashed) {
//						powerUp = true;
						instructions.setText(
								"Power ups randomly spawn across the court. Red powerups are jump boosts, and Blue powerups are speed boosts.");
						if (!me.getPower() && !spawned) {
							randX = me.getPowerLoc();
							me.spawnPowerup();
							
							spawned = true;
						}
					}
					if (shot && dashed && me.getPower()) {
						instructions.setText("Press escape to pause and exit the game. You are ready to play online!");
					}
				} else if (shot) {
//					instructions.setText("Awesome! Now try dashing - press shift. Dashing also takes up 1 energy.");
					if (shot && dashed) {
//						powerUp = true;
						instructions.setText(
								"Power ups randomly spawn across the court. Blue powerups are jump boosts, and red powerups are speed boosts.");
						if (!me.getPower() && !spawned) {
							randX = me.getPowerLoc();
							me.spawnPowerup();
							spawned = true;
						}
					} else if (shot && dashed && me.getPower()) {
						instructions.setText("Press escape to pause and exit the game. You are ready to play online!");
					} else {
						instructions.setText("Awesome! Now try dashing - press shift. Dashing also takes up 1 energy.");
					}
				} else {
					instructions.setText("Great! Now go pick up the ball!");
				}
				
				
				

				if (shot && dashed && powerCounter>=250) {
					finished = true;
					
			}
				
				if(finished) {
					instructions.setText("Press escape to pause and exit the game. You are ready to play online!");
				}
			}

			if (paused == false) {
				// System.out.println(me.hasBall());
				timeCounter++;
				barCounter++;
				
				if(me.getPower()) {
				powerCounter++;
			}
				stopCounter++;
				dashCounter++;
				shotCounter++;

				if (me.getPower() == false) {
					stopCounter = 0;
				}

//				if (barCounter == 1) {
//					me.spawnPowerup();
//				}
//
//				if (powerCounter % 1500 == 0) {
//					me.spawnPowerup();
//				}

				if (stopCounter == 300) {
					me.powerOff();
				}

				if (barCounter == 1) {
					spawnNewBall();
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
//				if (players.size() > 0) {
//					ball.block(me, players.get(0), 300);
//				}
//
//				if (me.hasBall() || me.isShooting() || ball.isOnGround()) {
//					ball.act(me, 300);
//				}
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

}
