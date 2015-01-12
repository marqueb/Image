package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;

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
