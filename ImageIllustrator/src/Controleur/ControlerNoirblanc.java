package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerNoirblanc implements ActionListener{
	private Controler controler;

	public ControlerNoirblanc(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.noirblanc();
	}
}
