package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Modele.Modele;
import Modele.TypeFiltre;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Controler extends MouseMotionAdapter implements MouseListener, ActionListener, ChangeListener{

	private Modele modele;
	private InterfaceGraphique it;
	private boolean echantillonageActif=false, estDansImage=false, fermeOnglet=false;
	private boolean fermerOnglet=false;


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
	public void actionPerformed(ActionEvent e) {/*
		if(e.getActionCommand().equals("Charger")){
			System.out.println("jambon1");
			modele.charger();
		}else if(e.getActionCommand().equals("Sauvegarde")){
			modele.sauvegarder();
		}else if(e.getActionCommand().equals("Couleur pixel")){
			echantillonageActif = true;
		}else if(e.getActionCommand().equals("X")){
			modele.fermerOnglet(e.getSource());
		}else if(e.getActionCommand().equals("Image grise")){
			modele.imagris();
		}*/
		switch(e.getActionCommand()){
			case "Charger":
				modele.charger();
			break;
			case "Moyenneur (flouter)":
				modele.appliquerFiltre(TypeFiltre.MOYENNEUR);
			break;
			case "Sauvegarde":
				modele.sauvegarder();
			break;
			case "Couleur pixel":
				//autorise l'ecoute à la souris
				echantillonageActif = true;
			break;
			case "X":	
				modele.fermerOnglet(e.getSource());
			break;
			case "Image grise":
				modele.imagris();
			break;
		}
	}
	//utiliser pour relever les coordonnées du pixel a evalué
	public void mouseMoved(MouseEvent e){
		if(estDansImage){
			//releve la valeur du pixel en fonction des coordonnées
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			modele.couleurPixel(e.getX(), e.getY());
		}
	}
	
	public void mouseEntered(MouseEvent e){
		if(echantillonageActif){
			estDansImage=true;
		}
	}
	
	public void mouseExited(MouseEvent e){
		if(echantillonageActif){
			estDansImage=false;
			modele.enleverCouleurPixel();
		}
	}	

	@Override
	public void stateChanged(ChangeEvent e) {
		if(echantillonageActif){
			echantillonageActif = false;
			modele.enleverCouleurPixel();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
