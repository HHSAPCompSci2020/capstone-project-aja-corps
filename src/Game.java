import java.awt.event.*;
import javax.swing.*;

import Graphics.FirebaseBackend;
import Graphics.Home;

import java.awt.*;

public class Game extends JFrame {

	public Game(String title) {
		super(title);
	}

	public static void main(String[] args) {

//		FirebaseBackend b = new FirebaseBackend();
//		b.show();
		
		Home screen = new Home();
		screen.show();
	}

}