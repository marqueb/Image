package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerCopier implements ActionListener {

	Controler controler;
	public ControlerCopier(Controler controler){
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		controler.getModele().copier();

	}

}
