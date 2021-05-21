package Graphics;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

public class Home extends JPanel {
	private JButton connectButton;
	private JButton newRoomButton;
	private JFrame theWindow;
	private boolean outOfTutorial;
	
	public Home() {
		ActionHandler actionEventHandler = new ActionHandler();
		newRoomButton = new JButton("<html><center>Play Tutorial</center></html>");
		newRoomButton.addActionListener(actionEventHandler);
		connectButton = new JButton("<html><center>Play Online</center></html>");
		connectButton.addActionListener(actionEventHandler);
		this.add(newRoomButton);
		this.add(connectButton);
	}
	
	public void paintComponent(Graphics g) {
		
	}

	public void show() {

		theWindow = new JFrame();
		theWindow.add(this);
		theWindow.setBounds(0, 0, 800, 600);
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.setVisible(true);

	}
	
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
	
	public void playOnline() {
		theWindow.setVisible(false);
		FirebaseBackend panel = new FirebaseBackend();
		panel.show();
		theWindow.dispose();
	}
	
	private class ActionHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == newRoomButton) {
				runTutorial();
			} else {
				playOnline();
			}
		}
	}
}
