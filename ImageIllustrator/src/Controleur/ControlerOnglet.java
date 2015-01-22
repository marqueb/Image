package Controleur;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;

public class ControlerOnglet implements ChangeListener{
	private Controler controler;

	public ControlerOnglet(Controler controler){
		this.controler=controler;
	}
	
	public void stateChanged(ChangeEvent e) {
		controler.changerOnglet();
	}
}
