package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UpperSectionActionListener implements ActionListener {

	private TrackGame trackGame;
	private ArrayList<Integer> imageSequence;
	private int buttonNo;
	private ArrayList<JTextField> upperTextFields;
	private ArrayList<Integer> categoriesUsed;
	private ArrayList<JCheckBox> checkBoxList;
	private JLabel turn;
	private JLabel roll;
	
	public UpperSectionActionListener(int buttonNo, 
	TrackGame trackGame, 
	ArrayList<Integer> imageSequence, 
	ArrayList<JTextField> upperTextFields, 
	ArrayList<Integer> categoriesUsed, 
	ArrayList<JCheckBox> checkBoxList,
	JLabel turn,
	JLabel roll) {
		super();
		this.buttonNo=buttonNo;
		this.trackGame=trackGame;
		this.imageSequence=imageSequence;
		this.upperTextFields=upperTextFields;
		this.categoriesUsed=categoriesUsed;
		this.checkBoxList=checkBoxList;
		this.turn = turn;
		this.roll = roll;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int usedCount=0;
		for(int i:categoriesUsed) {
			if(i==1)
				usedCount++;
		}
		
		if(usedCount == trackGame.getRoundNumber() && trackGame.getRollNumber()!=0 && categoriesUsed.get(buttonNo-1)==0) {
			categoriesUsed.set(buttonNo-1,1);
			System.out.print("Round"+(trackGame.getRoundNumber()+1)+"Ended");
			trackGame.setRoundNumber(trackGame.getRoundNumber()+1);
			trackGame.setRollNumber(0);
			for(JCheckBox cb:checkBoxList) {
				cb.setSelected(false);
			}
			int score=0;
			for(int i:imageSequence) {
				if(i == buttonNo)
					score+=i;
			}
			int subTotal=Integer.parseInt(upperTextFields.get(6).getText())+score;
			int bonus=subTotal>=63?35:0;
			upperTextFields.get(buttonNo-1).setText(score+"");
			upperTextFields.get(6).setText((subTotal)+"");
			upperTextFields.get(7).setText(bonus+"");
			upperTextFields.get(8).setText((subTotal+bonus)+"");
			turn.setText("Turn:"+ trackGame.getRoundNumber());
			roll.setText("Roll:"+ trackGame.getRollNumber());
		}
		else if(usedCount >= trackGame.getRoundNumber() && trackGame.getRollNumber() == 0) {
			JOptionPane.showMessageDialog(null, "Roll The Dice");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "This Category Has Already Been Used");
		}
	}
}
