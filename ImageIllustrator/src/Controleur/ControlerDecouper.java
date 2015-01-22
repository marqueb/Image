package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerDecouper implements ActionListener{
	Controler controler;
	
	ControlerDecouper(Controler controler){
		this.controler=controler;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controler.decouper();
	}
}
