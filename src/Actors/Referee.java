package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 
 * @author josh_choi
 *
 */
public class Referee extends MovingImage {

	public static final int MARIO_WIDTH = 40;
	public static final int MARIO_HEIGHT = 60;

	private double x, y;
	private static boolean blow;

	private int count = 0;

	private static String filename = "img/standing.png";

	/**
	 * 
	 * @param x x coord of where the ref will spawn
	 * @param y y coord of where the ref will spawn
	 */
	public Referee(int x, int y) {
		super(filename, x, y, MARIO_WIDTH, MARIO_HEIGHT);
		this.x = x;
		this.y = y;
	}

	/**
	 * Blows the whistle for the referee
	 * 
	 */
	public static void blowWhistle() {
		blow = true;
	}

	/**
	 * Draws the referee
	 * 
	 * @param g  Graphics needed to draw to screen
	 * @param io Image observer needed to draw image
	 */
	public void draw(Graphics g, ImageObserver io) {

		Color c = g.getColor();

		if (blow && count < 100) {
//			System.out.println(count);
			count++;
			g.setColor(Color.orange);

			g.drawString("TWEET!", (int) x, (int) y);

			g.setColor(c);

			try {
				g.drawImage(ImageIO.read(getClass().getClassLoader().getResource("img/blowing.png")), (int) x, (int) y,
						(int) width, (int) height, io);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (blow && count >= 100) {
			blow = false;
			count = 0;
		}

		if (!blow) {
			try {
				g.drawImage(ImageIO.read(getClass().getClassLoader().getResource("img/standing.png")), (int) x, (int) y,
						(int) width, (int) height, io);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		g.setColor(c);
	}

}
