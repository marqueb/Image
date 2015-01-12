package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.JMenuItem;
import java.awt.Checkbox;
import javax.swing.JTabbedPane;
import javax.swing.JButton;

public class Controler{
	private Modele modele;

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
	
	/*public void setInterfaceGraphique(Modele modele){
		this.modele=modele;
	}*/

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
		modele.getInterfaceGraphique().affichageChoixRGB();
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
}
