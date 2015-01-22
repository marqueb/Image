package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vue.InterfaceGraphique;

public class ControlerRedimessionner implements ActionListener {

	InterfaceGraphique it;
	
	public ControlerRedimessionner(InterfaceGraphique it){
		this.it=it;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		it.redimensionner();
	}

}