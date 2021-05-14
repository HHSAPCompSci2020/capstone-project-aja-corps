package Graphics;

import java.awt.Graphics;

public class Scoreboard {
	public static int score1;
	public static int score2;

	public Scoreboard() {

	}

	public void draw(Graphics g) {
		g.drawString(Integer.toString(score1), 50, 100);
		g.drawString(Integer.toString(score2), 750, 100);
	}

}
