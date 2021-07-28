package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import game.TrackGame;

public class SaveGameActionListener implements ActionListener{
	public static ArrayList<Integer> activeKeep; 
	public static ArrayList<JTextField> upperTextFields;
	public static ArrayList<JTextField> lowerTextFields;
	public static ArrayList<Integer> categoriesUsed;
	public static TrackGame trackGame;
	public static String name;
	public static JTextField tName; 
	public static int gameId;
	public static ArrayList<Integer> imageSequence;
	public SaveGameActionListener(JTextField tName, ArrayList<Integer> activeKeep, ArrayList<JTextField> upperTextFields, ArrayList<JTextField> lowerTextFields, ArrayList<Integer> categoriesUsed, TrackGame trackGame2, ArrayList<Integer> imageSequence){
		super();	
		this.tName=tName;
		this.activeKeep=activeKeep;
		this.upperTextFields=upperTextFields;
		this.lowerTextFields=lowerTextFields;
		this.categoriesUsed=categoriesUsed;
		this.trackGame=trackGame2;
		this.imageSequence=imageSequence;
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
	
	public void insertGameData() {
        String sql = "INSERT OR REPLACE INTO game_data(name,round,roll,time) VALUES(?,?,?,?)";

        try {
        	Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("game Data Name:"+name);
            pstmt.setString(1,name);
            pstmt.setInt(2, trackGame.getRoundNumber());
            pstmt.setInt(3, trackGame.getRollNumber());
            Date date = Calendar.getInstance().getTime();  
            DateFormat dateFrmt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
            String dateStr = dateFrmt.format(date);  
            pstmt.setString(4, dateStr);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        String sqlRowId="select seq from sqlite_sequence where name='game_data';";
        try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sqlRowId)){
        	gameId=rs.getInt(1);
        System.out.println("GameId:"+gameId);   
        } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
     		
    }
	
	public void insertTextFields() {
        String sql = "INSERT OR REPLACE INTO textfield_data(gameId,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,"
        		+ "t11,t12,t13,t14,t15,t16,t17,t18,t19) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
        	Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,gameId);
            for(int i=2;i<11;i++) {
            	 pstmt.setString(i,upperTextFields.get(i-2).getText());	
            }
            for(int i=11;i<21;i++) {
           	 pstmt.setString(i,lowerTextFields.get(i-11).getText());	
           }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	public void insertCategoriesUsed() {
        String sql = "INSERT OR REPLACE INTO categoryused_data(gameId,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try  {
        	Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,gameId);
            for(int i=2;i<15;i++) {
            	 pstmt.setInt(i,categoriesUsed.get(i-2));	
            }        
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	public void insertActiveKeep() {
        String sql = "INSERT OR REPLACE INTO activekeep_data(gameId,a1,a2,a3,a4,a5,i1,i2,i3,i4,i5) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try {     	
        	Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,gameId);
            for(int i=2;i<7;i++) {
            	 pstmt.setInt(i,activeKeep.get(i-2));	
            }
            for(int i=7;i<12;i++) {
           	 pstmt.setInt(i,imageSequence.get(i-7));	
           }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	public void createTables() {
        String sql1 = "CREATE TABLE IF NOT EXISTS game_data ("  
                + " gameId integer PRIMARY KEY AUTOINCREMENT,"
                + " name text,"
                + " round integer,"  
                + " roll integer," 
                + " time text"
                + ");";
        
        String sql2 = "CREATE TABLE IF NOT EXISTS textfield_data ("  
                + " gameId integer PRIMARY KEY,"  
                + " t1 text,"  
                + " t2 text,"
                + " t3 text,"
                + " t4 text,"
                + " t5 text,"
                + " t6 text,"
                + " t7 text,"
                + " t8 text,"
                + " t9 text,"
                + " t10 text,"
                + " t11 text,"
                + " t12 text,"
                + " t13 text,"
                + " t14 text,"
                + " t15 text,"
                + " t16 text,"
                + " t17 text,"
                + " t18 text,"
                + " t19 text"
                + ");";
        String sql3 = "CREATE TABLE IF NOT EXISTS categoryused_data ("  
                + " gameId integer PRIMARY KEY,"  
                + " c1 integer,"
                + " c2 integer,"
                + " c3 integer,"
                + " c4 integer,"
                + " c5 integer,"
                + " c6 integer,"
                + " c7 integer,"
                + " c8 integer,"
                + " c9 integer,"
                + " c10 integer,"
                + " c11 integer,"
                + " c12 integer,"
                + " c13 integer"
                + ");";
        String sql4 = "CREATE TABLE IF NOT EXISTS activekeep_data ("  
                + "gameId integer PRIMARY KEY,"  
                + "a1 integer,"
                + "a2 integer,"
                + "a3 integer,"
                + "a4 integer,"
                + "a5 integer,"
                + "i1 integer,"
                + "i2 integer,"
                + "i3 integer,"
                + "i4 integer,"
                + "i5 integer"
                + ");";

        try {
        	Connection conn = this.connect();
        	Statement stmt = conn.createStatement();  
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	
	@Override
	public void actionPerformed(ActionEvent e) {
		name=tName.getText();
		createTables();
		insertGameData();
		insertTextFields();
		insertCategoriesUsed();
		insertActiveKeep();
		
		System.out.println("tname: "+tName.getText()+"====gameId==="+gameId);
		JOptionPane.showMessageDialog(null, "Saved Game");
	}
	
}
