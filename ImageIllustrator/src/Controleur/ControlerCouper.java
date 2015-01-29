package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerCouper implements ActionListener {

	Controler controler;
	public ControlerCouper(Controler controler){
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controler.getModele().couper();

	}
}
