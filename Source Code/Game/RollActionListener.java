package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class RollActionListener implements ActionListener {

	private ArrayList<ImagePanel> imagePanels;
	private ArrayList<Integer> activeKeep;
	private TrackGame trackGame;
	private ArrayList<Integer> imageSequence;
	private ArrayList<JCheckBox> checkBoxList;
	private ArrayList<Integer> imageNo;
	private JLabel roll;

	public RollActionListener(ArrayList<ImagePanel> imagePanels, ArrayList<Integer> activeKeep, TrackGame trackGame, ArrayList<Integer> imageSequence,
	 ArrayList<JCheckBox> checkBoxList, JLabel roll) {
		super();
		this.imagePanels=imagePanels;
		this.activeKeep=activeKeep;
		this.trackGame=trackGame;
		this.imageSequence=imageSequence;
		this.checkBoxList=checkBoxList;
		this.imageNo=new ArrayList<Integer>();
		this.roll = roll;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0; i<5; i++) {
			imageNo.add(1);
		}
		//for round 0
		if(trackGame.getRollNumber()==0) {
			for(int k=0;k<5;k++) {
				if(activeKeep.get(k)==1) {
					JOptionPane.showMessageDialog(null, "Must Roll All Dice...");
					break;
				}
			}
			for(int k=0;k<5;k++) {	
				checkBoxList.get(k).setSelected(false);
				activeKeep.set(k,0);
			}
		}
		//for round greater than 0 and less than 3
		if(trackGame.getRoundNumber()<13 && trackGame.getRollNumber()<3) {			
				for(int k=0;k<5;k++) {
					if(activeKeep.get(k)==0) {
						imageNo.set(k,((int)(Math.random()*43))%6);
					}
					else
					{
						imageNo.set(k,-1);
					}
				}
				for(int i=0;i<5;i++) {
					if(imageNo.get(i)!=-1) {
						System.out.println("=================die"+(imageNo.get(i)+1)+".png");
						imagePanels.get(i).setImage(new ImageIcon("die"+(imageNo.get(i)+1)+".png").getImage());
						imageSequence.set(i, imageNo.get(i)+1);
					}
				}
				
				trackGame.setRollNumber(trackGame.getRollNumber()+1);
				roll.setText("Roll:"+ trackGame.getRollNumber());
			}
		else if(trackGame.getRollNumber()<13 && trackGame.getRollNumber()==3) {
			JOptionPane.showMessageDialog(null, "Exhausted Rolls! Please Select a suitable category");
		}
		else {
			JOptionPane.showMessageDialog(null, "Game Over");
		}	
	}
}


