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
		modele.getControler().init();
		modele.getControler().setFlouActive(true);
		it.ajouterComponentChoixTailleFiltre(TypeFiltre.MEDIAN);
	}

}
