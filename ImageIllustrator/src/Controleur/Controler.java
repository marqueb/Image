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
	private boolean fermerOnglet=false

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
				fermerOnglet=true;
				/*int closeTabNumber = it.getTabbedPane().getSelectedIndex();
				it.getTabbedPane().removeTabAt(closeTabNumber);*/
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
	
	
	/*
	public void mouseClicked(MouseEvent e){
		System.out.println("salut");
	}*/

	@Override
	public void stateChanged(ChangeEvent e) {
		if(fermerOnglet){
			
		}
			if(echantillonageActif){
				echantillonageActif = false;
				modele.enleverCouleurPixel();
			}
	}
}
