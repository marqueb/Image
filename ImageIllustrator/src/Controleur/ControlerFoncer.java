package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerFoncer implements ActionListener{
	private Controler controler;

	public ControlerFoncer(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.foncer();
	}
}
