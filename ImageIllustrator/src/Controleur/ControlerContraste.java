package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerContraste implements ActionListener{

	Controler controler = null;

	public ControlerContraste(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.getModele().rehausserContrastes();
	}

}
