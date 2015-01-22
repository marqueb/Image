package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerSauvegarder implements ActionListener{
	private Controler controler;

	public ControlerSauvegarder(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.sauvegarder();
	}
}
