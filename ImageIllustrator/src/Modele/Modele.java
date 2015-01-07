package Modele;



import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import Controleur.Controler;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Modele {

	private Controler controler;
	private InterfaceGraphique interfaceGraphique;
	private TraiteurImage traiteurImage;
	private Outil outil;

	private ArrayList<CadreImage> listCadreImage;
	private ArrayList<JButton> listBoutonFermeture;

	public Modele()
	{
		listCadreImage = new ArrayList<CadreImage>();
		listBoutonFermeture = new ArrayList<JButton>();
		outil = new Outil();
		traiteurImage = new TraiteurImage();
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
	
	public void suppCadreImage(int index){
		listCadreImage.remove(index);
	}
	
	public ArrayList<CadreImage> getListCadreImage() {
		return listCadreImage;
	}

	public void setListCadreImage(ArrayList<CadreImage> listCadreImage) {
		this.listCadreImage = listCadreImage;
	}

	public void charger(){
		//charge l'image et l'insert dans cadre image
		CadreImage cadreImage=outil.charger(interfaceGraphique);
		if(cadreImage != null)
		{
			//BEGIN TEST{ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//			int[][] noyau = {{1,1,1},{1,1,1},{1,1,1}};
			//			cadreImage = this.traiteurImage.convoluer(noyau, cadreImage, ModeConvolution.SAME);
			// }END TEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//ajoute le cadre image à la liste de cadre image
			listCadreImage.add(cadreImage);
			//creer l'onglet en lui affectant le cadre image, le selectionne et affecte le controleur au cadre image, ajoute le bouton creer a liste de bouton
			listBoutonFermeture.add(interfaceGraphique.ajouterOnglet(cadreImage));
			cadreImage.repaint();
		}

	}

	public void sauvegarder(){
		//sauvegarde image 
		if(!listCadreImage.isEmpty())
			outil.sauvegarder(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}	
	
	public void couleurPixel(int x, int y){
		if(x>=0 && x<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getWidth() && y>=0 && y<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getHeight()){
			//recupere la valeur du pixel en fonction de l'image et des coordonnées
			int couleur = outil.couleurPixel(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage(), x, y);
			//calcul et affiche les differentes intensités de couleur en fonction de la valeur du pixel
			interfaceGraphique.afficherValeurCouleur(x, y, outil.getR(couleur), outil.getG(couleur), outil.getR(couleur));
		}else{
			enleverCouleurPixel();
		}
	}

	public void enleverCouleurPixel(){
		interfaceGraphique.enleverCouleurPixel();
	}

	public void appliquerFiltre(TypeFiltre filtre)
	{
		switch(filtre){
		case MOYENNEUR:
			int[][] noyau = {{3,3,3},{3,3,3},{3,3,3}};
			BufferedImage bufImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
			BufferedImage res = traiteurImage.convoluer(noyau, bufImage);
			getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
			getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
		break;

		}
	}


	
	public void fermerOnglet(Object j){
		//cherche l'index de l'onglet à l'aide de la table de bouton
		int i = listBoutonFermeture.indexOf(j);
		//supprime le cadre de la liste de cadre
		suppCadreImage(i);
		//supprime le bouton de la liste de bouton
		listBoutonFermeture.remove(i);
		//supprime l'onglet correspondant
		interfaceGraphique.getTabbedPane().removeTabAt(i);
	}
}
