package Modele;
import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import Controleur.Controler;
import Vue.InterfaceGraphique;

public class Main {
	private static Modele modele;
	private static Controler controler;
	private static InterfaceGraphique it;
	
	public static Modele getModele() {
		return modele;
	}

	public static void setModele(Modele modele) {
		Main.modele = modele;
	}

	public static Controler getControler() {
		return controler;
	}

	public static void setControler(Controler controler) {
		Main.controler = controler;
	}

	public static InterfaceGraphique getIt() {
		return it;
	}

	public static void setIt(InterfaceGraphique it) {
		Main.it = it;
	}
	
	public static void main(String [] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If name is not available, you can set the GUI to another look and feel.
		}
		
		//Création des trois threads (modele, vue, controler)
		modele = new Modele();
		controler = new Controler();
		
		//liaison des trois entitées
		controler.setModele(modele);
		modele.setControler(controler);

		it = new InterfaceGraphique(modele, controler);
		modele.setInterfaceGraphique(it);
		controler.setInterfaceGraphique(it);
		
		//lancement de l'interface graphique
		EventQueue.invokeLater(it);
		
	}
}
