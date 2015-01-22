package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;

public class ControlerRedimensionnerValider implements ActionListener {

	Modele modele;
	
	public ControlerRedimensionnerValider(Modele modele){
		this.modele=modele;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.redimensionner(Integer.parseInt(modele.getInterfaceGraphique().getLargeur().getText()),Integer.parseInt(modele.getInterfaceGraphique().getHauteur().getText()));
		modele.getInterfaceGraphique().getFrameRedim().dispose();
	}

}