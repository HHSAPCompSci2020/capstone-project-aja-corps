import processing.core.PApplet;

public class Person extends PApplet {
	private int x;
	private int y;
	private float x1;
	private float x2;
	private float y1;
	private float y2;
	
	
	
	public Person() {
		x = 100;
		y = 450;
		
		
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getx() {
		int xval = this.x;
		return xval;
	}
	
	public int gety() {
		int yval = this.y;
		return yval;
	}
	
	public void draw(PApplet surface) {

		surface.fill(255,255,255);
		surface.circle(this.x, this.y, 50);
		surface.line(this.x, this.y+25, this.x, this.y+125);
		surface.line(this.x, this.y+125, this.x-25, this.y+150);
		surface.line(this.x, this.y+125, this.x+25, this.y+150);
		surface.line(this.x-25, this.y+55, this.x+25, this.y+55);
		x1 = this.x;
		y1 = this.y+25;
		x2 = this.x;
		y2 = this.y+125;
	}
	
	public float getx1() {
		return x1;
	}
	public float gety1() {
		return y1;
	}
	public float getx2() {
		return x2;
	}
	public float gety2() {
		return y2;
	}
}
