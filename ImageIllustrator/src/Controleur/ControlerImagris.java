package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlerImagris implements ActionListener{
	private Controler controler;

	public ControlerImagris(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.imaGris();
	}
}
