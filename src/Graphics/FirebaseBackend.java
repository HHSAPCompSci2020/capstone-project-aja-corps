package Graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

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

public class FirebaseBackend extends JPanel implements ChildEventListener
{
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
		cnPanel.add(roomList,BorderLayout.CENTER);
		JLabel ah = new JLabel("Available Rooms");
		ah.setHorizontalAlignment(JLabel.CENTER);
		cnPanel.add(ah,BorderLayout.NORTH);
		cnPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		add(cnPanel);
		
		
		JPanel ePanel = new JPanel();
		ePanel.setLayout(new GridLayout(1,5,15,15));
		newRoomButton = new JButton("<html><center>Create<br>A Room</center></html>");
		newRoomButton.addActionListener(actionEventHandler);
		connectButton = new JButton("<html><center>Join<br>Room</center></html>");
		connectButton.addActionListener(actionEventHandler);

		
		ePanel.add(newRoomButton);
		ePanel.add(connectButton);
		
		cnPanel.add(ePanel,BorderLayout.SOUTH);
		


		// DATABASE SETUP
		FileInputStream refreshToken;
		try {

			refreshToken = new FileInputStream("BasketballAllStars.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken))
					.setDatabaseUrl("https://basketballallstars-35240-default-rtdb.firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
			DatabaseReference database = FirebaseDatabase.getInstance().getReference();
			postsRef = database.child("arcade");

			postsRef.addChildEventListener(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void show() {
		
		theWindow = new JFrame();
		theWindow.add(this);
		theWindow.setBounds(0, 0, 800, 600);
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.setVisible(true);
		
	}
	
	
	public void selectRoom(String name) {
		

		postsRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {
				
				if (!snap.hasChildren())
					return;
				
				theWindow.setVisible(false);
				
				JFrame window = new JFrame();
				
				window.setBounds(100, 100, 800, 322);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				GamePanel panel = new GamePanel(snap.getChildren().iterator().next().getRef());
			    window.addKeyListener(panel.getKeyHandler());
				window.add(panel);
				window.setVisible(true);

				
				
//				DrawingSurface drawing = new DrawingSurface(snap.getChildren().iterator().next().getRef());
//				PApplet.runSketch(new String[]{""}, drawing);
//				PSurfaceAWT surf = (PSurfaceAWT) drawing.getSurface();
//				PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
//				JFrame window = (JFrame)canvas.getFrame();
//
//				window.setSize(800, 600);
//				window.setMinimumSize(new Dimension(100,100));
//				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				window.setResizable(true);
//
//				window.setVisible(true);
				
//				canvas.requestFocus();
				
				theWindow.dispose();
			}

			@Override
			public void onCancelled(DatabaseError arg0) {
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
				
				String roomName = JOptionPane.showInputDialog("Please choose a name for your room:");
				if (roomName == null || roomName.isEmpty()) {
					JOptionPane.showMessageDialog(FirebaseBackend.this, "Room creation fail - The room needs a name.");
					return;
				}
				
				if (rooms.contains(roomName)) {
					JOptionPane.showMessageDialog(FirebaseBackend.this, "Room creation fail - Room name already exists.");
					return;
				}
				
				postsRef.push().child("name").setValue(roomName, new CompletionListener() {

					@Override
					public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
						selectRoom(roomName);
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
