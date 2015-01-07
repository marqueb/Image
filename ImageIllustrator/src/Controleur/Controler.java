package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;

import Modele.Modele;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Controler extends MouseMotionAdapter implements ActionListener{

	private Modele modele;
	private InterfaceGraphique it;
	//private BoutonListener boutonListener;
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
				
				/*
				//charge l'image et l'insert dans cadre image
				CadreImage cadreImage=modele.getOutil().Charger(it);
				//creer l'onglet en lui affectant le cadre image, le selectionne et affecte le controleur au cadre image
				it.ajouterOnglet(cadreImage);
	    		cadreImage.repaint();*/
			break;
			case "Couleur pixel":
				//autorise l'ecoute à la souris
				echantillonageActif = true;
			break;
		}
	}
	
	//utiliser pour relever les coordonnées du pixel a evalué
	public void mouseMoved(MouseEvent e){
		if(echantillonageActif){
			//releve la valeur du pixel en fonction des coordonnées
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			modele.couleurPixel(e.getX(), e.getY());
			/*int couleur = modele.getOutil().CouleurPixel(modele.getListCadreImage().get(modele.getInterfaceGraphique().getTabbedPane().getSelectedIndex()).getImage(), e.getX(), e.getY());
			//calcul et affiche les differentes intensités de couleur en fonction de la valeur du pixel
			it.afficherValeurCouleur(couleur, e.getX(), e.getY());*/
		}
	}
	/*
	public void mouseClicked(MouseEvent e){
		System.out.println("salut");
	}*/
}
