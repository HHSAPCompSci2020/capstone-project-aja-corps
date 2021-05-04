import processing.core.PApplet;

public class House {

	private int x,y;
	private float xcord;
	private float ycord;
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	private float parallelcheck;
	boolean dooropener;
	// Add more fields to variable-ize house

	public House() {
		xcord = 350;
		ycord = 200;
		x = 300;
		y = 400;
		dooropener = true;
		
		
		
	}

	public void move(int x, int y ) {
		this.x = x;
		this.y =y;
	}
	
	public void resize(float difference) {
		this.ycord = difference*this.ycord;
		this.xcord = difference*this.xcord;
	}
	
	
	public void draw(PApplet surface) {

		if(dooropener == true) {
			
			
				surface.rect(this.x, this.y, this.xcord, this.ycord);
				surface.triangle(this.x, this.y, this.x+(this.xcord/2), this.y-(this.ycord/4), this.x+this.xcord, this.y);
				surface.square(this.x+(this.ycord/4),this.y+(this.ycord/4),this.ycord/4);
				surface.square(this.x+(this.xcord-(2*(this.ycord/4))),this.y+(this.ycord/4),this.ycord/4);
				surface.rect(this.x+((this.xcord-this.ycord/4)/2),this.y+this.ycord/2,this.ycord/4,this.ycord/2);
				x1 = this.x+((this.xcord-this.ycord/4)/2);
				y1 = this.y+this.ycord/2 - this.ycord/2+this.ycord/2;
				x2 = this.x+((this.xcord-this.ycord/4)/2)+this.ycord/4;
				y2 = this.y+this.ycord/2 - this.ycord/2+this.ycord/2;
				
					
			
		

	}else {
		surface.rect(this.x, this.y, this.xcord, this.ycord);
		surface.triangle(this.x, this.y, this.x+(this.xcord/2), this.y-(this.ycord/4), this.x+this.xcord, this.y);
		surface.square(this.x+(this.ycord/4),this.y+(this.ycord/4),this.ycord/4);
		surface.square(this.x+(this.xcord-(2*(this.ycord/4))),this.y+(this.ycord/4),this.ycord/4);
		surface.rect(this.x+((this.xcord-this.ycord/4)/2)-this.ycord/4,this.y+this.ycord/2,this.ycord/4,this.ycord/2);
	}
	}
	
	
	
	
	

	public boolean intersect(Person p ) {
		
		
		
		float x3 = p.getx1();
		float x4 = p.getx2();
		float y3 = p.gety1();
		float y4 = p.gety2();
		
		float interx = getIntersectionX(p);
		float intery = getIntersectionY(p);
		
		
		
		 parallelcheck = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
		if(parallelcheck==0) {
			return false;
		}
		
		if(interx>x1 &interx>x2) {
			return false;
			
		}
		
		if(interx<x1 &interx<x2) {
			return false;
		}
		
		if(interx>x3 & interx>x4) {
			return false;
		}
		
		if(interx<x3 & interx<x4) {
			return false;
		}
		
		if(intery>y1 & intery>y2) {
			return false;
		}
		
		if(intery<y1 & intery<y2) {
			return false;
		}
		
		if(intery>y3 & intery>y4) {
			return false;
		}
		
		if(intery<y3 & intery<y4) {
			return false;
		}
		
		
		
	
		
		
		
		
		
		return true;
		
	}
	
public float getIntersectionX(Person p){
		
	float x3 = p.getx1();
	float x4 = p.getx2();
	float y3 = p.gety1();
	float y4 = p.gety2();
		float interxtop = 	(x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4);
		float interx = interxtop/parallelcheck;
	
		return interx;
	}												// fix this
	

	public float getIntersectionY(Person p){
		float x3 = p.getx1();
		float x4 = p.getx2();
		float y3 = p.gety1();
		float y4 = p.gety2();
		float interytop = 	(x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4);
		float intery = interytop/parallelcheck;
		
		return intery;					// fix this
	}
		
	}

	
	// Add more methods to modify fields. Make sure methods sound like real actions.





