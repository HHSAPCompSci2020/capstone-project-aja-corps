package Graphics;

import java.awt.Graphics;
/**
 * This class tracks the statistics of the player, and creates a String containing all the info
 * @author adityapanikkar
 * 
 * 
 */

public class PlayerStats {
	public static int score1;
	public static int score2;
	private int shots;
	private int dashes;
	private int walknum;
	private int jumpcount;
	private int score;
	private long timePassed;

	/**
	 * default constructor
	 * 
	 * 
	 */
	public PlayerStats(){
		
	}
	
	/**
	 * 
	 * @param shots number of shots which have been taken
	 * @param dashes number of times the player has dashes
	 * @param walknum number of times the player walked( took a step )
	 * @param jumpcount number of times the player jumped
	 * @param score if score is 1, left hoop is the scoring hoop, if score =2 right hoop is the soring hoop
	 */
	 
	public PlayerStats(int shots, int dashes, int walknum,  int jumpcount, int score, long timePassed) {

		this.shots = shots;
		this.dashes = dashes;
		this.walknum = walknum;
		this.jumpcount = jumpcount;
		this.score = score;
		this.timePassed = timePassed;
		
	}
	/**
	 * Creates a String which contains all the statistics. Since it returns an array, the stats could be printed in multiple orders
	 * @return returns an array of  Strings which includes the following categories: shots taken, shots made, shooting percentage, number of times jumped, number of times dashed, distance covered, and energy used
	 */
	
	public String[] statString() {
		
		
		
		double distance = Math.round(walknum * 0.7 + dashes *1.4)/12;
		double percent;
		int scorecount;
		
		scorecount = score;
		
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

	/**
	 * 
	 * @param g the score is drawn on g
	 * draws the live score in the scoreboard on the court
	 */
	 
	 
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
