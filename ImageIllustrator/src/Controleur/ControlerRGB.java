package Controleur;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JMenuItem;

public class ControlerRGB implements ItemListener{
	private Controler controler;

	public ControlerRGB(Controler controler){
		this.controler=controler;
	}
	
	public void itemStateChanged(ItemEvent e){
		controler.changeSelectionRGB();
	}
}
