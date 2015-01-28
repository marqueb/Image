package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerColler implements ActionListener {
	Controler controler;
	
	ControlerColler(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e) {
		controler.coller();
	}

}
