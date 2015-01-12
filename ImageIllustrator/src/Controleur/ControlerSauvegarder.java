package Controleur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class ControlerSauvegarder implements ActionListener{
	private Controler controler;

	public ControlerSauvegarder(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.sauvegarder();
	}
}
