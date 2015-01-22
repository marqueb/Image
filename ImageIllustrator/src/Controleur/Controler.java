package Controleur;

import java.awt.Checkbox;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import Modele.Modele;
import Modele.Outil;
import Modele.TypeFiltre;
import Vue.InterfaceGraphique;


public class Controler{
	private Modele modele;
	private InterfaceGraphique it;
	private boolean echantillonageActif=false,flouActive=false, fusionActive=false, selectionActive=false, ajustementSelection=false, deplacementScroll=false, isRGB;

	public boolean selectionActive()
	{
		return selectionActive;
	}
	
	public void init(){
		if(echantillonageActif){
			modele.enleverCouleurPixel();
			modele.getInterfaceGraphique().retraitChoixRGB();
		}
		if(modele.isImageVide()){
			modele.getInterfaceGraphique().setEnable(false);
		}
		echantillonageActif=false;
		selectionActive=false;
		it.getButtonSegmenter().setEnabled(false);
		ajustementSelection=false;
		deplacementScroll=false;
		if(flouActive){
			it.retirerComponent();
			flouActive=false;
		}
		if(fusionActive){
			it.retirerComponent();
			fusionActive=false;
		}
		if(!modele.getListCadreImage().isEmpty()){	
			it.rafraichirComponentOption();
		}
	}

	public void annuler()
	{
		modele.getListCadreImage().get(it.getTabbedPane().getSelectedIndex()).setImage(modele.getImaAvantTraitement());
		modele.actualiserImageIcon();
		it.rafraichirComponentOption();
		init();
	}

	public void valider()
	{
		modele.actualiserImageIcon();
		it.rafraichirComponentOption();
		init();
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
	public void addControlerCheckCouleur(JCheckBox rouge,JCheckBox vert, JCheckBox bleu,JCheckBox luminence, JCheckBox chrominenceU,JCheckBox chrominenceV){
		rouge.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
		vert.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
		bleu.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
		luminence.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
		chrominenceU.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
		chrominenceV.addActionListener(new ControlerCheckCouleur(it,it.getHisto()));
	}

	public void addControlerHistoChoixRgbYuv(JComboBox choixRgbYuv){
		choixRgbYuv.addActionListener(new ControlerHistoChoixRgbYuv(it,it.getHisto()));
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
		init();
		modele.imagris();
	}	

	public void changerOnglet(){
		init();
		if(modele.existeSelection()){
			//modele.annulerSelection();
		}
	}

	public void fermerOnglet(Object o){
		modele.fermerOnglet(o);
	}

	public void changeSelectionRGB(){
		isRGB=!isRGB;
	}

	public void sourisBouge(int x, int y, int u, int v){
		x=x-(u/2-modele.cadreImageCourant().getImage().getWidth()/2);
		y=y-(v/2-modele.cadreImageCourant().getImage().getHeight()/2);
		if(echantillonageActif){
			if(modele.estDansImage(x, y)){
				modele.afficherCouleurPixel(x, y, isRGB);
			}else{
				modele.enleverCouleurPixel();
			}
		}
	}
	
	public void addNbhisto(JFrame histo){
		histo.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(!modele.isEstEgalisation()){
					if(modele.isEstHistoCliquer()){
						modele.fermetureHisto();
						modele.setEstHistoCliquer(!modele.isEstHistoCliquer());
					}
				}
				else
					modele.setEstEgalisation(false);
			}
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				//if(!modele.isEstEgalisation()){
					if(modele.isEstHistoCliquer()){
						modele.fermetureHisto();
						modele.setEstHistoCliquer(!modele.isEstHistoCliquer());
						
					}
					modele.setEstEgalisation(false);
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		//(new ControlerEgalisation(modele));
	}
	public void sourisEntre(int x, int y, int u, int v){
		x=x-(u/2-modele.cadreImageCourant().getImage().getWidth()/2);
		y=y-(v/2-modele.cadreImageCourant().getImage().getHeight()/2);
		if(echantillonageActif && modele.estDansImage(x, y)){
			modele.afficherCouleurPixel(x, y, isRGB);
		}
		if(selectionActive){
			deplacementScroll=false;
		}
	}

	public void sourisSort(int x, int y, int u, int v){
		x=x-(u/2-modele.cadreImageCourant().getImage().getWidth()/2);
		y=y-(v/2-modele.cadreImageCourant().getImage().getHeight()/2);
		if(echantillonageActif){
			modele.enleverCouleurPixel();
		}
		if(selectionActive){
			modele.setDeltaScroll(x, y);
			deplacementScroll=true;
			//modele.setDelta(x, y);
			//modele.deplacerScroll(x,y,deplacementScroll);
		}
		/*if(ajustementSelection){
			deplacementScroll=true;
			modele.setDeltaScroll(x, y);
		}*/
	}

	public void sourisClique(int x, int y){
	}

	public void sourisPresse(int x, int y, int u, int v){
		//modele.afficherpos(x,y);
		//modele.setScroll();
		//init();
		x=x-(u/2-modele.cadreImageCourant().getImage().getWidth()/2);
		y=y-(v/2-modele.cadreImageCourant().getImage().getHeight()/2);
		if(modele.estDansImage(x, y)){
			//System.out.println("jambon1"+selectionActive);
			if(selectionActive && modele.estDansSelection(x, y)){
				init();
				//System.out.println("jambon2");
				ajustementSelection=true;
				modele.setDelta(x, y);
				modele.setDist(x, y);
			}else{
				init();
				selectionActive=true;
				it.getButtonSegmenter().setEnabled(true);
				modele.setPrec(x, y);
			}
		}else{
			init();
			modele.actualiserImageIcon();
		}
	}

	public void sourisRelache(int x, int y, int u, int v){
		x=x-(u/2-modele.cadreImageCourant().getImage().getWidth()/2);
		y=y-(v/2-modele.cadreImageCourant().getImage().getHeight()/2);
		if(selectionActive){
			x=modele.ajustementX(x);
			y=modele.ajustementY(y);
			if(deplacementScroll){
				modele.deplacerScroll(x, y);
			}
			modele.selectionne(x, y);
			//modele.calculerHistogrammeRGB();
		}
		if(ajustementSelection){
			//System.out.println("jambon1 "+x+" "+y);
			x=modele.ajustementSelectionX(x);
			y=modele.ajustementSelectionY(y);
			//System.out.println("jambon2 "+x+" "+y);
			modele.deplacerScrollAjustement(x, y);
			modele.ajustementSelection(x, y);
			selectionActive=true;
			it.getButtonSegmenter().setEnabled(true);
			//modele.calculerHistogrammeRGB();
		}
	}

	public void sourisDragged(int x, int y){
		/*if(ajustementSelection){
			modele.ajustementSelection(x, y);
		}
		if(selectionActive){
			modele.selectionne(x, y);
		}*/
	}

	public void boutonFusionClic()
	{
		init();
		fusionActive=true;
		modele.traiterFusion();
	}

	public void sliderFusionChange()
	{
		modele.traiterVariationFusion(it.getSliderFusionValue());
	}

	public void boutonAppliquerFusionClic()
	{
		init();
		//this.it.retirerComponentFusion();
		//modele.calculerHistogrammeRGB();
	}

	public void boutonAppliquerFiltre()
	{
		this.it.rafraichirComponentOption();
		modele.actualiserImageIcon();
	}

	public void boutonFlouterClic()
	{
		init();
		flouActive=true;
		modele.memoriseImage();
		it.ajouterComponentChoixTailleFiltre(TypeFiltre.GAUSSIEN);
	}
	
	public void decouper(){
		modele.decouper();
		modele.annulerSelection();
	}
	
	public void addControlerEgalisation(JMenuItem egalisation){
		egalisation.addActionListener(new ControlerEgalisation(modele));
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

	public void addControlerRedimenssioner(JMenuItem redimensionner){
		redimensionner.addActionListener(new ControlerRedimessionner(it));
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
	
	public void addControlerFiltreUser(JButton valider, JButton annuler, JButton previsualiser, JComboBox<String> boxChoixTailleFiltre, JPanel panelUser)
	{
		ControlerFiltreUser c = new ControlerFiltreUser(this.it, this.modele, boxChoixTailleFiltre, panelUser);		
		previsualiser.addActionListener(c);
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

	public void addControlerAfficherHisto(JButton afficherHisto) {
		afficherHisto.addActionListener(new ControlerAfficherHisto(this.modele));
	}

	public void addRedimensionnerValider(JButton valider) {
		valider.addActionListener(new ControlerRedimensionnerValider(modele));	
	}

	public void addControlerEtalement(JMenuItem etalement) {
		etalement.addActionListener(new ControlerEtalement(modele));	
	}

	public void addControlerInverser(JMenuItem inverser) {
		inverser.addActionListener(new ControlerInverser(modele));	
	}
	
	public void addControlerDecouper(JMenuItem decouper){
		decouper.addActionListener(new ControlerDecouper(this));
	}
	
	public void addControlerSegmentation(JButton segmenter)
	{
		segmenter.addActionListener(new ControlerSegmentation(this));
	}
	
	public void addControlerMoyen(JMenuItem moyen){
		moyen.addActionListener(new ControlerFlouter(this));
	}
}
