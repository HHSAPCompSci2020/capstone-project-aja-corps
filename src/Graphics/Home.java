package Graphics;


import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;


/**
 * 
 * @author anirudhv
 *
 */
public class Home extends JPanel {
	private JButton connectButton;
	private JButton newRoomButton;
	private JButton soloButton;
	private JFrame theWindow;
	private boolean outOfTutorial;
	private Image backgroundImage;
	
	
	
	public Home() {
		try {
//			backgroundImage = ImageIO.read(new File("img/TitleScreen.png"));
			backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("img/TitleScreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ActionHandler actionEventHandler = new ActionHandler();
		newRoomButton = new JButton("Play Tutorial");
		newRoomButton.addActionListener(actionEventHandler);
		connectButton = new JButton("Play Online");
		connectButton.addActionListener(actionEventHandler);
		soloButton = new JButton("Play Solo");
		soloButton.addActionListener(actionEventHandler);
		
		this.add(newRoomButton);
		this.add(connectButton);
		this.add(soloButton);
	}
	/**
	 * @param g - the graphics object which the program is drawn on
	 */
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		int width = getWidth();
		int height = getHeight();

		double ratioX = (double) width / 800;
		double ratioY = (double) height / 316;

		AffineTransform at = g2.getTransform();
		g2.scale(ratioX, ratioY);
		g.drawImage(backgroundImage, 0, 0, this);
		g2.setTransform(at);
		connectButton.setBounds(410, 200, 130, 30);
		newRoomButton.setBounds(260, 200, 130, 30);
		soloButton.setBounds(350, 225, 100, 30);
	}
	
	/**
	 * draws the actual game
	 */

	public void show() {

		theWindow = new JFrame();
		theWindow.add(this);
		theWindow.setBounds(0, 0, 800, 316);
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.setVisible(true);

	}
	
	/**
	 * runs the interactive tutorial
	 */
	public void runTutorial() {
		theWindow.setVisible(false);

		JFrame window = new JFrame();

		window.setBounds(100, 100, 800, 322);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CourtTutorial panel = new CourtTutorial("TestPlayer");
		window.addKeyListener(panel.getKeyHandler());
		window.add(panel);
		window.setVisible(true);

		theWindow.dispose();
	}
	
	/**
	 * runs the main online game mode
	 */
	public void playOnline() {
		theWindow.setVisible(false);
		FirebaseBackend panel = new FirebaseBackend();
		panel.show();
		theWindow.dispose();
	}
	
	/**
	 * runs the solo game mode
	 */
	public void playSolo() {
		theWindow.setVisible(false);

		JFrame window = new JFrame();

		window.setBounds(100, 100, 800, 322);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CourtSolo panel = new CourtSolo("TestPlayer");
		window.addKeyListener(panel.getKeyHandler());
		window.add(panel);
		window.setVisible(true);

		theWindow.dispose();
	}
	
	private class ActionHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == newRoomButton) {
				runTutorial();
			} else if (source == connectButton) {
				playOnline();
			} else {
				playSolo();
			}
		}
	}
}
