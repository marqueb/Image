package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerCopier implements ActionListener {
	Controler controler;
	
	ControlerCopier(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		controler.copier();
	}

}
