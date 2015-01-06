package Modele;

import Controleur.Controler;
import Vue.InterfaceGraphique;

public class Modele {
	
	Controler controler;
	InterfaceGraphique interfaceGraphique;
	
	public Modele()
	{
		
	}

	public Controler getControler() {
		return controler;
	}

	public void setControler(Controler controler) {
		this.controler = controler;
	}

	public InterfaceGraphique getInterfaceGraphique() {
		return interfaceGraphique;
	}

	public void setInterfaceGraphique(InterfaceGraphique interfaceGraphique) {
		this.interfaceGraphique = interfaceGraphique;
	}

}
