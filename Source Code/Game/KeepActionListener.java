package game;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class KeepActionListener implements ItemListener {
	private ArrayList<Integer> activeKeep;
	private int boxNo;
	public KeepActionListener(int boxNo,ArrayList<Integer> activeKeep) {
		this.boxNo=boxNo;
		this.activeKeep=activeKeep;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			activeKeep.set(boxNo,1);
		}
		else
		if(e.getStateChange() == ItemEvent.DESELECTED) {
			activeKeep.set(boxNo,0);
		}
	}
}
