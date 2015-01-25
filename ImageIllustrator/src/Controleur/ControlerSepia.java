package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerSepia implements ActionListener {
	
	Controler controle;
	public ControlerSepia(Controler controle){
		this.controle=controle;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controle.getModele().sepia();
	}

}
