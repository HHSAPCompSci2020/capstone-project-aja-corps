import java.awt.event.*;
import javax.swing.*;

import Graphics.FirebaseBackend;

import java.awt.*;

public class Main extends JFrame {

	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) {
		
		FirebaseBackend b = new FirebaseBackend();
		b.show();
	}

}