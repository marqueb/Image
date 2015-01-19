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
		modele.setEstHistoCliquer(!modele.isEstHistoCliquer());
		if(modele.isEstHistoCliquer()){
				modele.calculerHistogrammeRGB();
				modele.setNbAffichageHisto(modele.getNbAffichageHisto()+1);
				System.out.println(modele.getNbAffichageHisto());
		}
		else{
			modele.getInterfaceGraphique().getFrameHisto().dispose();
			modele.setNbAffichageHisto(modele.getNbAffichageHisto()-1);
		}
	}			

}
