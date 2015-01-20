package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;

public class ControlerInverser implements ActionListener {

	Modele modele;
	public ControlerInverser(Modele modele){
			this.modele=modele;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		modele.inverser();

	}

}
