import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

//import gui.DrawingSurface.Cursor;

import java.util.*;

import networking.frontend.NetworkDataObject;
import networking.frontend.NetworkListener;
import networking.frontend.NetworkMessenger;

import java.util.Queue;
import java.io.Serializable;

public class GamePanel extends JPanel implements Runnable, NetworkListener {
	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 322;

	private Rectangle screenRect;

	// private Mario mario;
	private Ball ball;
	private Image backgroundImage;
	private ArrayList<Shape> obstacles;
	private NetworkMessenger nm;

	private KeyHandler keyControl;
	private ArrayList<Player> entities = new ArrayList<>();

	public GamePanel() {
		super();

		try {
			backgroundImage = ImageIO.read(new File("./2CA52D8.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// JFrame window = new JFrame("Peer Chat");
		// window.setBounds(300, 300, 800, 600);
		// window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// window.add(this);
		// window.setVisible(true);

		keyControl = new KeyHandler();
		setBackground(Color.CYAN);
		screenRect = new Rectangle(0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		obstacles.add(new Rectangle(0, 300, 800, 22));
		// spawnNewMario("me!");
		// spawnNewBall();
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

		if (entities.size() > 0) {
			for (Player e : entities) {
				e.draw(g2, this);
			}
		}

		// entities.get(0).draw(g2, this);
		// mario.draw(g2, this);
		// ball.draw(g2, this);

		g2.setTransform(at);

		// TODO Add any custom drawings here
	}

	public void spawnNewMario(String host) {
		Player mario = new Player(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 50, host);
		System.out.println(host);
		entities.add(mario);
	}

	public void spawnNewBall() {
		ball = new Ball(DRAWING_WIDTH / 2 - Player.MARIO_WIDTH / 2, 250);
	}

	public KeyHandler getKeyHandler() {
		return keyControl;
	}

	public void enableKeys() {
		if (keyControl.isPressed(KeyEvent.VK_LEFT)) {
			if (nm != null) {
				nm.sendMessage(NetworkDataObject.MESSAGE, "hello");
			}
			((Player) entities.get(0)).walk(-1);
		}

		if (keyControl.isPressed(KeyEvent.VK_RIGHT))
			((Player) entities.get(0)).walk(-1);
		if (keyControl.isPressed(KeyEvent.VK_UP))
			((Player) entities.get(0)).walk(-1);
	}

	public void run() {
		while (true) { // Modify this to allow quitting
			long startTime = System.currentTimeMillis();

			// nm.sendMessage(NetworkDataObject.MESSAGE, "hello world");

			enableKeys();

			if (entities.size() > 0) {
				((Player) entities.get(0)).act(obstacles);
			}

			// ball.act(obstacles);

			// if (!screenRect.intersects((Mario) entities.get(0)))
			// spawnNewMario();

			processNetworkMessages();
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

	@Override
	public void connectedToServer(NetworkMessenger nm) {
		this.nm = nm;
	}

	public void processNetworkMessages() {

		if (nm == null)
			return;

		Queue<NetworkDataObject> queue = nm.getQueuedMessages();

		while (!queue.isEmpty()) {
			NetworkDataObject ndo = queue.poll();
			System.out.println(ndo.messageType);

			String host = ndo.getSourceIP();

			if (ndo.messageType.equals(NetworkDataObject.MESSAGE)) {
				// System.out.println(host);
			} else if (ndo.messageType.equals(NetworkDataObject.CLIENT_LIST)) {
				for (Player c : entities) {
					// System.out.println(c.host);
					if (c.host.equals(host))
						return;
				}
				// System.out.println(host);

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
			return keys.contains(code);
		}
	}

	@Override
	public void networkMessageReceived(NetworkDataObject ndo) {
		// TODO Auto-generated method stub

	}

}
