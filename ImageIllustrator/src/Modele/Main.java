package Modele;
import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import Controleur.Controler;
import Vue.InterfaceGraphique;

public class Main {
	public static void main(String [] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If name is not available, you can set the GUI to another look and feel.
		}
		
		//Création des trois threads (modele, vue, controler)
		Modele modele = new Modele();
		Controler controler = new Controler();
		InterfaceGraphique it = new InterfaceGraphique(modele, controler);
		
		//liaison des trois entitées
		controler.setInterfaceGraphique(it);
		controler.setModele(modele);
		modele.setControler(controler);
		modele.setInterfaceGraphique(it);
		
		//lancement de l'interface graphique
		EventQueue.invokeLater(it);
		
	}
}
