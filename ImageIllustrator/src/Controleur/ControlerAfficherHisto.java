package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;

public class ControlerAfficherHisto implements ActionListener {

	Modele modele;
	public ControlerAfficherHisto(Modele modele){
		this.modele=modele;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(modele.getNbAffichageHisto()==0){
			modele.calculerHistogrammeRGB();
			modele.setNbAffichageHisto(modele.getNbAffichageHisto()+1);
		}
	}

}
