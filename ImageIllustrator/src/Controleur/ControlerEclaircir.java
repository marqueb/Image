package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerEclaircir implements ActionListener{
	private Controler controler;

	public ControlerEclaircir(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.eclaircir();
	}
}
