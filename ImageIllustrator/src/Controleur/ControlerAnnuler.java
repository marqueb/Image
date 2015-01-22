package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerAnnuler implements ActionListener{

	Controler controler;
	
	ControlerAnnuler(Controler controler){
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controler.annuler2();		
	}
}
