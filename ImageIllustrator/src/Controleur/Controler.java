package Controleur;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
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
/*		//si on bouge le jslider de la fusion:
		else if(fusionActive && e.getSource() instanceof JSlider)
		{
			System.out.println(((JSlider)e.getSource()).getValue());
			modele.traiterVariationFusion(((JSlider)e.getSource()).getValue());
		}*/
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

	public void boutonMoyenneurClic()
	{
		modele.appliquerFiltre(TypeFiltre.MOYENNEUR);
	}
	
	public void addControlerCharger(JMenuItem charger){
		charger.addActionListener(new ControlerCharger(this));
	}

	public void addControlerSauvegarder(JMenuItem sauvegarder){
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

	public void addControlerSouris(CadreImage cadreImage){
		ControlerSouris cs=new ControlerSouris(this);
		cadreImage.addMouseListener(cs);
		cadreImage.addMouseMotionListener(cs);
	}
	
	public void addControlerFusion(JSlider slider, JButton appliquer){
		ControlerFusion cf = new ControlerFusion(this);
		slider.addChangeListener(cf);
		appliquer.addActionListener(cf);
		
	}
}
