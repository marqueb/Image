package Modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;

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
	private CadreImage cadre_ima_fusion = null;



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
			interfaceGraphique.setEnableSauvegarde(true);
			//ajoute le cadre image à la liste de cadre image
			listCadreImage.add(cadreImage);
			//creer l'onglet en lui affectant le cadre image, le selectionne et affecte le controleur au cadre image, ajoute le bouton creer a liste de bouton
			listBoutonFermeture.add(interfaceGraphique.ajouterOnglet(cadreImage));
			cadreImage.repaint();
			
			interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(cadreImage.getImage()));
		}
	}

	public void sauvegarder(){
		//sauvegarde image 
		if(!listCadreImage.isEmpty())
			outil.sauvegarder(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}		

	public void couleurPixel(int x, int y, boolean isRGB){
		if(x>=0 && x<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getWidth() && y>=0 && y<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getHeight()){
			//recupere la valeur du pixel en fonction de l'image et des coordonnées
			int couleur = outil.couleurPixel(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage(), x, y);
			//calcul et affiche les differentes intensités de couleur en fonction de la valeur du pixel
			int r=outil.getR(couleur);
			int g=outil.getG(couleur);
			int b=outil.getB(couleur);
			if(isRGB){
				interfaceGraphique.afficherValeurCouleur(x, y, r, g, b);
			}else{
				double yp=outil.getY(r, g, b);
				double u=outil.getU(b, yp);
				double v=outil.getV(r, yp);
				interfaceGraphique.afficherYUVCouleur(x, y, Math.round(yp), Math.round(u), Math.round(v));
			}
		}else{
			enleverCouleurPixel();
		}
	}

	public void imagris(){
		outil.imagris(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
		listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void enleverCouleurPixel(){
		interfaceGraphique.enleverCouleurPixel();
	}

	public void appliquerFiltre(TypeFiltre filtre)
	{
		BufferedImage bufImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		BufferedImage res = traiteurImage.convoluer(filtre, bufImage);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void fermerOnglet(Object j){
		//cherche l'index de l'onglet à l'aide de la table de bouton
		int i = listBoutonFermeture.indexOf(j);
		//supprime le cadre de la liste de cadre
		suppCadreImage(i);
		if(listCadreImage.isEmpty()){
			interfaceGraphique.setEnableSauvegarde(false);
		}
		//supprime le bouton de la liste de bouton
		listBoutonFermeture.remove(i);
		//supprime l'onglet correspondant
		interfaceGraphique.getTabbedPane().removeTabAt(i);
	}
	
	public void traiterFusion()
	{
		cadre_ima_fusion = outil.charger(interfaceGraphique);
		if(cadre_ima_fusion!=null){
			interfaceGraphique.ajouterComponentFusion(cadre_ima_fusion);
		}
	}
	
}
