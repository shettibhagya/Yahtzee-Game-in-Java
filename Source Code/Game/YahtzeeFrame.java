package game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import server.LoadGameActionListener;
import server.SaveGameActionListener;

public class YahtzeeFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static ArrayList<ImagePanel> imagePanels;
	private static ArrayList<Integer> imageSequence;
	private static ArrayList<Integer> activeKeep; 
	private static ArrayList<JTextField> upperTextFields;
	private static ArrayList<JTextField> lowerTextFields;
	private static ArrayList<Integer> trackCategories;
	private static ArrayList<JCheckBox> checkBoxList;
	private static ArrayList<JButton> buttonsList;

	private static TrackGame trackGame;

	//labels
	private JLabel turnLabel;
	private JLabel rollLabel;

	//textfields
	private static JTextField playerTextField;

	//panels
	private JPanel playerPanel;
	private JPanel dicePanel;
	private JPanel upperLowerPanel;

	private JMenuBar menuBar;
	private JMenuItem loadItem;
	private JMenuItem saveItem;
	private JMenuItem exit;
	private JButton rollDice;

	//strings
	private String[] upperSectionLabel = {"Aces", "Twos", "Threes", "Fours", "Fives", "Sixes", "Sub Total", "Bonus", "Grand Total"};
	private String[] lowerSectionLabel = {"3 of a Kind", "4 of a Kind", "Full House", "Small Straight", "Large Straight", 
	"Yahtzee", "Chance", "Yahtzee Bonus", "Total Of Lower Section", "Grand Total"};
	
	public YahtzeeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,700);

		//initializations
		trackGame=new TrackGame();
		buttonsList=new ArrayList<JButton>();
		upperTextFields=new ArrayList<JTextField>();
		lowerTextFields=new ArrayList<JTextField>();
		imagePanels=new ArrayList<ImagePanel>();
		activeKeep=new ArrayList<Integer>();
		imageSequence=new ArrayList<Integer>();	
		trackCategories=new ArrayList<Integer>();

		for(int i=0; i<5; i++) {
			activeKeep.add(0);
			imageSequence.add(1);
		}

		for(int i=0; i<13; i++) {
			trackCategories.add(0);
		}

		setTitle("Yahtzee Game");
		JPanel menuPlayerPanel =new JPanel();
		menuPlayerPanel.setLayout(new GridLayout(2,1,2,5));

		createMenuBar();	
		menuPlayerPanel.add(menuBar,BorderLayout.NORTH);
		createPlayerMenu();
		menuPlayerPanel.add(playerPanel);
		add(menuPlayerPanel,BorderLayout.NORTH);
		
		createDiceSection();
		add(dicePanel,BorderLayout.EAST);

		upperLowerPanel = new JPanel();
		upperLowerPanel.setLayout(new BoxLayout(upperLowerPanel,BoxLayout.Y_AXIS));
		createUpperSection();
		createLowerSection();
		add(upperLowerPanel,BorderLayout.WEST);
		
		setVisible(true);
		
		saveItem.addActionListener(new SaveGameActionListener(playerTextField,activeKeep,upperTextFields,lowerTextFields,trackCategories,trackGame,imageSequence));
		loadItem.addActionListener(new LoadGameActionListener(playerTextField, activeKeep, upperTextFields, lowerTextFields, trackCategories, trackGame,checkBoxList,imageSequence,imagePanels));
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispatchEvent(new WindowEvent(new YahtzeeFrame(), WindowEvent.WINDOW_CLOSING));
				
			}
		});

		//set round and roll to 0
		trackGame.setRoundNumber(0);
		trackGame.setRollNumber(0);
	}
	

	private void createMenuBar() {
		menuBar = new JMenuBar();
		JMenu file = new JMenu("Game");
		loadItem = new JMenuItem("Load Game");
		saveItem = new JMenuItem("Save Game");
		exit = new JMenuItem("Exit");
		file.add(loadItem);
		file.add(saveItem);
		file.add(exit);
		menuBar.add(file);	
	}

	private void createPlayerMenu() {
		playerPanel=new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel,BoxLayout.X_AXIS));
		JLabel playerLabel=new JLabel("Player Name: ");
		playerPanel.add(playerLabel);
		playerTextField = new JTextField();	
		playerPanel.add(playerTextField);
	}

	private void createUpperSection() {
		JPanel upperSubPanel = new JPanel();
		Border upperLabel = BorderFactory.createTitledBorder("Upper Section");
		upperSubPanel.setLayout(new GridLayout(0,2,2,4));
		upperSubPanel.setBorder(upperLabel);
		upperSubPanel.setSize(400,200);

		for(int i=0; i<6; i++) {
			JTextField textField = new JTextField(10);
			JButton button = new JButton(upperSectionLabel[i]);
			upperTextFields.add(textField);
			buttonsList.add(button);
			button.addActionListener(new UpperSectionActionListener(i+1, trackGame, imageSequence, upperTextFields,
			trackCategories, checkBoxList, turnLabel, rollLabel));
			upperSubPanel.add(button);
			upperSubPanel.add(textField);
		}

		for(int i=6; i<9; i++) {
			JTextField textField = new JTextField(10);
			JLabel label = new JLabel(upperSectionLabel[i]);
			upperTextFields.add(textField);
			textField.setText("0");
			upperSubPanel.add(label);
			upperSubPanel.add(textField);
		}
		upperLowerPanel.add(upperSubPanel);
	}

	private void createLowerSection() {
		JPanel lowerSubPanel = new JPanel();
		Border lowerLabel = BorderFactory.createTitledBorder("Lower Section");
		lowerSubPanel.setLayout(new GridLayout(0,2,2,4));
		lowerSubPanel.setBorder(lowerLabel);
		lowerSubPanel.setSize(400,200);

		for(int i=0; i<7; i++) {
			JTextField textField = new JTextField(10);
			JButton button = new JButton(lowerSectionLabel[i]);
			lowerTextFields.add(textField);
			buttonsList.add(button);
			button.addActionListener(new LowerSectionActionListener(i+7, trackGame, imageSequence, lowerTextFields,
			trackCategories, checkBoxList, turnLabel, rollLabel));
			lowerSubPanel.add(button);
			lowerSubPanel.add(textField);
		}

		for(int i=7; i<10; i++) {
			JTextField textField = new JTextField(10);
			JLabel label = new JLabel(lowerSectionLabel[i]);
			lowerTextFields.add(textField);
			textField.setText("0");
			lowerSubPanel.add(label);
			lowerSubPanel.add(textField);
		}
		upperLowerPanel.add(lowerSubPanel);
	}

	private void createDiceSection() {
		dicePanel = new JPanel();
		dicePanel.setSize(300,500);
		dicePanel.setLayout(new GridLayout(0,1,0,0));

		checkBoxList=new ArrayList<JCheckBox>();

		for(int i=0; i<5; i++) {
			JCheckBox checkBox = new JCheckBox("Keep");
			checkBoxList.add(checkBox);
			checkBox.addItemListener(new KeepActionListener(i,activeKeep));
			ImagePanel imagePanel = new ImagePanel(new ImageIcon("die1.png").getImage());
			imagePanel.scaleImage(0.25);
			imagePanels.add(imagePanel);
			dicePanel.add(imagePanel);
			dicePanel.add(checkBox);
		}

		//add Turn and roll labels and values
		turnLabel = new JLabel("Turn:"+ trackGame.getRoundNumber());
		rollLabel = new JLabel("Roll:" + trackGame.getRollNumber());
		dicePanel.add(turnLabel);
		dicePanel.add(rollLabel);

		//add dice roll button
		rollDice = new JButton("Roll");
		buttonsList.add(rollDice);
		rollDice.addActionListener(new RollActionListener(imagePanels,activeKeep,trackGame,imageSequence,checkBoxList, rollLabel));
		dicePanel.add(rollDice);
	}
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
	}
}
