package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Referee extends MovingImage{

	public static final int MARIO_WIDTH = 40;
	public static final int MARIO_HEIGHT = 60;
	
	private double x, y;
	private static boolean blow;
	
	private int count = 0;
	
	private static String filename = "img/standing.png";
	
	public Referee(int x, int y) {
		super(filename, x, y, MARIO_WIDTH, MARIO_HEIGHT);
		this.x = x;
		this.y = y;
	}
	
	public static void blowWhistle() {
		blow = true;
	}
	
	public void draw(Graphics g, ImageObserver io) {
		
		Color c = g.getColor();
		
		if(blow && count < 100) {
//			System.out.println(count);
			count++;
			g.setColor(Color.orange);

			g.drawString("TWEET!", (int)x, (int)y);
			
			g.setColor(c);
			
			g.drawImage((new ImageIcon("img/blowing.png")).getImage(), (int)x, (int)y, (int) width, (int) height, io);
		} else if(blow && count >= 100) {
			blow = false;
			count = 0;
		}
		
		if(!blow){
			g.drawImage((new ImageIcon(filename)).getImage(), (int)x, (int)y, (int) width, (int) height, io);
		}

		g.setColor(c);
	}
	
	
}
