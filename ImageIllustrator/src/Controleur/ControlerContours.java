package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerContours  implements ActionListener{

	Controler controler = null;

	public ControlerContours(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.getModele().rehausserContours();
	}

}
