import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Ball extends MovingImage {
	
	private double xVelocity, yVelocity;
	private boolean onASurface;
	private double friction;
	private double gravity;
	private double jumpStrength;
	private boolean landed;
	
	public Ball(int x, int y) {
		super("ballimg.jpeg", x, y, 20, 20);
		xVelocity = 0;
		yVelocity = 0;
		onASurface = false;
		gravity = 0.1;
		friction = .85;
		jumpStrength = 15;
	}
	
	public void act(ArrayList<Shape> obstacles) {
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();

		// ***********Y AXIS***********

		if (!onASurface) {
			yVelocity += gravity; // GRAVITY
		} else {
//			System.out.println("On a surface!");
			yVelocity = -yVelocity - gravity;
		}

		double yCoord2 = yCoord + yVelocity;

		Rectangle2D.Double strechY = new Rectangle2D.Double(xCoord,Math.min(yCoord,yCoord2),width,height+Math.abs(yVelocity));

//		onASurface = false;

		if (yVelocity > 0) {
			for (Shape s : obstacles) {
				if (s.intersects(strechY)) {
//					System.out.println(yCoord2)
//					landed = true;
					onASurface = true;
//					standingSurface = s;
//					yVelocity = 0;
////					System.out.println(yCoord);
//					yCoord2 = yCoord + yVelocity;
//					System.out.println(yCoord2);
				}
			}
		} else if (yVelocity < 0) {
			onASurface = false;
		}
//		} else if (yVelocity < 0) {
//			Shape headSurface = null;
//			for (Shape s : obstacles) {
//				if (s.intersects(strechY)) {
//					headSurface = s;
//					yVelocity = 0;
//				}
//			}
//			if (headSurface != null) {
//				Rectangle r = headSurface.getBounds();
//				yCoord2 = r.getY()+r.getHeight();
//			}
//		}
//
//		if (Math.abs(yVelocity) < .2)
//			yVelocity = 0;
		
		moveToLocation(xCoord,yCoord2);
	}



}
