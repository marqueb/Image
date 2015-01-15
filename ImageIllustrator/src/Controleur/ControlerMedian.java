package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;
import Modele.TypeFiltre;
import Vue.InterfaceGraphique;

public class ControlerMedian implements ActionListener{

	Modele modele = null;
	InterfaceGraphique it = null;

	public ControlerMedian(Modele m, InterfaceGraphique i)
	{
		modele = m;
		it = i;
	}

	public void actionPerformed(ActionEvent e) {
		modele.memoriseImage();
		it.ajouterComponentChoixTailleFiltre(TypeFiltre.MEDIAN);
	}

}
