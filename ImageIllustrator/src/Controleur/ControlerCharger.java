package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerCharger implements ActionListener{
	private Controler controler;

	public ControlerCharger(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.charger();
	}
}
