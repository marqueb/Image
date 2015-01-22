package Controleur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class ControlerImagris implements ActionListener{
	private Controler controler;

	public ControlerImagris(Controler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e){
		controler.imaGris();
	}
}
