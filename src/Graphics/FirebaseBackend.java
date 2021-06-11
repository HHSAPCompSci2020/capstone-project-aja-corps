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
	public ArrayList<String> rooms;
	public DatabaseReference postsRef;

	public FirebaseBackend() {
		rooms = new ArrayList<String>();
		InputStream refreshToken;
		try {

			refreshToken = this.getClass().getClassLoader().getResourceAsStream("BasketballAllStars.json");
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

	@Override
	public void onCancelled(DatabaseError arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Overridden method from firebase's ChildEventListener which adds the room name
	 * to the local array when a new room is added to the database
	 * 
	 * @param DataSnapshot arg0 Firebase DataSnapshot of the room
	 * @param String       arg1
	 * @post Adds a new room to the rooms array
	 */
	@Override
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		rooms.add(arg0.child("name").getValue(String.class));
	}

	@Override
	public void onChildChanged(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * Overridden method from firebase's ChildEventListener which removes the room
	 * name from the local array rooms when a new room is removed from the database
	 * 
	 * @param DataSnapshot arg0 Firebase DataSnapshot of the room
	 * @param String       arg1
	 * @post Removes room from rooms array	
	 */
	@Override
	public void onChildRemoved(DataSnapshot arg0) {
		rooms.remove(arg0.child("name").getValue(String.class));
	}
}
