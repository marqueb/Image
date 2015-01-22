package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vue.Histogramme;
import Vue.InterfaceGraphique;

public class ControlerCheckCouleur implements ActionListener {

	InterfaceGraphique it;
	Histogramme histo;
	
	public ControlerCheckCouleur(InterfaceGraphique it,Histogramme histo ) {
		this.it=it;
		this.histo=histo;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(it.getRouge().isSelected()){
			histo.setAfficheRouge(true);
		}
		else{
			histo.setAfficheRouge(false);
		}
		
		if(it.getVert().isSelected()){
			histo.setAfficheVert(true);
		}
		else{
			histo.setAfficheVert(false);
		}
		
		if(it.getBleu().isSelected()){
			histo.setAfficheBleu(true);
		}
		else{
			histo.setAfficheBleu(false);
		}

		if(it.getChrominanceV().isSelected()){
			histo.setAfficheChrominanceV(true);
		}
		else{
			histo.setAfficheChrominanceV(false);
		}
		
		if(it.getChrominanceU().isSelected()){
			histo.setAfficheChrominanceU(true);
		}
		else{
			histo.setAfficheChrominanceU(false);
		}
		
		if(it.getLuminance().isSelected()){
			histo.setAfficheLuminence(true);
		}
		else{
			histo.setAfficheLuminence(false);

		}

		histo.repaint();		
		it.getFrameHisto().validate();

	}

}
