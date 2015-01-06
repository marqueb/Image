package Modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Controleur.Controler;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Modele {
	
	private Controler controler;
	private InterfaceGraphique interfaceGraphique;
	private TraiteurImage traiteurImage;
	private Outil outil;
	
	private ArrayList<CadreImage> listCadreImage;
	
	public Modele()
	{
		listCadreImage = new ArrayList<CadreImage>();
		outil = new Outil();
	}	
	
	public Outil getOutil() {
		return outil;
	}

	public void setOutil(Outil outil) {
		this.outil = outil;
	}

	public TraiteurImage getTraiteurImage() {
		return traiteurImage;
	}

	public void setTraiteurImage(TraiteurImage traiteurImage) {
		this.traiteurImage = traiteurImage;
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
	
	public void addCadreImage(CadreImage cadreImage){
		listCadreImage.add(cadreImage);
	}

	public ArrayList<CadreImage> getListCadreImage() {
		return listCadreImage;
	}

	public void setListCadreImage(ArrayList<CadreImage> listCadreImage) {
		this.listCadreImage = listCadreImage;
	}
}
