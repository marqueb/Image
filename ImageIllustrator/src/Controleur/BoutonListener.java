package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.Main;
import Modele.Modele;

public class BoutonListener implements ActionListener{
	private Modele modele;
	
	public BoutonListener(Modele m){
		this.modele=m;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("jambon1)");
		if(e.getActionCommand().equals("Charger")){
			modele.getOutil().Charger(modele.getInterfaceGraphique());
		}
	}
}
