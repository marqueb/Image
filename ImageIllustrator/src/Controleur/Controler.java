package Controleur;

import Modele.Modele;
import Vue.InterfaceGraphique;

public class Controler {
	
	private Modele modele;
	private InterfaceGraphique interfaceGraphique;

	BoutonListener boutonListener;
	
	public Modele getModele() {
		return modele;
	}

	public void setModele(Modele modele) {
		this.modele = modele;
	}

	public InterfaceGraphique getInterfaceGraphique() {
		return interfaceGraphique;
	}

	public void setInterfaceGraphique(InterfaceGraphique interfaceGraphique) {
		this.interfaceGraphique = interfaceGraphique;
	}

	
	//listener pour chaque bouton du menu
	//mouse listener combiner à un etat (exemple decouper)
}
