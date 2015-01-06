/*package Controleur;

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
		switch(e.getActionCommand()){
			case "Charger":
				modele.getOutil().Charger(modele.getInterfaceGraphique());
			break;
			case "Couleur pixel":
				
				//parametre : bufferedimage de l'onglet courant
				modele.getOutil().CouleurPixel(modele.getListCadreImage().get(modele.getInterfaceGraphique().getTabbedPane().getSelectedIndex()));
			break;
		}
	}
}
*/