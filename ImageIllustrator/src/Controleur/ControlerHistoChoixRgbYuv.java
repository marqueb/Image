package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vue.Histogramme;
import Vue.InterfaceGraphique;

public class ControlerHistoChoixRgbYuv implements ActionListener {


	InterfaceGraphique it;
	Histogramme histo;
	
	public ControlerHistoChoixRgbYuv(InterfaceGraphique it,Histogramme histo ) {
		this.it=it;
		this.histo=histo;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(it.getChoixRgbYuv().getSelectedIndex()==0){
			it.getRouge().setVisible(true);
			it.getVert().setVisible(true);
			it.getBleu().setVisible(true);
			it.getLuminance().setVisible(false);
			it.getChrominanceU().setVisible(false);
			it.getChrominanceV().setVisible(false);
		}
		else{
			it.getLuminance().setVisible(true);
			it.getChrominanceU().setVisible(true);
			it.getChrominanceV().setVisible(true);
			it.getRouge().setVisible(false);
			it.getVert().setVisible(false);
			it.getBleu().setVisible(false);
			
		}
		it.getHisto().repaint();
		it.getFrameHisto().validate();

	}

}
