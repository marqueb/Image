package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Modele;
import Vue.InterfaceGraphique;

public class Controler implements ActionListener{
	
	private Modele modele;
	private InterfaceGraphique interfaceGraphique;
	private BoutonListener boutonListener;
	
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Charger")){
			modele.getOutil().Charger(interfaceGraphique);
		}		
		else
			System.out.println("jambon3");
	}
}
