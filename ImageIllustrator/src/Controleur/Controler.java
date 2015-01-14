package Controleur;

import java.awt.Checkbox;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import Modele.Modele;
import Modele.TypeFiltre;
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
		if(!modele.getListCadreImage().isEmpty())	it.rafraichirComponentOption();
	}
	
	public void annuler()
	{
		modele.getListCadreImage().get(it.getTabbedPane().getSelectedIndex()).setImage(modele.getImaAvantTraitement());
		modele.actualiserImageIcon();
		it.rafraichirComponentOption();
	}
	
	public void valider()
	{
		modele.actualiserImageIcon();
		it.rafraichirComponentOption();
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
		if(!modele.isImageVide()){
		init();
		modele.sauvegarder();
		}
	}

	public void couleurPixel(){
		if(!modele.isImageVide()){
		init();
		echantillonageActif = true;
		isRGB=true;
		it.affichageChoixRGB();
		}
	}

	public void imaGris(){
		if(!modele.isImageVide()){
		init();
		modele.imagris();
		}
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

	public void boutonFlouterClic()
	{
		//modele.appliquerFiltre(TypeFiltre.MOYENNEUR);
		modele.memoriseImage();
		it.ajouterComponentChoixTailleFiltre(TypeFiltre.GAUSSIEN);
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
	
	public void addControlerChoixTailleFiltre(JComboBox<String> boxTaille, JButton annuler, JButton b, TypeFiltre filtre, JComboBox<String> typeFlou)
	{
		ControlerChoixTailleFiltre c =new ControlerChoixTailleFiltre(this, filtre, typeFlou, boxTaille);
		if(typeFlou!=null) typeFlou.addActionListener(c);
		annuler.addActionListener(c);
		boxTaille.addActionListener(c);
		b.addActionListener(c);
	}
	
	public void addControlerMedian(JMenuItem m)
	{
		m.addActionListener(new ControlerMedian(this.modele, this.it));
	}
	
	public void addControlerBoutonFiltreUser(JMenuItem u)
	{
		u.addActionListener(new ControlerBoutonFiltreUser(this.modele, this.it));
	}
	
	public void addControlerFiltreUser(JButton valider, JButton annuler, JComboBox<String> boxChoixTailleFiltre, JPanel panelUser)
	{
		ControlerFiltreUser c = new ControlerFiltreUser(this.it, this.modele, boxChoixTailleFiltre, panelUser);
		
		annuler.addActionListener(c);
		valider.addActionListener(c);
		boxChoixTailleFiltre.addActionListener(c);
	}
	
	public void addControlerContraste(JMenuItem contraste)
	{
		contraste.addActionListener(new ControlerContraste(this));
	}
	
	public void addControlerContours(JMenuItem contraste)
	{
		contraste.addActionListener(new ControlerContours(this));
	}
}
