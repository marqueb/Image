
package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;

public class ControlerEgalisation implements ActionListener {

	Modele modele;
	public ControlerEgalisation(Modele modele){
		this.modele=modele;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.egalisation();
		modele.setEstEgalisation(true);
		if(modele.isEstHistoCliquer()){
			modele.getInterfaceGraphique().getFrameHisto().dispose();
			modele.calculerHistogrammeRGB();
		}
	}

}

