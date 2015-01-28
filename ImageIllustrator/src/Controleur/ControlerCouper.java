package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerCouper implements ActionListener {
	Controler controler;
	
	ControlerCouper(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e) {
		controler.couper();
	}
}
