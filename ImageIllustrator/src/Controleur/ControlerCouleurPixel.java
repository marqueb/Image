package Controleur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class ControlerCouleurPixel implements ActionListener{
	private Controler controler;

	public ControlerCouleurPixel(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.couleurPixel();
	}
}
