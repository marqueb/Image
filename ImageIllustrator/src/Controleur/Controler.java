package Controleur;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import Modele.Modele;
import Modele.TypeFiltre;
import Vue.CadreImage;
import Vue.InterfaceGraphique;


public class Controler{
	private Modele modele;
	private InterfaceGraphique it;
	private boolean echantillonageActif=false, isRGB;

	public void init(){
		if(echantillonageActif){
			modele.enleverCouleurPixel();
			modele.getInterfaceGraphique().retraitChoixRGB();
		}
		echantillonageActif=false;
	}
	
	public Modele getModele()
	{
		return this.modele;
	}

	public void setModele(Modele modele){
		this.modele=modele;
	}
	
	public void setInterfaceGraphique(InterfaceGraphique i){
		this.it=i;
	}

	public void charger(){
		init();
		modele.charger();
	}

	public void sauvegarder(){
		init();
		modele.sauvegarder();
	}

	public void couleurPixel(){
		init();
		echantillonageActif = true;
		isRGB=true;
		it.affichageChoixRGB();
	}

	public void imaGris(){
		init();
		modele.imagris();
	}	

	public void changerOnglet(){
		init();
	}

	public void fermerOnglet(Object o){
		modele.fermerOnglet(o);
	}

	public void changeSelectionRGB(){
		isRGB=!isRGB;
	}

	public void sourisBouge(int x, int y){
		if(echantillonageActif){
			modele.afficherCouleurPixel(x, y, isRGB);
		}
	}

	public void sourisEntre(int x, int y){
		if(echantillonageActif){
			modele.afficherCouleurPixel(x, y, isRGB);
		}
	}

	public void sourisSort(int x, int y){
		if(echantillonageActif){
			modele.enleverCouleurPixel();
		}
	}

	public void sourisClique(int x, int y){
		
	}
	
	public void boutonFusionClic()
	{
		modele.traiterFusion();
	}
	
	public void sliderFusionChange()
	{
		modele.traiterVariationFusion(it.getSliderFusionValue());
	}
	
	public void boutonAppliquerFusionClic()
	{
		this.it.retirerComponentFusion();
		modele.calculerHistogrammeRGB();
	}
	
	public void boutonAppliquerFiltre()
	{
		this.it.rafraichirComponentOption();
		modele.actualiserImageIcon();
	}

	public void boutonMoyenneurClic()
	{
		//modele.appliquerFiltre(TypeFiltre.MOYENNEUR);
		modele.memoriseImage();
		it.ajouterComponentChoixTailleFiltre(TypeFiltre.MOYENNEUR);
	}
	
	public void addControlerCharger(JMenuItem charger){
		charger.addActionListener(new ControlerCharger(this));
	}
	
	public void addControlerCharger(JButton charger){
		charger.addActionListener(new ControlerCharger(this));
	}

	public void addControlerSauvegarder(JMenuItem sauvegarder){
		sauvegarder.addActionListener(new ControlerSauvegarder(this));
	}
	
	public void addControlerSauvegarder(JButton sauvegarder){
		sauvegarder.addActionListener(new ControlerSauvegarder(this));
	}
	
	public void addControlerCouleurPixel(JMenuItem couleurPixel){
		couleurPixel.addActionListener(new ControlerCouleurPixel(this));
	}

	public void addControlerImagris(JMenuItem imaGris){
		imaGris.addActionListener(new ControlerImagris(this));
	}

	public void addControlerOnglet(JTabbedPane tabbedPane){
		tabbedPane.addChangeListener(new ControlerOnglet(this));
	}

	public void addControlerX(JButton fermer){
		fermer.addActionListener(new ControlerX(this));
	}

	public void addControlerRGB(Checkbox box1){
		box1.addItemListener(new ControlerRGB(this));
	}

	public void addControlerYUV(Checkbox box2){
		box2.addItemListener(new ControlerYUV(this));
	}

	public void addControlerSouris(JLabel image){
		ControlerSouris cs=new ControlerSouris(this);
		image.addMouseListener(cs);
		image.addMouseMotionListener(cs);
	}
	
	public void addControlerFusion(JSlider slider, JButton appliquer){
		ControlerFusion cf = new ControlerFusion(this);
		slider.addChangeListener(cf);
		appliquer.addActionListener(cf);
	}
	
	public void addControlerChoixTailleFiltre(JSlider slider,JButton b, TypeFiltre filtre)
	{
		ControlerChoixTailleFiltre c =new ControlerChoixTailleFiltre(this,b,  filtre);
		slider.addChangeListener(c);
		b.addActionListener(c);
	}
}
