import java.awt.event.*;
import javax.swing.*;

import Graphics.FirebaseBackend;

import java.awt.*;

public class Main extends JFrame {

//	JPanel cardPanel;

	public Main(String title) {
		super(title);
		
		
//		setBounds(100, 100, 800, 322);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		GamePanel panel = new GamePanel();
//		NetworkManagementPanel nmp = new NetworkManagementPanel("SwingChat", 20, panel);
//	    addKeyListener(panel.getKeyHandler());
//		add(panel);
//		setVisible(true);

//	    cardPanel = new JPanel();
//	    CardLayout cl = new CardLayout();
//	    cardPanel.setLayout(cl);
//	    
//		OptionPanel panel1 = new OptionPanel(this);    
//	    GamePanel panel2 = new GamePanel();
//	    
//	
//	    cardPanel.add(panel1,"1");
//	    cardPanel.add(panel2,"2");
//	    
//	    add(cardPanel);
//	
//	    setVisible(true);
	}

	public static void main(String[] args) {
		
		FirebaseBackend b = new FirebaseBackend();
		b.show();
		
//		Main main = new Main("hello world");
//		NetworkManagementPanel nmp = new NetworkManagementPanel("SwingChat", 20, panel);
	}

//	public void changePanel(String name) {
//		((CardLayout)cardPanel.getLayout()).show(cardPanel,name);
//		requestFocus();
//	}

}