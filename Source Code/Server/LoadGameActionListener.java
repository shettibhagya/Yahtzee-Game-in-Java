package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import game.ImagePanel;
import game.TrackGame;

public class LoadGameActionListener implements ActionListener{

	private static ArrayList<Integer> activeKeep; 
	private static ArrayList<JTextField> upperTextFields;
	private static ArrayList<JTextField> lowerTextFields;
	private static ArrayList<Integer> categoriesUsed;
	private static TrackGame trackGame;
	private static String name;
	private static JTextField tName;
	private static int gameId;
	private ArrayList<JCheckBox> checkBoxList;
	private static ArrayList<Integer> imageSequence;
	private static ArrayList<ImagePanel> imagePanels;

	public LoadGameActionListener(JTextField tName, ArrayList<Integer> activeKeep, ArrayList<JTextField> upperTextFields, ArrayList<JTextField> lowerTextFields, ArrayList<Integer> categoriesUsed, TrackGame trackGame2, ArrayList<JCheckBox> checkBoxList, ArrayList<Integer> imageSequence, ArrayList<ImagePanel> imagePanels){
		super();
		this.tName=tName;
		this.activeKeep=activeKeep;
		this.upperTextFields=upperTextFields;
		this.lowerTextFields=lowerTextFields;
		this.categoriesUsed=categoriesUsed;
		this.trackGame=trackGame2;
		this.checkBoxList=checkBoxList;
		this.imageSequence=imageSequence;
		this.imagePanels=imagePanels;
	}
	
	private Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = "jdbc:sqlite:yahtzee.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
	public List<GameObject> getNames(){
        String sql = "SELECT gameId,name,time FROM game_data";
        List<GameObject> names=new ArrayList<GameObject>();
        try {
        	Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
               while (rs.next()) {
            	   GameObject go=new GameObject();
            	   go.setGameId(rs.getInt("gameId"));
            	   go.setName(rs.getString("name"));
            	   go.setTime(rs.getString("time"));
            	   names.add(go);
               }
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
        return names;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		List<GameObject> names=getNames();
		JFrame frame=new JFrame();
		frame.setTitle("Load Game");
		frame.setSize(600, 400);
		JPanel panel=new JPanel();

		for(GameObject go:names) {
			JButton button = new JButton(go.getName()+" "+go.getTime());
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameId=go.getGameId();
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

					loadGamedata();
					System.out.print("Track game=========="+trackGame.getRollNumber()+" "+trackGame.getRoundNumber());
					loadActiveKeep();
					for(JCheckBox ch:checkBoxList) {
						System.out.println("check==========="+ch.isSelected());
					}
					loadTextfields();
					for(JTextField jt:upperTextFields) {
						System.out.println("textFild================"+jt.getText());
					}
					loadCategoriesUsed();
				}
			});

			panel.add(button);
		}
		
		JScrollPane loadScrPane = new JScrollPane(panel);
		loadScrPane.setPreferredSize(new Dimension(250,80));
		frame.add(loadScrPane,BorderLayout.CENTER);
		frame.setVisible(true);  
	}

	private void loadCategoriesUsed() {
		// TODO Auto-generated method stub
		String sql = "SELECT c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13 FROM categoryused_data where gameId=?";
        
        try {
			Connection conn = this.connect();
        	PreparedStatement pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1,gameId);
            ResultSet rs    = pstmt.executeQuery();
			while (rs.next()) {
				for(int i=1;i<=13;i++) {
					categoriesUsed.set(i-1,rs.getInt(i));
				} 
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void loadTextfields() {
		String sql = "SELECT t1,t2,t3,t4,t5,t6,t7,t8,t9,t10," + 
				"t11,t12,t13,t14,t15,t16,t17,t18,t19 FROM textfield_data where gameId=?";
        
        try {
			Connection conn = this.connect();
        	PreparedStatement pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1,gameId);
            ResultSet rs    = pstmt.executeQuery();
			while (rs.next()) {
				for(int i=1;i<=9;i++) {
					upperTextFields.get(i-1).setText(rs.getString(i));
				}
				for(int i=10;i<=19;i++) {
					lowerTextFields.get(i-10).setText(rs.getString(i));
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void loadActiveKeep() {
		String sql = "SELECT a1,a2,a3,a4,a5,i1,i2,i3,i4,i5 FROM activekeep_data where gameId=?";
        
        try {
			Connection conn = this.connect();
        	PreparedStatement pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1,gameId);
            ResultSet rs    = pstmt.executeQuery();
			while (rs.next()) {
				for(int i=1;i<=5;i++) {
					activeKeep.set(i-1,rs.getInt(i));
				}
				for(int i=6;i<11;i++) {
					imageSequence.set(i-6,rs.getInt(i));
				}
			}
			for(int i=0;i<5;i++) {
				checkBoxList.get(i).setSelected(activeKeep.get(i)==1?true:false);
			}
			for(int i=0;i<5;i++) {
				imagePanels.get(i).setImage(new ImageIcon("die"+(imageSequence.get(i))+".png").getImage());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void loadGamedata() {
		// TODO Auto-generated method stub
		String sql = "SELECT name,round,roll FROM game_data where gameId=?";
        
        try {
			Connection conn = this.connect();
        	PreparedStatement pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1,gameId);
            ResultSet rs   = pstmt.executeQuery();
			while (rs.next()) {
				trackGame.setRoundNumber(rs.getInt(2));
				trackGame.setRollNumber(rs.getInt(3));
				tName.setText(rs.getString(1));
				System.out.println("--------player---------"+rs.getString(1));
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
