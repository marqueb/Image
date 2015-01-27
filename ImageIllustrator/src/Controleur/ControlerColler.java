package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerColler implements ActionListener {

	Controler controler;
	public ControlerColler(Controler controler){
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controler.getModele().coller();

	}

}
