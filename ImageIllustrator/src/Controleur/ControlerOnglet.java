package Controleur;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlerOnglet implements ChangeListener{
	private Controler controler;

	public ControlerOnglet(Controler controler){
		this.controler=controler;
	}
	
	public void stateChanged(ChangeEvent e) {
		controler.changerOnglet();
	}
}
