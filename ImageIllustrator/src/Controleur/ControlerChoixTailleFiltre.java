package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Modele.TypeFiltre;

public class ControlerChoixTailleFiltre implements ActionListener, ChangeListener{
	private Controler controler;
	private TypeFiltre typeFiltre;

	public ControlerChoixTailleFiltre(Controler controler, JButton appliquer, TypeFiltre filtre){
		this.controler=controler;
		this.typeFiltre = filtre;
	}
	
	public void stateChanged(ChangeEvent arg0)
	{
		controler.getModele().traiterChangementTailleFiltre(typeFiltre);
	}

	public void actionPerformed(ActionEvent e) {
		controler.boutonAppliquerFiltre();
	}
}