package Modele;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	private BufferedImage imaAvantTraitement = null;


	private int xPrec=0, yPrec=0, xCour=0, yCour=0, dX, dY, dXscroll, dYscroll, distx1, disty1, distx2, disty2,nbAffichageHisto;
	private boolean estHistoCliquer,estEgalisation;


	public Modele()
	{
		listCadreImage = new ArrayList<CadreImage>();
		listBoutonFermeture = new ArrayList<JButton>();
		outil = new Outil();
		traiteurImage = new TraiteurImage();
		nbAffichageHisto=0;
		estHistoCliquer=false;
		estEgalisation=false;
	}	

	public void charger(){	
		try{
			File monFichier=outil.lectureFichier();
			BufferedImage image =outil.lectureImage(monFichier);
			int index = monFichier.getName().indexOf('.');
			CadreImage cadreImage=outil.initCadre(image, controler);
			cadreImage.setNomFichier(monFichier.getName().substring(0,index));
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
			//interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(cadreImage.getImage()));
		}catch(Exception e){}
	}
	
	
	public boolean isImageVide(){
		return listCadreImage.isEmpty();
	}
	
	public void afficherpos(int x, int y){
		System.out.println(x+" "+y);
	}

	public void sauvegarder(){
		//sauvegarde image 
		if(!listCadreImage.isEmpty())
			outil.sauvegarder(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage());
	}	

	public boolean estDansImage(int x, int y){
		return (x>=0 && x<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getWidth() && y>=0 && y<listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage().getHeight());
	}	


	public void redimensionner( int newlargeur,int newhauteur) {
		CadreImage  cadre = listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex());
			int largeur=cadre.getImage().getWidth();
			int hauteur=cadre.getImage().getHeight();

			BufferedImage res = traiteurImage.redimenssioner(largeur,hauteur,newlargeur,newhauteur, cadre.getImage());
//			cadre.setImageIcon(new ImageIcon(res));
//			JLabel icon=new JLabel(cadre.getImageIcon());
//			cadre.getImageScroller().setViewportView(icon);

//			System.out.println(((JScrollPane)interfaceGraphique.getTabbedPane().getSelectedComponent()).setViewportView(arg0));
			
			actualiserImageIcon(res);
			interfaceGraphique.getFrame().repaint();
			interfaceGraphique.getFrame().validate();
			
			System.out.println(this.getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).toString());
	}
	
	private JPanel createPanelCadreImage(CadreImage cadre)
	{
		JPanel res = new JPanel();
		
		
		
		return res;
	}
	
	public void inverser(){
		int pixel,r,g,b;
		BufferedImage image= cadreImageCourant().getImage();
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){				
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				image.setRGB(i, j,outil.setR(255-r)+outil.setG(255-g)+outil.setB(255-b));
			}
		}
		cadreImageCourant().setImage(image);
		interfaceGraphique.getFrame().repaint();
	}
	
	public void etalement() {
		double ratior=0,ratiob=0,ratiog=0;
		int minr=255,ming=255,minb=255,maxr=0,maxg=0,maxb=0;
		int pixel,r,g,b;
		BufferedImage image= cadreImageCourant().getImage();
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){	
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
		ratior=255/(maxr-minr);
		ratiog=255/(maxg-ming);
		ratiob=255/(maxb-minb);
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){	
				pixel = image.getRGB(i, j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				image.setRGB(i, j,outil.setR((int) ((r-minr)*ratior))+outil.setG((int) ((g-ming)*ratiog))+outil.setB((int) ((b-minb)*ratiob)));
			}
		}
		cadreImageCourant().setImage(image);
		interfaceGraphique.getFrame().repaint();
		  
	}

	public void egalisation (){
		  double ratio;
		  int pixel,r,g,b;
		  int imageCumule[]=new int[256];
		  BufferedImage image= cadreImageCourant().getImage();
		  ratio = 255.0 / (image.getWidth()*image.getHeight());
		  outil.histogrammeCumule(image,imageCumule);
		  for(int i=0;i<image.getWidth();i++){
			  for(int j=0;j<image.getHeight();j++){
				  pixel = image.getRGB(i, j);
				  r=outil.getR(pixel);
				  g=outil.getG(pixel);
				  b=outil.getB(pixel);
				  if(r==255 && b == 255 && g==255)
					  image.setRGB(i, j,outil.setR(255)+outil.setG(255)+outil.setB(255));
				  else
					  image.setRGB(i, j,outil.setR((int) (imageCumule[r]*ratio))+outil.setG((int) (imageCumule[g]*ratio))+outil.setB((int) (imageCumule[b]*ratio)));
			  }
		  }
		  cadreImageCourant().setImage(image);
		  interfaceGraphique.getFrame().repaint();
		  
	
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
		/*CadreImage cadreImage=cadreImageCourant();
		cadreImage.setImage(outil.imagris(cadreImage.getImage(), existeSelection(),selection()));
		actualiserImageIcon();*/
		
		CadreImage tmp=listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex());
		outil.imagris(tmp.getImage(), existeSelection(), selection());
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
		}
	}

	public void validerFusion()
	{
		interfaceGraphique.retirerComponentFusion();
		interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()),outil.getTabyuvHisto(getListCadreImage().get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()));
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
		listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(im);
		actualiserImageIcon();
		interfaceGraphique.rafraichirComponentOption();
	}
	
	public void rehausserContours()
	{
		BufferedImage im = listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage();
		im = traiteurImage.rehausserContours(im, existeSelection(), selection());
		listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).setImage(im);
		actualiserImageIcon();
		interfaceGraphique.rafraichirComponentOption();
	}

	public void calculerHistogrammeRGB()
	{
		interfaceGraphique.ajouterHistoRgb(outil.getTabRgbHisto(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()),outil.getTabyuvHisto(listCadreImage.get(interfaceGraphique.getTabbedPane().getSelectedIndex()).getImage()));
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
		//interfaceGraphique.
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
		return xCour!=xPrec || yCour!=yPrec;
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
		int distx1=dX-xPrec;
		int distx2=xCour-dX;
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
		int disty1=dY-yPrec;
		int disty2=yCour-dY;
		if(y+disty2>image.getHeight()){
			y=image.getHeight()-1-disty2;
		}else{
			if(y-disty1<0){
				y=disty1;
			}
		}
		return y;
	}

	public void selectionne(int x, int y){
		CadreImage cadreImage=cadreImageCourant();
		xCour=x;
		yCour=y;
		if(existeSelection()){
			BufferedImage image=Outil.deepCopy(cadreImage.getImage());
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
			outil.tracer(image,xPrec, yPrec, xCour, yCour);
			cadreImage.setImageIcon(new ImageIcon(image));
			JLabel icon=new JLabel(cadreImage.getImageIcon());
			controler.addControlerSouris(icon);
			x=cadreImage.getImageScroller().getVerticalScrollBar().getValue();
			y=cadreImage.getImageScroller().getHorizontalScrollBar().getValue();
			cadreImage.getImageScroller().setViewportView(icon);
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(x);
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(y);
		}else{
			actualiserImageIcon();
		}
	}
	
	public void ajustementSelection(int x, int y){
		dX=dX-x;
		dY=dY-y;
		xPrec=xPrec-dX;
		yPrec=yPrec-dY;
		selectionne(xCour-dX, yCour-dY);
	}

	public void deplacerScroll(int x, int y){
		CadreImage cadreImage=cadreImageCourant();
		dXscroll=dXscroll-x;
		dYscroll=dYscroll-y;
		if(x<cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(cadreImage.getImageScroller().getHorizontalScrollBar().getValue()-dXscroll);
		}else if(y<cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(cadreImage.getImageScroller().getVerticalScrollBar().getValue()-dYscroll);
		}else if(x>cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(cadreImage.getImageScroller().getHorizontalScrollBar().getValue()-dXscroll);
		}else if(y>cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(cadreImage.getImageScroller().getVerticalScrollBar().getValue()-dYscroll);
		}
	}
	
	public void deplacerScrollAjustement(int x, int y){		
		CadreImage cadreImage=cadreImageCourant();
		dXscroll=dXscroll-x;
		dYscroll=dYscroll-y;
		if(x-distx1<cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue(x-distx1);
		}else if(y-disty1<cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue(y-disty1);
		}else if(x+distx2>cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()){
			cadreImage.getImageScroller().getHorizontalScrollBar().setValue((cadreImage.getImageScroller().getHorizontalScrollBar().getValue())+(x+distx2)-(cadreImage.getImage().getWidth()-cadreImage.getMaxScrollX()+cadreImage.getImageScroller().getHorizontalScrollBar().getValue()));
		}else if(y+disty2>cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()){
			cadreImage.getImageScroller().getVerticalScrollBar().setValue((cadreImage.getImageScroller().getVerticalScrollBar().getValue())+(y+disty2)-(cadreImage.getImage().getHeight()-cadreImage.getMaxScrollY()+cadreImage.getImageScroller().getVerticalScrollBar().getValue()));
		}
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
		dX=x;
		dY=y;
	}
	
	public void setDeltaScroll(int x, int y){
		dXscroll=x;
		dYscroll=y;
	}

	public int getDeltaX(){
		return dX;
	}

	public int getDeltaY(){
		return dY;
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
		xPrec=-1;
		yPrec=-1;
		xCour=-1;
		yCour=-1;
		this.actualiserImageIcon();
	}
	
	public void decouper(){
		if(this.existeSelection()){
			CadreImage cadreImage=cadreImageCourant();
			cadreImage.setImage(outil.decouper(cadreImage.getImage(), selection()));
			actualiserImageIcon();
		}
	}
}


