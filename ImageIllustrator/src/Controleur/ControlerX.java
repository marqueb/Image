package Controleur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class ControlerX implements ActionListener{
	private Controler controler;

	public ControlerX(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.fermerOnglet(e.getSource());
	}
}
