package Graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import com.google.firebase.database.FirebaseDatabase;

/**
 * 
 * @author anirudhv
 *
 */

public class FirebaseBackend extends JPanel implements ChildEventListener {
	private JFrame theWindow;

	private ArrayList<String> rooms;
	private JList<String> roomList;

	private JButton connectButton;
	private JButton newRoomButton;

	private DatabaseReference postsRef;

	public FirebaseBackend() {
		rooms = new ArrayList<String>();

		ActionHandler actionEventHandler = new ActionHandler();

		setLayout(new BorderLayout());
		JPanel cnPanel = new JPanel();
		cnPanel.setLayout(new BorderLayout());
		roomList = new JList<String>();
		roomList.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		cnPanel.add(roomList, BorderLayout.CENTER);
		JLabel ah = new JLabel("Select a game below and click join game to enter the room! Click Create Game to create a new room!");
		ah.setHorizontalAlignment(JLabel.CENTER);
		cnPanel.add(ah, BorderLayout.NORTH);
		cnPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		add(cnPanel);

		JPanel ePanel = new JPanel();
		ePanel.setLayout(new GridLayout(1, 5, 15, 15));
		newRoomButton = new JButton("<html><center>Create<br>Game</center></html>");
		newRoomButton.addActionListener(actionEventHandler);
		connectButton = new JButton("<html><center>Join<br>Game</center></html>");
		connectButton.addActionListener(actionEventHandler);

		ePanel.add(newRoomButton);
		ePanel.add(connectButton);

		cnPanel.add(ePanel, BorderLayout.SOUTH);

		// DATABASE SETUP
		InputStream refreshToken;
		try {

			refreshToken = this.getClass().getClassLoader().getResourceAsStream("BasketballAllStars.json");
//			refreshToken = new FileInputStream("./BasketballAllStars.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken))
					.setDatabaseUrl("https://basketballallstars-35240-default-rtdb.firebaseio.com/").build();

			FirebaseApp.initializeApp(options);
			DatabaseReference database = FirebaseDatabase.getInstance().getReference();
			postsRef = database.child("arcade");

			postsRef.addChildEventListener(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Constructs and shows the JFrame main window
	 */
	public void show() {

		theWindow = new JFrame();
		theWindow.add(this);
		theWindow.setBounds(0, 0, 800, 600);
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.setVisible(true);

	}

	/**
	 * Allows one to join a room in the Firebase with a specified name
	 * 
	 * @param name Name of the room to join
	 */
	public void createRoom(String name) {

		postsRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
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
				theWindow.dispose();
			}
		});

	}
	
	public void selectRoom(String name) {

		postsRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {

				if (!snap.hasChildren())
					return;
				
				for(DataSnapshot s: snap.getChildren()) {
					for (DataSnapshot t: s.getChildren()) {
						if (t.getKey().equals("users")) {
							if (t.getChildrenCount() == 2) {
								JOptionPane.showMessageDialog(FirebaseBackend.this, "Room is full!");
								return;	
							}
						}
					}
				}
//				System.out.println(snap.getChildren().next());

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
			}

			@Override
			public void onCancelled(DatabaseError arg0) {
				theWindow.dispose();
			}
		});

	}

	@Override
	public void onCancelled(DatabaseError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		rooms.add(arg0.child("name").getValue(String.class));
		roomList.setListData(rooms.toArray(new String[rooms.size()]));
	}

	@Override
	public void onChildChanged(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildRemoved(DataSnapshot arg0) {
		rooms.remove(arg0.child("name").getValue(String.class));
		roomList.setListData(rooms.toArray(new String[rooms.size()]));
	}

	private class ActionHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == newRoomButton) {

//				String roomName = JOptionPane.showInputDialog("Please choose a name for your room:");
//				if (roomName == null || roomName.isEmpty()) {
//					JOptionPane.showMessageDialog(FirebaseBackend.this, "Room creation fail - The room needs a name.");
//					return;
//				}
//
//				if (rooms.contains(roomName)) {
//					JOptionPane.showMessageDialog(FirebaseBackend.this,
//							"Room creation fail - Room name already exists.");
//					return;
//				}

				postsRef.push().child("name").setValue("Game " + roomList.getModel().getSize(),
						new CompletionListener() {

							@Override
							public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
								createRoom("Game " + (roomList.getModel().getSize()-1));
							}

						});

			} else if (source == connectButton) {
				String sel = roomList.getSelectedValue();

				if (sel != null) {

				}
				selectRoom(sel);

			}

		}
	}

}
