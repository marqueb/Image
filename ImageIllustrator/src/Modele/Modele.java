package Modele;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controleur.*;
import Vue.*;


public class Modele {

	private Controler controler;
	private InterfaceGraphique interfaceGraphique;
	private TraiteurImage traiteurImage;
	private Outil outil;

	private ArrayList<CadreImage> listCadreImage;
	private ArrayList<JButton> listBoutonFermeture;
	private CadreImage cadre_ima_fusion = null;
	private BufferedImage imaAvantFusion = null;


	public Modele()
	{
		listCadreImage = new ArrayList<CadreImage>();
		listBoutonFermeture = new ArrayList<JButton>();
		outil = new Outil();
		traiteurImage = new TraiteurImage();
	}	

	public void charger(){	
		try{
			File monFichier=outil.lectureFichier();
			BufferedImage image =outil.lectureImage(monFichier);

			int index = monFichier.getName().indexOf('.');
			//charge l'image et l'insert dans cadre image
			//			CadreImage cadreImage=outil.charger();
			//			if(cadreImage != null)
			CadreImage cadreImage=outil.initCadre(image, controler);
			cadreImage.setNomFichier(monFichier.getName().substring(0,index));
			interfaceGraphique.getTabbedPane().add(cadreImage.getImageScroller());
			//this.initCadre(image);
			interfaceGraphique.setEnableSauvegarde(true);
			//ajoute le cadre image à la liste de cadre image
			listCadreImage.add(cadreImage);
			//creer l'onglet en lui affectant le cadre image, le selectionne et affecte le controleur au cadre image, ajoute le bouton creer a liste de bouton
			listBoutonFermeture.add(interfaceGraphique.ajouterOnglet(cadreImage));
			//controler.addControlerSouris(cadreImage);
			//cadreImage.repaint();
			//interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(cadreImage.getImage()));

		}catch(Exception e){}
	}

	public void sauvegarder(){
		//sauvegarde image 
		if(!listCadreImage.isEmpty())
			outil.sauvegarder(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}	

	public boolean estDansImage(int x, int y){
		return (x>=0 && x<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getWidth() && y>=0 && y<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getHeight());
	}	

	public void afficherCouleurPixel(int x, int y, boolean isRGB){
		//recupere la valeur du pixel en fonction de l'image et des coordonnées
		//System.out.println("taille liste "+listImage.size()+" onglet selectionné "+interfaceGraphique.getTabbedPane().getSelectedIndex());
		int couleur = outil.couleurPixel(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage(), x, y);
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
	}

	public void enleverCouleurPixel(){
		interfaceGraphique.enleverCouleurPixel();
	}

	public void imagris(){
		CadreImage tmp=listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		outil.imagris(tmp.getImage());
		actualiserImageIcon();
		interfaceGraphique.getFrame().validate();
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

	//appel� lorsqu'on appuie sur le bouton fusion du menu
	public void traiterFusion()
	{
		cadre_ima_fusion = outil.charger();
		if(cadre_ima_fusion!=null){
			imaAvantFusion = Outil.deepCopy(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
			interfaceGraphique.ajouterComponentFusion(cadre_ima_fusion);
		}
	}

	public void validerFusion()
	{
		interfaceGraphique.retirerComponentFusion();
		interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()));
	}

	//appel� lorsqu'on change le pourcentage d'image avec le scroll
	public void traiterVariationFusion(int pourcentImageSecondaire)
	{
		BufferedImage imaPrincipale = this.imaAvantFusion;
		BufferedImage imaSecondaire = cadre_ima_fusion.getImage();
		BufferedImage imaToChange = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		float coef1 = (float) ((100.0-pourcentImageSecondaire)/100.0);
		float coef2 = (float) (pourcentImageSecondaire/100.0);


		//TODO redimensionner les images pour qu'elles aient les m�me dimensions ou trouver une autre solution

		float valR = 0, valG = 0, valB = 0;
		int rgb1 = 0, rgb2 = 0, newRgb = 0;
		int borneX = imaPrincipale.getWidth()<imaSecondaire.getWidth()?imaPrincipale.getWidth():imaSecondaire.getWidth();
		int borneY = imaPrincipale.getHeight()<imaSecondaire.getHeight()?imaPrincipale.getHeight():imaSecondaire.getHeight();

		for(int i=0; i<borneX; i++)
		{
			for(int j=0; j<borneY; j++)
			{
				rgb1 = imaPrincipale.getRGB(i, j);
				rgb2 = imaSecondaire.getRGB(i, j);

				valR = ((float)outil.getR(rgb1))*coef1;
				valR += ((float)outil.getR(rgb2))*coef2;

				valG = ((float)outil.getG(rgb1))*coef1;
				valG += ((float)outil.getG(rgb2))*coef2;

				valB = ((float)outil.getB(rgb1))*coef1;
				valB += ((float)outil.getB(rgb2))*coef2;

				newRgb = outil.setR((int)valR)+outil.setG((int)valG)+outil.setB((int)valB);

				imaToChange.setRGB(i, j, newRgb);
			}
		}

		actualiserImageIcon();
		interfaceGraphique.getFrame().repaint();
	}

	public void traiterChangementTailleFiltre(TypeFiltre typeFiltre)
	{
		int taille = interfaceGraphique.getSliderChoixTailleFiltreValue();
		float[][] filtre = null;
		//cr�er filtre
		switch (typeFiltre){
		case MOYENNEUR:
			filtre = FiltreConvolution.createFiltreMoyenne(taille);
			break;
		}

		//appliquer convolution
		if(filtre!=null) appliquerFiltre(filtre, this.imaAvantFusion);

		actualiserImageIcon();
	}

	public void calculerHistogrammeRGB()
	{
		interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()));
	}

	public void appliquerFiltre(TypeFiltre filtre)
	{
		BufferedImage bufImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		BufferedImage res = traiteurImage.convoluer(filtre, bufImage);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void appliquerFiltre(float[][] noyau)
	{
		BufferedImage bufImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		BufferedImage res = traiteurImage.convoluer(noyau, bufImage);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void appliquerFiltre(float[][] noyau, BufferedImage bufImage)
	{
		BufferedImage res = traiteurImage.convoluer(noyau, bufImage);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void memoriseImage()
	{
		this.imaAvantFusion = Outil.deepCopy(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}

	public void actualiserImageIcon(){
		CadreImage cadreImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		BufferedImage i = cadreImage.getImage();
		cadreImage.setImageIcon(new ImageIcon(i));
		JLabel icon=new JLabel(cadreImage.getImageIcon());
		controler.addControlerSouris(icon);
		int x=cadreImage.getImageScroller().getVerticalScrollBar().getValue();
		int y=cadreImage.getImageScroller().getHorizontalScrollBar().getValue();
		cadreImage.getImageScroller().setViewportView(icon);
		cadreImage.getImageScroller().getVerticalScrollBar().setValue(x);
		cadreImage.getImageScroller().getHorizontalScrollBar().setValue(y);
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

	public void addImage(CadreImage image){
		listCadreImage.add(image);
	}

	public void suppCadreImage(int index){
		listCadreImage.remove(index);
	}

	public ArrayList<CadreImage> getListImage() {
		return listCadreImage;
	}

	public void setListCadreImage(ArrayList<CadreImage> listCadreImage) {
		this.listCadreImage = listCadreImage;
	}

	public ArrayList<JButton> getListBoutonFermeture() {
		return listBoutonFermeture;
	}

	public void setListBoutonFermeture(ArrayList<JButton> listBoutonFermeture) {
		this.listBoutonFermeture = listBoutonFermeture;
	}

	public CadreImage getCadre_ima_fusion() {
		return cadre_ima_fusion;
	}

	public void setCadre_ima_fusion(CadreImage cadre_ima_fusion) {
		this.cadre_ima_fusion = cadre_ima_fusion;
	}

	public BufferedImage getImaAvantFusion() {
		return imaAvantFusion;
	}

	public void setImaAvantFusion(BufferedImage imaAvantFusion) {
		this.imaAvantFusion = imaAvantFusion;
	}

	public ArrayList<CadreImage> getListCadreImage() {
		return listCadreImage;
	}
}
