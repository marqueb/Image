package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlerFusion implements ActionListener, ChangeListener{
	
	Controler controler = null;
	
	public ControlerFusion(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.boutonAppliquerFusionClic();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		controler.sliderFusionChange();
	}
	
	
}
