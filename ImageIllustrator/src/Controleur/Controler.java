package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Modele.Modele;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Controler extends MouseMotionAdapter implements ActionListener, ChangeListener{

	private Modele modele;
	private InterfaceGraphique it;
	private boolean echantillonageActif=false;


	public Modele getModele() {
		return modele;
	}

	public void setModele(Modele modele) {
		this.modele = modele;
	}

	public InterfaceGraphique getInterfaceGraphique() {
		return it;
	}

	public void setInterfaceGraphique(InterfaceGraphique interfaceGraphique) {
		this.it = interfaceGraphique;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "Charger":
				modele.charger();
			break;
			case "Couleur pixel":
				//autorise l'ecoute à la souris
				echantillonageActif = true;
			break;
			case "X":
				int closeTabNumber = it.getTabbedPane().getSelectedIndex();
				it.getTabbedPane().removeTabAt(closeTabNumber);
			break;
		}
	}
	//utiliser pour relever les coordonnées du pixel a evalué
	public void mouseMoved(MouseEvent e){
		if(echantillonageActif){
			//releve la valeur du pixel en fonction des coordonnées
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			modele.couleurPixel(e.getX(), e.getY());
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		/*
		//System.out.println("salut");
		//System.out.println(e.getSource());
		if(fermerOnglet){
			//System.out.println(it.getTabbedPane().getTabComponentAt(0));
			//System.out.println(it.getTabbedPane().getTabComponentAt(1));
		}*/
		if(echantillonageActif){
				echantillonageActif = false;
				modele.enleverCouleurPixel();
			}
	}
}
