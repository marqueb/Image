package Controleur;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JMenuItem;

public class ControlerYUV implements ItemListener{
	private Controler controler;

	public ControlerYUV(Controler controler){
		this.controler=controler;
	}
	
	public void itemStateChanged(ItemEvent e){
		controler.changeSelectionRGB();
	}
}