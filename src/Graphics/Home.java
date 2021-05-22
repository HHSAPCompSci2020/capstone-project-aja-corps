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

import jay.jaysound.JayLayer;
import jay.jaysound.JayLayerListener;

public class Home extends JPanel implements JayLayerListener{
	private JButton connectButton;
	private JButton newRoomButton;
	private JFrame theWindow;
	private boolean outOfTutorial;
	
	private static JayLayer sound;
	
	public Home() {
		ActionHandler actionEventHandler = new ActionHandler();
		newRoomButton = new JButton("<html><center>Play Tutorial</center></html>");
		newRoomButton.addActionListener(actionEventHandler);
		connectButton = new JButton("<html><center>Play Online</center></html>");
		connectButton.addActionListener(actionEventHandler);
		this.add(newRoomButton);
		this.add(connectButton);
		
		String[] soundEffects = new String[] {"bounce.mp3", "jump.mp3", "steal.mp3", "swish.mp3", "rim.mp3", "crowd.mp3", "scoreboard.mp3"};
		String[] songs = new String[] {"track1.mp3", "track2.mp3", "track3.mp3", "track4.mp3"};
		
		sound = new JayLayer("audio/","audio/",false);
		sound.addPlayList();
		sound.addSongs(0,songs);
		sound.addSoundEffects(soundEffects);
		sound.changePlayList(0);
		sound.addJayLayerListener(this);
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

	public static void soundEffect(int i) {
		sound.playSoundEffect(i);
	}
	
	@Override
	public void musicStarted() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void musicStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playlistEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void songEnded() {
		// TODO Auto-generated method stub
		
	}
}
