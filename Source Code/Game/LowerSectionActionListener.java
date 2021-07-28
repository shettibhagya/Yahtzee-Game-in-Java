package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LowerSectionActionListener implements ActionListener {

	private TrackGame trackGame;
	private ArrayList<Integer> imageSequence;
	private int buttonNo;
	private ArrayList<JTextField> lowerTextFields;
	private ArrayList<Integer> categoriesUsed;
	private ArrayList<JCheckBox> checkBoxList;
	private JLabel turn;
	private JLabel roll;
	
	public LowerSectionActionListener(int buttonNo, 
	TrackGame trackGame, 
	ArrayList<Integer> imageSequence, 
	ArrayList<JTextField> lowerTextFields, 
	ArrayList<Integer> categoriesUsed, 
	ArrayList<JCheckBox> checkBoxList,
	JLabel turn,
	JLabel roll) {
		super();
		this.buttonNo=buttonNo;
		this.trackGame=trackGame;
		this.imageSequence=imageSequence;
		this.lowerTextFields=lowerTextFields;
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
		int lowerEligible=1;
		for(int i:imageSequence) {
			if(categoriesUsed.get(i-1)!=1)
			{
				lowerEligible=0;
			}
		}
		
		if(lowerEligible==1 && usedCount==trackGame.getRoundNumber() && trackGame.getRollNumber()!=0 && categoriesUsed.get(buttonNo-1)==0) {
				for(JCheckBox cb:checkBoxList) {
					cb.setSelected(false);
				}
			
			System.out.print("Round"+(trackGame.getRoundNumber()+1)+"Ended");
			trackGame.setRoundNumber(trackGame.getRoundNumber()+1);
			trackGame.setRollNumber(0);
			int score=0;

			switch(buttonNo) {
				case 7:
					score = calculate3OfAKind(score);
					break;
				case 8:
					score = calculate4OfAKind(score);
					break;
				case 9:
					score = calculateFullHouse(score);
					break;
				case 10:
					score = calculateSmallStraight(score);
					break;
				case 11:
					score = calculateLargeStraight(score);
					break;
				case 12:
					score = calculateYahtzee(score);
					break;
				case 13:
					score = calculateChance(score);
					break;
				default:
					break;
			}	
			
			//calculate yahtzee bonus
			int bonus=Integer.parseInt(lowerTextFields.get(7).getText());
			bonus = yahtzeeBonus(bonus);
			//calculate teh sub total for lower section
			int subTotal=Integer.parseInt(lowerTextFields.get(8).getText())+score;
			System.out.println("==================subtotal"+subTotal+"==========score"+score+"=========bonus"+bonus+"============grandtotal"+(subTotal+bonus));
			// set the text fields to reflect the scores
			lowerTextFields.get(buttonNo-7).setText(score+"");
			lowerTextFields.get(7).setText((bonus)+"");
			lowerTextFields.get(8).setText(subTotal+"");
			lowerTextFields.get(9).setText((subTotal+bonus)+"");
			//set the category
			categoriesUsed.set(buttonNo-1,1);
			turn.setText("Turn:"+ trackGame.getRoundNumber());
			roll.setText("Roll:"+ trackGame.getRollNumber());
		}
		else if(usedCount>=trackGame.getRoundNumber() && trackGame.getRollNumber()==0) {
			JOptionPane.showMessageDialog(null, "Roll The Dice....");
		}
		else if(lowerEligible==0) {
			JOptionPane.showMessageDialog(null, "Select Category available from Upper Section...");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "This Category Has Already Been Used");
		}
	}

	private int calculate3OfAKind(int score) {
		System.out.println("3 Of Kind");
		int kindCount = 0;
		for(int i=0;i<3;i++) {
			kindCount=0;
			for(int k=0;k<5;k++) {
				if(imageSequence.get(i) == imageSequence.get(k)) {
					kindCount++;
				}
				if(kindCount==3) {
					break;
				}
			}
			if(kindCount==3) {
				break;
			}
		}
		if(kindCount==3) {
			for(int i:imageSequence) {
				score+=i;
			}
		}
		return score;
	}

	private int calculate4OfAKind(int score) {
		System.out.println("4 Of Kind");
		int kindCount = 0;
		for(int i=0;i<2;i++) {
			kindCount=0;
			for(int k=0;k<5;k++) {
				if(imageSequence.get(i)==imageSequence.get(k)) {
					kindCount++;
				}
				if(kindCount==4) {
					break;
				}
			}
			if(kindCount==4) {
				break;
			}
		}
		if(kindCount==4) {
			for(int i:imageSequence) {
				score+=i;
			}
		}
		return score;
	}

	private int calculateFullHouse(int score) {
		System.out.println("Full House");
		Set<Integer> imageset=new HashSet<Integer>();
		int kindCount = 0;

		for(int i:imageSequence) {
			imageset.add(i);
		}

		kindCount=imageset.size();

		if(kindCount==2) {
			for(int i=0;i<3;i++) {
				kindCount=0;
				for(int k=0;k<5;k++) {
					if(imageSequence.get(i) == imageSequence.get(k)) {
						kindCount++;
					}
					if(kindCount==4) {
						break;
					}
				}
				if(kindCount==4) {
					break;
				}
			}
			if(kindCount!=4)
				score+=25;
		}
		else
		{
			kindCount=0;
			int num=imageSequence.get(0);
			for(int i:imageSequence) {
				if(i==num) {
					kindCount++;
				}
			}
			if(kindCount==5) {
				score+=25;
			}
		}
		return score;
	}

	private int calculateSmallStraight(int score) {
		System.out.println("Small Straight");
		int kindCount = 0;

		for(int i=1;i<=3;i++) {
			int k=i;
			kindCount=0;
			for(int j=0;j<4;j++) {
				if(imageSequence.contains(k++)) {
					kindCount++;
				}
			}
			if(kindCount==4) {
				score+=30;
				break;
			}	
		}
		if(kindCount!=4)
		{
			kindCount=0;
			int num=imageSequence.get(0);
			for(int i:imageSequence) {
				if(i==num) {
					kindCount++;
				}
			}
			if(kindCount==5) {
				score+=30;
			}
		}
		return score;
	}

	private int calculateLargeStraight(int score) {
		System.out.println("Large Straight");
		int kindCount = 0;

		for(int i=1;i<=2;i++) {
			int k=i;
			kindCount=0;
			for(int j=0;j<5;j++) {
				if(imageSequence.contains(k++)) {
					kindCount++;
				}
			}
			if(kindCount==5) {
				score+=40;
			}
		}
		if(kindCount!=5)
		{
			kindCount=0;
			int num=imageSequence.get(0);
			for(int i:imageSequence) {
				if(i==num) {
					kindCount++;
				}
			}
			if(kindCount==5) {
				score+=30;
			}
		}
		return score;
	}

	private int calculateYahtzee(int score) {
		System.out.println("Yahtzee");
		int kindCount = 0;
		int num=imageSequence.get(0);
		
		for(int i:imageSequence) {
			if(i==num) {
				kindCount++;
			}
		}
		if(kindCount==5) {
			score+=50;
		}
		return score;
	}

	private int calculateChance(int score) {
		System.out.println("Chance");
		for(int i:imageSequence) {
			score+=i;
		}
		return score;
	}

	private int yahtzeeBonus(int bonus) {
		int kindCount = 0;
		if(categoriesUsed.get(11) == 1) {
			System.out.println("Yahtzee");
			int num=imageSequence.get(0);
			for(int i:imageSequence) {
				if(i==num) {
					kindCount++;
				}
			}
			if(kindCount==5) {
				bonus+=100;
			}
		}
		return bonus;
	}

}
