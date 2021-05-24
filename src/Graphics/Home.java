package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference.CompletionListener;

/**
 * 
 * @author anirudhv
 *
 */
public class Home extends JPanel {
	private JButton connectButton;
	private JButton tutorialButton;
	private JButton soloButton;
	private JButton joinGame;
	private JFrame theWindow;
	private boolean outOfTutorial;
	private Image backgroundImage;
	private ArrayList<String> rooms;
	FirebaseBackend backend;
	private boolean openRoom;
	
	public Home() {
		try {
//			backgroundImage = ImageIO.read(new File("img/TitleScreen.png"));
			backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("img/TitleScreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		backend = new FirebaseBackend();
		rooms = new ArrayList<>();
		ActionHandler actionEventHandler = new ActionHandler();
		tutorialButton = new JButton("Play Tutorial");
		tutorialButton.addActionListener(actionEventHandler);
		connectButton = new JButton("Create Online Game");
		connectButton.addActionListener(actionEventHandler);
		soloButton = new JButton("Play Solo");
		soloButton.addActionListener(actionEventHandler);
		joinGame = new JButton("Join Online Game");
		joinGame.addActionListener(actionEventHandler);
		
		this.add(tutorialButton);
		this.add(connectButton);
		this.add(soloButton);
		this.add(joinGame);
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
		tutorialButton.setBounds(260, 200, 130, 30);
		soloButton.setBounds(260, 225, 130, 30);
		joinGame.setBounds(410, 225, 130, 30);
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

	public void createRoom(String name) {

		backend.postsRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {

				if (!snap.hasChildren())
					return;

				String playerName = JOptionPane.showInputDialog("Enter your username:");

				theWindow.setVisible(false);

				JFrame window = new JFrame();

				window.setBounds(100, 100, 800, 322);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Court panel = new Court(snap.getChildren().iterator().next().getRef(), playerName, 1);
				window.addKeyListener(panel.getKeyHandler());
				window.add(panel);
				window.setVisible(true);

				theWindow.dispose();
			}

			@Override
			public void onCancelled(DatabaseError arg0) {
//				theWindow.dispose();
			}
		});

	}
	
	public void selectRoom(String name) {

		backend.postsRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {

				if (!snap.hasChildren())
					return;
				
				for(DataSnapshot s: snap.getChildren()) {
					for (DataSnapshot t: s.getChildren()) {
						if (t.getKey().equals("users")) {
							if (t.getChildrenCount() == 1) {
//								System.out.println(snap.getChildren().next());

								openRoom = true;
								String playerName = JOptionPane.showInputDialog("Enter your username:");

								theWindow.setVisible(false);

								JFrame window = new JFrame();

								window.setBounds(100, 100, 800, 322);
								window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								Court panel = new Court(snap.getChildren().iterator().next().getRef(), playerName, 2);
								window.addKeyListener(panel.getKeyHandler());
								window.add(panel);
								window.setVisible(true);

								theWindow.dispose();
								return;
							}
						}
					}
				}
				
//				JOptionPane.showMessageDialog(Home.this, "Room is full!");

			}

			@Override
			public void onCancelled(DatabaseError arg0) {
				theWindow.dispose();
			}
		});
		
		if (openRoom) {
			return;
		} else {
			openRoom = false;
		}
		
	}
	
	
	/**
	 * runs the main online game mode
	 */
	public void playOnline() {
//		theWindow.setVisible(false);
//		System.out.println(postsRef);
//		System.out.println(backend.rooms.zi)
		backend.postsRef.push().child("name").setValue("Game " + backend.rooms.size(),
				new CompletionListener() {
					@Override
					public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
//						System.out.println(backend.rooms.size());
						createRoom("Game " + (backend.rooms.size()-1));
					}

				});

		
//		panel.show();
//		theWindow.dispose();
		
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
			if (source == tutorialButton) {
				runTutorial();
			} else if (source == connectButton) {
				playOnline();
			} else if (source == joinGame) {
				for (int i = 0; i < backend.rooms.size(); i++) {
					selectRoom("Game " + i);
				}
				JOptionPane.showMessageDialog(Home.this, "All Rooms Full!");

			} else {
				playSolo();
			}
		}
	}
}
