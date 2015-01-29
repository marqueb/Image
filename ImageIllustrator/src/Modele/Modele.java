package Modele;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;

import Controleur.Controler;
import Vue.CadreImage;
import Vue.InterfaceGraphique;


public class Modele {

	private Controler controler;
	private InterfaceGraphique interfaceGraphique;
	private TraiteurImage traiteurImage;
	private Outil outil;
	private int ongletPrec=-1, ongletCour=-1;;
	private ArrayList<CadreImage> listCadreImage;
	private ArrayList<JButton> listBoutonFermeture;
	private CadreImage cadre_ima_fusion = null;
	private BufferedImage imaAvantTraitement = null;
	private BufferedImage copie;


	private int xPrec=0, yPrec=0, xCour=0, yCour=0,  d1X, d1Y, d2X, d2Y, dXscroll, dYscroll, distx1, disty1, distx2, disty2,nbAffichageHisto;
	private boolean estHistoCliquer,estEgalisation, segmentation;


	public Modele()
	{
		listCadreImage = new ArrayList<CadreImage>();
		listBoutonFermeture = new ArrayList<JButton>();
		outil = new Outil();
		traiteurImage = new TraiteurImage(outil);
		nbAffichageHisto=0;
		estHistoCliquer=false;
		estEgalisation=false;
	}	

	public void charger(){	
		try{
			int couleur;
			File monFichier=outil.lectureFichier();
			BufferedImage image =outil.lectureImage(monFichier);
			int index = monFichier.getName().indexOf('.');
			CadreImage cadreImage=outil.initCadre(image, controler);
			cadreImage.setNomFichier(monFichier.getName().substring(0, index));
			//ajoute le cadre image à la liste de cadre image
			listCadreImage.add(cadreImage);
			interfaceGraphique.getTabbedPane().add(cadreImage.getImageScroller());
			interfaceGraphique.setEnable(true);
			//creer l'onglet en lui affectant le cadre image, le selectionne et affecte le controleur au cadre image, ajoute le bouton creer a liste de bouton
			listBoutonFermeture.add(interfaceGraphique.ajouterOnglet(cadreImage));
			interfaceGraphique.getFrame().validate();
			setScroll(cadreImage);
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(0);
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(0);

		}catch(Exception e){
		}
	}


	public boolean isImageVide(){
		return listCadreImage.isEmpty();
	}

	public void afficherpos(int x, int y){
	}

	public void sauvegarder(){
		//sauvegarde image 
		if(!listCadreImage.isEmpty())
			outil.sauvegarder(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}	

	public boolean estDansImage(int x, int y){
		return (x>=0 && x<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getWidth() && y>=0 && y<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getHeight());
	}	

	public void redimensionner(int newHauteur, int newLargeur) {
		CadreImage  cadre = cadreImageCourant();
		initAnnulerRefaire(cadre);
		BufferedImage intermediaire =traiteurImage.redimenssionerLargeur(cadre.getImage(), newLargeur);
		cadre.setImage(traiteurImage.redimenssionerHauteur(intermediaire, newHauteur));
		actualiserImageIcon();
	}

	public void redimensionnerIntelligement(int newhauteur,int newlargeur) {
		CadreImage  cadre = listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		initAnnulerRefaire(cadre);
		int largeur=cadre.getImage().getWidth();
		int hauteur=cadre.getImage().getHeight();
		cadre.setImage(traiteurImage.redimensionnerIntelligement(largeur, hauteur, newlargeur, newhauteur,cadre.getImage()));
		actualiserImageIcon();
	}

	private JPanel createPanelCadreImage(CadreImage cadre)
	{
		JPanel res = new JPanel();



		return res;
	}

	public void inverser(){

		initAnnulerRefaire(cadreImageCourant());
		int pixel,r,g,b,i_deb, i_fin, j_deb, j_fin;
		BufferedImage image= cadreImageCourant().getImage();
		if(existeSelection()){
			int[] selection=selection();
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
		}else{
			i_deb=0;
			i_fin=image.getWidth();
			j_deb=0;
			j_fin=image.getHeight();
		}
		for (int i=i_deb;i<i_fin;i++){
			for (int j=j_deb;j<j_fin;j++){		
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				image.setRGB(i, j,outil.setR(255-r)+outil.setG(255-g)+outil.setB(255-b)+outil.setAlpha(255));
			}
		}
		cadreImageCourant().setImage(image);
		actualiserImageIcon();
	}

	public void etalement() {
		double ratior=0,ratiob=0,ratiog=0;
		int i_deb,i_fin,j_deb,j_fin;
		int minr=255,ming=255,minb=255,maxr=0,maxg=0,maxb=0;
		int pixel,r,g,b;
		initAnnulerRefaire(cadreImageCourant());
		BufferedImage image= cadreImageCourant().getImage();
		if(existeSelection()){
			int[] selection=selection();
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
		}else{
			i_deb=0;
			i_fin=image.getWidth();
			j_deb=0;
			j_fin=image.getHeight();
		}
		for (int i=i_deb;i<i_fin;i++){
			for (int j=j_deb;j<j_fin;j++){	
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				if(r>maxr)
					maxr=r;
				if(g>maxg)
					maxg=g;
				if(b>maxb)
					maxb=b;
				if(r<minr)
					minr=r;
				if(g<ming)
					ming=g;
				if(b<minb)
					minb=b;
			}
		}
		if(maxr-minr<=0)
			ratior=255;			
		else
			ratior=255/(maxr-minr);
		if(maxg-ming<=0)
			ratiog=255;
		else
			ratiog=255/(maxg-ming);
		if(maxb-minb<=0)
			ratiob=255;
		else
			ratiob=255/(maxb-minb);
		for (int i=i_deb;i<i_fin;i++){
			for (int j=j_deb;j<j_fin;j++){	
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				image.setRGB(i, j,outil.setR((int) ((r-minr)*ratior))+outil.setG((int) ((g-ming)*ratiog))+outil.setB((int) ((b-minb)*ratiob))+outil.setAlpha(255));
			}
		}
		cadreImageCourant().setImage(image);
		actualiserImageIcon();

	}

	public void egalisation (){

		double ratio;
		int pixel,r,g,b,i_deb,i_fin,j_deb,j_fin;
		int imageCumule[]=new int[256];
		initAnnulerRefaire(cadreImageCourant());
		BufferedImage image= cadreImageCourant().getImage();

		outil.histogrammeCumule(image,imageCumule);
		if(existeSelection()){
			int[] selection=selection();
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
		}else{
			i_deb=0;
			i_fin=image.getWidth();
			j_deb=0;
			j_fin=image.getHeight();
		}
		ratio = 255.0 / ((i_fin-i_deb)*(j_fin-j_deb));
		for (int i=i_deb;i<i_fin;i++){
			for (int j=j_deb;j<j_fin;j++){	
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				if(r==255 && b == 255 && g==255)
					image.setRGB(i, j,outil.setR(255)+outil.setG(255)+outil.setB(255)+outil.setAlpha(255));
				else
					image.setRGB(i, j,outil.setR((int) (imageCumule[r]*ratio))+outil.setG((int) (imageCumule[g]*ratio))+outil.setB((int) (imageCumule[b]*ratio))+outil.setAlpha(255));
			}
		}
		actualiserImageIcon();
	}


	public void afficherCouleurPixel(int x, int y, boolean isRGB){
		//recupere la valeur du pixel en fonction de l'image et des coordonnées
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
		CadreImage cadreImage=cadreImageCourant();
		initAnnulerRefaire(cadreImage);
		outil.imagris(cadreImage.getImage(), existeSelection(), selection());
		actualiserImageIcon();
	}

	public void eclaircir(){
		CadreImage cadreImage=cadreImageCourant();
		initAnnulerRefaire(cadreImage);
		outil.eclaircir(cadreImage.getImage(), existeSelection(), selection());
		actualiserImageIcon();
	}

	public void foncer(){
		CadreImage cadreImage=cadreImageCourant();
		initAnnulerRefaire(cadreImage);
		outil.foncer(cadreImage.getImage(), existeSelection(), selection());
		actualiserImageIcon();
	}

	public void noirblanc(){
		CadreImage cadreImage=cadreImageCourant();
		initAnnulerRefaire(cadreImage);
		outil.noirblanc(cadreImage.getImage(), existeSelection(), selection());
		actualiserImageIcon();
	}

	public void sepia() {
		CadreImage cadreImage=cadreImageCourant();
		initAnnulerRefaire(cadreImage);
		outil.sepia(cadreImage.getImage(), existeSelection(), selection());
		actualiserImageIcon();		
	}

	public void fermerOnglet(Object j){
		//cherche l'index de l'onglet à l'aide de la table de bouton
		int i = listBoutonFermeture.indexOf(j);
		//supprime le cadre de la liste de cadre
		suppCadreImage(i);
		if(listCadreImage.isEmpty()){
			interfaceGraphique.setEnable(false);
			interfaceGraphique.getAfficherHisto().setVisible(false);
			interfaceGraphique.getFrame().validate();
			interfaceGraphique.getRedimensionner().setEnabled(false);
			interfaceGraphique.getEgalisation().setEnabled(false);
			interfaceGraphique.getEtalement().setEnabled(false);
			interfaceGraphique.getInverser().setEnabled(false);

		}
		if(nbAffichageHisto>0){
			interfaceGraphique.getFrameHisto().dispose();
			//setNbAffichageHisto(getNbAffichageHisto()-1);
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
			imaAvantTraitement = Outil.deepCopy(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
			interfaceGraphique.ajouterComponentFusion(cadre_ima_fusion);
			initAnnulerRefaire(cadreImageCourant());
		}else{
			controler.setFusionActive(false);
		}
	}

	public void validerFusion()
	{
		//interfaceGraphique.getPanelOption().remove(1);
	}

	//appel� lorsqu'on change le pourcentage d'image avec le scroll
	public void traiterVariationFusion(int pourcentImageSecondaire)
	{
		BufferedImage imaPrincipale = this.imaAvantTraitement;
		BufferedImage imaSecondaire = cadre_ima_fusion.getImage();
		BufferedImage imaToChange = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		float coef1 = (float) ((100.0-pourcentImageSecondaire)/100.0);
		float coef2 = (float) (pourcentImageSecondaire/100.0);
		float rapportLargeur, rapportHauteur;
		int[] selection = selection();

		// redimensionner les images 
		if(existeSelection())
		{
			rapportLargeur = ((float)selection[2]-selection[0])/(float)imaSecondaire.getWidth();
			rapportHauteur = ((float)selection[3]-selection[1])/(float)imaSecondaire.getHeight();
		}
		else
		{
			rapportLargeur = ((float)imaPrincipale.getWidth())/(float)imaSecondaire.getWidth();
			rapportHauteur = ((float)imaPrincipale.getHeight())/(float)imaSecondaire.getHeight();
		}
		if(rapportLargeur<rapportHauteur)
		{
			imaSecondaire = Outil.resize(imaSecondaire, (int)(((float)imaSecondaire.getWidth())*rapportLargeur), (int)(((float)imaSecondaire.getHeight())*rapportLargeur));
		}
		else
		{
			imaSecondaire = Outil.resize(imaSecondaire, (int)(((float)imaSecondaire.getWidth())*rapportHauteur), (int)(((float)imaSecondaire.getHeight())*rapportHauteur));
		}


		float valR = 0, valG = 0, valB = 0;
		int rgb1 = 0, rgb2 = 0, newRgb = 0;
		int borneX = imaPrincipale.getWidth()<imaSecondaire.getWidth()?imaPrincipale.getWidth():imaSecondaire.getWidth();
		int borneY = imaPrincipale.getHeight()<imaSecondaire.getHeight()?imaPrincipale.getHeight():imaSecondaire.getHeight();
		int i_deb, i_fin, j_deb, j_fin;

		//borne de la fusion
		if(existeSelection()){
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
			if(i_fin>i_deb + borneX) i_fin = i_deb + borneX;
			if(j_fin>j_deb + borneY) j_fin = j_deb + borneY;
		}else{
			i_deb=0;
			i_fin=borneX;
			j_deb=0;
			j_fin=borneY;
		}

		for(int i=i_deb; i<i_fin; i++)
		{
			for(int j=j_deb; j<j_fin; j++)
			{
				rgb1 = imaPrincipale.getRGB(i, j);
				rgb2 = imaSecondaire.getRGB(i-i_deb, j-j_deb);

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
			taille = taille*2+1;
			filtre = FiltreConvolution.createFiltreMoyenne(taille);
			break;
		case GAUSSIEN:
			taille = taille*2+1;
			filtre = FiltreConvolution.createFiltreGaussien(taille, 5);
			break;
		case MEDIAN:
			filtre = null;
			break;
		}

		//appliquer convolution
		if(filtre!=null)
		{
			appliquerFiltre(filtre, this.imaAvantTraitement);
		}
		else if(typeFiltre == TypeFiltre.MEDIAN && taille>0)
		{
			BufferedImage res = traiteurImage.convoluerFiltreMedian(imaAvantTraitement, taille, existeSelection(), selection());
			listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		}

		actualiserImageIcon();
	}

	public void traiterChangementTailleFiltre(TypeFiltre typeFiltre, int taille)
	{
		float[][] filtre = null;
		//cr�er filtre
		switch (typeFiltre){
		case MOYENNEUR:
			taille = taille*2+1;
			filtre = FiltreConvolution.createFiltreMoyenne(taille);
			break;
		case GAUSSIEN:
			taille = taille*2+1;
			filtre = FiltreConvolution.createFiltreGaussien(taille, 5);
			break;
		case MEDIAN:
			filtre = null;
			break;
		}

		//appliquer convolution
		if(filtre!=null)
		{
			appliquerFiltre(filtre, this.imaAvantTraitement);
		}
		else if(typeFiltre == TypeFiltre.MEDIAN && taille>0)
		{
			BufferedImage res = traiteurImage.convoluerFiltreMedian(imaAvantTraitement, taille, existeSelection(), selection());
			listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		}
		actualiserImageIcon();
	}

	public void rehausserContrastes()
	{
		BufferedImage im = listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		im = traiteurImage.convoluer(FiltreConvolution.getNoyauContraste3x3(), im, existeSelection(), selection());
		initAnnulerRefaire(cadreImageCourant());
		listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(im);
		actualiserImageIcon();
		interfaceGraphique.rafraichirComponentOption();
	}

	public void rehausserContours()
	{
		BufferedImage im = listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		im = traiteurImage.rehausserContours(im, existeSelection(), selection());
		listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(im);
		initAnnulerRefaire(cadreImageCourant());
		actualiserImageIcon();
		interfaceGraphique.rafraichirComponentOption();
	}

	public void calculerHistogrammeRGB()
	{
		interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage(), this.existeSelection(), this.selection()),outil.getTabyuvHisto(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage(), this.existeSelection(), this.selection()));
	}

	public void appliquerFiltre(float[][] noyau)
	{
		BufferedImage bufImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		BufferedImage res = traiteurImage.convoluer(noyau, bufImage, existeSelection(), selection());
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void appliquerFiltre(float[][] noyau, BufferedImage bufImage)
	{
		BufferedImage res = traiteurImage.convoluer(noyau, bufImage, existeSelection(), selection());
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(res);
		getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).repaint();
	}

	public void memoriseImage()
	{
		this.imaAvantTraitement = Outil.deepCopy(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
		initAnnulerRefaire(cadreImageCourant());
	}

	public void actualiserImageIcon(){
		CadreImage cadreImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		cadreImage.setImageIcon(new ImageIcon(cadreImage.getImage()));
		cadreImage.getLabelImage().setIcon(cadreImage.getImageIcon());
		/*cadreImage.setLabelImage(labelImage);
		JLabel icon=new JLabel(cadreImage.getImageIcon());
		cadreImage.setImageScroller(new JScrollPane(icon));
		controler.addControlerSouris(icon);*/
		int x=cadreImage.getImageScroller().getVerticalScrollBar().getValue();
		int y=cadreImage.getImageScroller().getHorizontalScrollBar().getValue();
		cadreImage.getImageScroller().setViewportView(cadreImage.getLabelImage());
		cadreImage.getImageScroller().getVerticalScrollBar().setValue(x);
		cadreImage.getImageScroller().getHorizontalScrollBar().setValue(y);
		//interfaceGraphique.getFrame().validate();
		interfaceGraphique.getTabbedPane().setComponentAt(interfaceGraphique.getTabbedPane().getSelectedIndex(), cadreImage.getImageScroller());
	}

	public void actualiserImageIcon(BufferedImage im){
		CadreImage cadreImage = getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		cadreImage.setImage(im);
		cadreImage.setImageIcon(new ImageIcon(im));
		JLabel icon=new JLabel(cadreImage.getImageIcon());
		controler.addControlerSouris(icon);
		int x=cadreImage.getImageScroller().getVerticalScrollBar().getValue();
		int y=cadreImage.getImageScroller().getHorizontalScrollBar().getValue();
		cadreImage.getImageScroller().setViewportView(icon);
		cadreImage.getImageScroller().getVerticalScrollBar().setValue(x);
		cadreImage.getImageScroller().getHorizontalScrollBar().setValue(y);
		//interfaceGraphique.
	}

	public boolean existeSelection(){
		return xCour!=xPrec && yCour!=yPrec;
	}

	public int[] selection(){
		int[] points=new int[4];
		points[0]=xPrec;
		points[1]=yPrec;
		points[2]=xCour;
		points[3]=yCour;
		return points;
	}

	public int ajustementX(int x){
		BufferedImage image =cadreImageCourant().getImage();
		if(x>image.getWidth()){
			x=image.getWidth()-1;
		}else{
			if(x<0){
				x=0;
			}
		}
		return x;
	}

	public int ajustementY(int y){
		BufferedImage image =cadreImageCourant().getImage();
		if(y>image.getHeight()){
			y=image.getHeight()-1;
		}else{
			if(y<0){
				y=0;
			}
		}
		return y;
	}

	public int ajustementSelectionX(int x){
		BufferedImage image =cadreImageCourant().getImage();
		if(x+distx2>image.getWidth()){
			x=image.getWidth()-1-distx2;
		}else{
			if(x-distx1<0){
				x=distx1;
			}
		}
		return x;
	}

	public int ajustementSelectionY(int y){
		BufferedImage image =cadreImageCourant().getImage();
		if(y+disty2>image.getHeight()){
			y=image.getHeight()-1-disty2;
		}else{
			if(y-disty1<0){
				y=disty1;
			}
		}
		return y;
	}

	public void selectionne(int x, int y, boolean deplacementScroll){
		CadreImage cadreImage=cadreImageCourant();
		if(deplacementScroll){
			if(x<cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
				x=xCour-1;
			}else if(y<cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
				y=yCour-1;
			}else if(x>cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
				x=xCour+1;
			}else if(y>cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
				y=yCour+1;
			}
		}
		xCour=x;
		yCour=y;
		int a, b, c, d;
		if(existeSelection()){
			interfaceGraphique.getCopier().setEnabled(true);
			interfaceGraphique.getCouper().setEnabled(true);
			interfaceGraphique.getDecouper().setEnabled(true);
			BufferedImage image=Outil.deepCopy(cadreImage.getImage());
			if(xPrec>xCour){
				a=xCour;
				c=xPrec;
			}else{
				a=xPrec;
				c=xCour;
			}
			if(yPrec>yCour){
				b=yCour;
				d=yPrec;
			}else{
				b=yPrec;
				d=yCour;
			}
			outil.tracer(image,a, b, c, d);
			cadreImage.getLabelImage().setIcon(new ImageIcon(image));
			x=cadreImage.getImageScroller().getVerticalScrollBar().getValue();
			y=cadreImage.getImageScroller().getHorizontalScrollBar().getValue();
			cadreImage.getImageScroller().setViewportView(cadreImage.getLabelImage());
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(x);
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(y);
		}else{
			interfaceGraphique.getCopier().setEnabled(false);
			interfaceGraphique.getCouper().setEnabled(false);
			interfaceGraphique.getDecouper().setEnabled(false);
			actualiserImageIcon();
		}
	}

	public void ajustementSelection(int x, int y){
		xPrec=xPrec+d2X;
		yPrec=yPrec+d2Y;
		xCour=xCour+d2X;
		yCour=yCour+d2Y;
		d2X=d1X-x;
		d2Y=d1Y-y;
		xPrec=xPrec-d2X;
		yPrec=yPrec-d2Y;
		selectionne(xCour-d2X, yCour-d2Y, false);
	}

	public void deplacerScroll(int x, int y){
		CadreImage cadreImage=cadreImageCourant();
		dXscroll=dXscroll-x;
		dYscroll=dYscroll-y;
		if(x<cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(cadreImage.getImageScroller().getHorizontalScrollBar().getValue()-1);
		}if(y<cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(cadreImage.getImageScroller().getVerticalScrollBar().getValue()-1);
		}if(x>cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(cadreImage.getImageScroller().getHorizontalScrollBar().getValue()+1);
		}if(y>cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(cadreImage.getImageScroller().getVerticalScrollBar().getValue()+1);
		}
	}

	public void deplacerScrollAjustement(int x, int y){		
		CadreImage cadreImage=cadreImageCourant();
		dXscroll=dXscroll-x;
		dYscroll=dYscroll-y;
		if(x-distx1<cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(x-distx1);
		}if(y-disty1<cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(y-disty1);
		}if(x+distx2>cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue((cadreImage.getImageScroller().getHorizontalScrollBar().getValue())+(x+distx2)-(cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()));
		}if(y+disty2>cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue((cadreImage.getImageScroller().getVerticalScrollBar().getValue())+(y+disty2)-(cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()));
		}
	}
	
	public int getOngletPrecedent(){
		return ongletPrec;
	}

	public boolean isEstEgalisation() {
		return estEgalisation;
	}

	public void setEstEgalisation(boolean estEgalisation) {
		this.estEgalisation = estEgalisation;
	}

	public boolean estDansSelection(int x, int y){
		return x>xPrec && x<xCour && y>yPrec && y<yCour;
	}

	public BufferedImage calculerConvolution(float[][] filtre, BufferedImage im)
	{
		return traiteurImage.convoluer(filtre, im, existeSelection(), selection());
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
	public boolean isEstHistoCliquer() {
		return estHistoCliquer;
	}

	public void setEstHistoCliquer(boolean estHistoCliquer) {
		this.estHistoCliquer = estHistoCliquer;
	}

	public CadreImage cadreImageCourant(){
		return listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex());
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

	public BufferedImage getImaAvantTraitement() {
		return imaAvantTraitement;
	}

	public void setImaAvantTraitement(BufferedImage imaAvantTraitement) {
		this.imaAvantTraitement = imaAvantTraitement;
	}

	public ArrayList<CadreImage> getListCadreImage() {
		return listCadreImage;
	}

	public void setPrec(int x, int y){
		xPrec=x;
		yPrec=y;
	}

	public void setDelta(int x, int y){
		d1X=x;
		d1Y=y;
	}

	public void setDeltaScroll(int x, int y){
		dXscroll=x;
		dYscroll=y;
	}

	public int getDeltaX(){
		return d1X;
	}

	public int getDeltaY(){
		return d1Y;
	}

	public int getDelta2X(){
		return d2X;
	}

	public int getDelta2Y(){
		return d2Y;
	}

	public int getNbAffichageHisto() {
		return nbAffichageHisto;
	}
	public void fermetureHisto(){
		setNbAffichageHisto(getNbAffichageHisto()-1);
	}
	public void setNbAffichageHisto(int nbAffichageHisto) {
		this.nbAffichageHisto = nbAffichageHisto;
	}

	public void setScroll(CadreImage cadreImage){
		cadreImage.setMaxScrollX(cadreImage.getImageScroller().getHorizontalScrollBar().getValue());
		cadreImage.setMaxScrollY(cadreImage.getImageScroller().getVerticalScrollBar().getValue());
	}

	public int largeurImageVisible(){
		CadreImage cadreImage=cadreImageCourant();
		return cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX();
	}

	public int hauteurImageVisible(){
		CadreImage cadreImage=cadreImageCourant();
		return cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY();
	}

	public void setDist(int x, int y){
		distx1=x-xPrec;
		disty1=y-yPrec;
		distx2=xCour-x;
		disty2=yCour-y;
	}

	public void annulerSelection(){
		controler.setSelectionActive(false);
		xPrec=-1;
		yPrec=-1;
		xCour=-1;
		yCour=-1;
		d2X=0;
		d2Y=0;
		this.actualiserImageIcon();
	}

	public void decouper(){
		if(this.existeSelection()){
			CadreImage cadreImage=cadreImageCourant();
			initAnnulerRefaire(cadreImage);
			cadreImage.setImage(outil.decouper(cadreImage.getImage(), selection()));
		}
	}

	public void annuler(){
		CadreImage cadreImage=cadreImageCourant();
		BufferedImage image = outil.deepCopy(cadreImage.getImage());
		cadreImage.getRefaire().add(image);
		cadreImage.setImage(cadreImage.getAnnuler().remove(cadreImage.getAnnuler().size()-1));
		if(cadreImage.getAnnuler().isEmpty()){
			interfaceGraphique.getAnnuler().setEnabled(false);
			interfaceGraphique.getBtnAnnuler().setEnabled(false);
		}
		interfaceGraphique.getRefaire().setEnabled(true);
		interfaceGraphique.getBtnRefaire().setEnabled(true);
		actualiserImageIcon();
	}

	public void refaire(){
		CadreImage cadreImage=cadreImageCourant();
		BufferedImage image = outil.deepCopy(cadreImage.getImage());
		cadreImage.getAnnuler().add(image);
		cadreImage.setImage(cadreImage.getRefaire().remove(cadreImage.getRefaire().size()-1));
		if(cadreImage.getRefaire().isEmpty()){
			interfaceGraphique.getRefaire().setEnabled(false);
			interfaceGraphique.getBtnRefaire().setEnabled(false);
		}
		interfaceGraphique.getAnnuler().setEnabled(true);
		interfaceGraphique.getBtnAnnuler().setEnabled(true);
		actualiserImageIcon();
	}

	public void initAnnulerRefaire(CadreImage cadreImage){
		BufferedImage image = outil.deepCopy(cadreImage.getImage());
		cadreImage.getAnnuler().add(image);
		interfaceGraphique.getAnnuler().setEnabled(true);
		interfaceGraphique.getBtnAnnuler().setEnabled(true);
		interfaceGraphique.getRefaire().setEnabled(false);
		interfaceGraphique.getBtnRefaire().setEnabled(false);
		cadreImage.getRefaire().clear();
	}

	public void majSelection(int x, int y){
		xPrec=x-distx1;
		yPrec=y-disty1;
		xCour=x+distx2;
		yCour=y+disty2;
		d2X=0;
		d2Y=0;
	}

	public void majSelection2(){

		if(xPrec>xCour){
			int tmp=xPrec;
			xPrec=xCour;
			xCour=tmp;
		}
		if(yPrec>yCour){
			int tmp=yPrec;
			yPrec=yCour;
			yCour=tmp;
		}
	}

	public void remplirInit(Mat fg, int width, int height) {
		byte[] data;
	data = new byte[height * width *3];
		fg.get(0, 0, data);
	for(int i = 0; i <  height * width*3; i=i+1)
		{			
			data[i]=2;
		}
		fg.put(0, 0, data);		
	}

	public void remplirMatrice(Mat fg, int ancienx, int ancieny, int x, int y, int height, int width, boolean background) {
		byte[] data;
		data = new byte[height * width * 3];
		fg.get(0, 0, data);
		int largeur=0, hauteur=0;
		for(int i = 0; i < height * width*3; i=i+1)
		{		
			if(!background){
				if((ancienx>=x) && (ancieny>=y)) {	
					if((largeur>=x && largeur<=ancienx) && (hauteur>=y && hauteur<=ancieny)){				
						data[i]=1;

					}
				}
				else if((ancienx>=x) && (ancieny<=y)) {
					if((largeur>=x && largeur<=ancienx) && (hauteur>=ancieny && hauteur<=y)){
						data[i]=1;
					}
				}
				else if((ancienx<=x) && (ancieny>=y)) {
					if((largeur>=ancienx && largeur<=x) && (hauteur>=y && hauteur<=ancieny)){
						data[i]=1;
					}
				}
				else if((ancienx<=x) && (ancieny<=y)) {
					if((largeur>=ancienx && largeur<=x) && (hauteur>=ancieny && hauteur<=y)){
						data[i]=1;

					}
				}
			}
			else{
				if((ancienx>=x) && (ancieny>=y)) {	
					if((largeur>=x && largeur<=ancienx) && (hauteur>=y && hauteur<=ancieny)){				
						data[i]=0;

					}
				}
				else if((ancienx>=x) && (ancieny<=y)) {
					if((largeur>=x && largeur<=ancienx) && (hauteur>=ancieny && hauteur<=y)){
						data[i]=0;
					}
				}
				else if((ancienx<=x) && (ancieny>=y)) {
					if((largeur>=ancienx && largeur<=x) && (hauteur>=y && hauteur<=ancieny)){
						data[i]=0;
					}
				}
				else if((ancienx<=x) && (ancieny<=y)) {
					if((largeur>=ancienx && largeur<=x) && (hauteur>=ancieny && hauteur<=y)){
						data[i]=0;

					}
				}
			}


			largeur++;
			if(largeur==cadreImageCourant().getImage().getWidth()){
				largeur=0;
				hauteur++;
			}	

		}
		fg.put(0, 0, data);		
	}
	
	public void copier(){
		if(existeSelection()){
			initAnnulerRefaire(cadreImageCourant());
			interfaceGraphique.getColler().setEnabled(true);
			CadreImage cadreImage = cadreImageCourant();
			int[] selection=selection();
			copie=new BufferedImage(selection[2]-selection[0], selection[3]-selection[1],BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<selection[2]-selection[0]; i++){
				for(int j=0; j<selection[3]-selection[1]; j++){
					copie.setRGB(i, j, cadreImage.getImage().getRGB(i+selection[0], j+selection[1]));
				}
			}
		}
	}

	public void couper(){
		if(existeSelection()){
			initAnnulerRefaire(cadreImageCourant());
			CadreImage cadreImage = cadreImageCourant();
			interfaceGraphique.getColler().setEnabled(true);
			int[] selection=selection();
			copie=new BufferedImage(selection[2]-selection[0], selection[3]-selection[1],BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<selection[2]-selection[0]; i++){
				for(int j=0; j<selection[3]-selection[1]; j++){
					copie.setRGB(i, j, cadreImage.getImage().getRGB(i+selection[0], j+selection[1]));
					cadreImage.getImage().setRGB(i+selection[0], j+selection[1], outil.setAlpha(255)+outil.setR(255)+outil.setG(255)+outil.setB(255));
				}
			}
			actualiserImageIcon();
		}
	}

	public void coller(){

		if(copie!=null){
			initAnnulerRefaire(cadreImageCourant());
			CadreImage cadreImage = cadreImageCourant();
			int i=0;
			while(i<copie.getWidth() && i+xPrec<cadreImage.getImage().getWidth()){
				int j=0;
				while(j<copie.getHeight() && j+yPrec<cadreImage.getImage().getHeight()){
					cadreImage.getImage().setRGB(i+xPrec, j+yPrec, copie.getRGB(i, j));
					j++;
				}
				i++;
			}
		}
		actualiserImageIcon();
	}

	public int getOngletPrec() {
		return ongletPrec;
	}

	public void setOngletPrec(int ongletPrec) {
		this.ongletPrec = ongletPrec;
	}

	public int getOngletCour() {
		return ongletCour;
	}

	public void setOngletCour(int ongletCour) {
		this.ongletCour = ongletCour;
	}
}


