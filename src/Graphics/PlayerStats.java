package Graphics;

import java.awt.Graphics;

public class PlayerStats {
	public static int score1;
	public static int score2;
	private int shots;
	private int dashes;
	private int walknum;
	private int jumpcount;
	private int score;


	public PlayerStats(){
		
	}
	
	
	public PlayerStats(int shots, int dashes, int walknum,  int jumpcount, int score) {

		this.shots = shots;
		this.dashes = dashes;
		this.walknum = walknum;
		this.jumpcount = jumpcount;
		this.score = score;
		
	}
	
	public String[] statString() {
		
		
		
		double distance = Math.round(walknum * 0.7 + dashes *1.4);
		double percent;
		int scorecount;
		if(score == 1) {
			scorecount = score1;
		}else {
			scorecount = score2;
		}
		
		if(shots ==0) {
			 percent = 0;
		}else {
		
		 percent = Math.round( (100.0)*scorecount/shots);
		}
		int energyUse = dashes+shots;
		
		
		
		
		
		String ret = "You took "+shots+" shot(s) and made "+scorecount+" shot(s) which means that your shooting percentage is "+percent+" percent ";
		String ret1 = " You jumped "+jumpcount + " times and you dashed " +dashes + " times" ;
		String ret2 = "You covered a total of "+distance + " meters and you used a total of "+energyUse +" energy";
		
		String [] arr;
		arr = new String [3];
		
		arr[0] = ret;
		arr[1] = ret1;
		arr[2] = ret2;
		
		return arr;
 		
		 
		
		
	}

	public void draw(Graphics g)  {
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
