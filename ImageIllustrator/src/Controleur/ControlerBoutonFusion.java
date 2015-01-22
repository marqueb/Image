package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerBoutonFusion implements ActionListener{

	Controler controler = null;

	public ControlerBoutonFusion(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.boutonFusionClic();
	}

}
