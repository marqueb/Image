package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;



public class ControlerEtalement implements ActionListener {
	Modele modele;

	public ControlerEtalement(Modele modele){
		this.modele = modele;

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.etalement();

	}

}
