package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerFlouter implements ActionListener{

	Controler controler = null;

	public ControlerFlouter(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.boutonFlouterClic();
	}

}
