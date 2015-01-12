package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerMoyenneur implements ActionListener{

	Controler controler = null;

	public ControlerMoyenneur(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.boutonMoyenneurClic();
	}

}
