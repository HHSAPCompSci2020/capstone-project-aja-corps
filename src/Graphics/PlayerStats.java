package Graphics;

import java.awt.Graphics;

public class PlayerStats {
	public static int score1;
	public static int score2;

	public PlayerStats() {

	}

	public void draw(Graphics g) {
		if(score1 >9) {
			g.drawString(Integer.toString(score1), 366, 68);
			
		}else {
			g.drawString(Integer.toString(score1), 370, 68);
		}
		
		if(score2>9) {
			g.drawString(Integer.toString(score2), 411, 68);
			
		}else {
			g.drawString(Integer.toString(score2), 415, 68);
		}
		
		
		
	}

}
