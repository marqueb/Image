package Controleur;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ControlerYUV implements ItemListener{
	private Controler controler;

	public ControlerYUV(Controler controler){
		this.controler=controler;
	}
	
	public void itemStateChanged(ItemEvent e){
		controler.changeSelectionRGB();
	}
}
