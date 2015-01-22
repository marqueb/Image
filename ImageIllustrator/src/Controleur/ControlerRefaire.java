package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerRefaire implements ActionListener{

	Controler controler;
	
	ControlerRefaire(Controler controler){
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controler.refaire();		
	}
}
