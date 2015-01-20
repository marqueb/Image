package Vue;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import Controleur.Controler;
import Controleur.ControlerBoutonFusion;
import Controleur.ControlerFlouter;
import Modele.Modele;
import Modele.Outil;
import Modele.TypeFiltre;

public class InterfaceGraphique implements Runnable{
	private JFrame frame,frameHisto,frameRedim ;
	private JPanel panelOption;
	private JPanel panelInfo;
	private JTextArea PixelCouleur ;
	private TextArea largeur, hauteur;
	private JTabbedPane tabbedPane;
	private Modele modele;
	private Controler controler;
	private JMenuItem sauvegarde, couleurPixel, fusion, imagris, moyen,egalisation,redimensionner,etalement,inverser ;
	private CheckboxGroup groupe;
	private Checkbox box1, box2;
	private JCheckBox rouge,vert,bleu, luminance, chrominanceU , chrominanceV;
	private Histogramme histo;
	private JSlider sliderFusion = null, sliderChoixTailleFiltre = null;
	private JComboBox choixRgbYuv;
	private JButton afficherHisto;

	public Image chargerImage(String bouton){
		Image img=null;
		switch (bouton) {
		case "Charger" :
			try {
				img = ImageIO.read(getClass().getResource("Image/charger.png"));
			} catch (IOException ex) {
			}
			break;
		case "Sauver" :
			try {
				img = ImageIO.read(getClass().getResource("Image/sauver.png"));
			} catch (IOException ex) {
			}
			break;
		case "Imprimer" :
			try {
				img = ImageIO.read(getClass().getResource("Image/imprimer.png"));
			} catch (IOException ex) {
			}
			break;
		}
		return img;
	}
	
	public void redimensionner() {
		CadreImage image = new CadreImage();
		image=modele.cadreImageCourant();
		frameRedim = new JFrame();
		frameRedim.setSize(new Dimension(800,200));
		//frameRedim.setLocation(image.getWidth(),image.getHeight());
		largeur= new TextArea(""+image.getImage().getWidth());
		hauteur= new TextArea(""+image.getImage().getHeight());
		JPanel text = new JPanel();
		text.setLayout(new BorderLayout());
		text.add(largeur,BorderLayout.EAST);
		text.add(hauteur,BorderLayout.WEST);
		JButton valider = new JButton("Valider");
		controler.addRedimensionnerValider(valider);
		text.add(valider,BorderLayout.SOUTH);
		frameRedim.add(text);
		frameRedim.setVisible(true);
	}
	
	public int getSliderFusionValue()
	{
		if(this.sliderFusion == null) return -1;
		return this.sliderFusion.getValue();
	}

	public JPanel getPanelInfo()
	{
		return this.panelInfo;
	}

	public int getSliderChoixTailleFiltreValue()
	{
		if(this.sliderChoixTailleFiltre == null) return -1;
		//*2+1 pour avoir les tailles impaires entre 1 et 15
		return this.sliderChoixTailleFiltre.getValue();
	}

	public JMenuItem getSauvegarde() {
		return sauvegarde;
	}

	public void setSauvegarde(JMenuItem sauvegarde) {
		this.sauvegarde = sauvegarde;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public JTabbedPane getTabbedPane(JTabbedPane tabbedPane) {
		return tabbedPane;
	}

	public Modele getModele() {
		return modele;
	}

	public void setModele(Modele modele) {
		this.modele = modele;
	}

	public Controler getControler() {
		return controler;
	}

	public void setControler(Controler controler) {
		this.controler = controler;
	}

	public InterfaceGraphique(Modele m, Controler c)
	{
		modele = m;
		controler = c;
	}

	//retourne un JButton pour completer la liste de bouton
	public JButton ajouterOnglet(CadreImage image){
		//final JPanel content = new JPanel();
		JPanel tab = new JPanel();
		tab.setOpaque(false);    

		//partie onglet nom
		JLabel labelOnglet = new JLabel(image.getNomFichier()+(getTabbedPane().getTabCount()));
		//partie onglet fermerOnglet
	
		JButton boutonFermer = new JButton("X");
		controler.addControlerX(boutonFermer);
		//Ajout au panel de la partie nom+fermer
		tab.add(labelOnglet, BorderLayout.WEST);
		tab.add(boutonFermer, BorderLayout.EAST);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()- 1, tab);  
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		this.rafraichirComponentOption();	
		egalisation.setEnabled(true);
		etalement.setEnabled(true);
		inverser.setEnabled(true);
		redimensionner.setEnabled(true);
		if(modele.getNbAffichageHisto()==0){
			afficherHisto.setVisible(true);
		}
		else{
			frameHisto.dispose();
		}
		frame.validate();
		//Parametre de l'onglet
		return boutonFermer;
	}


	public void afficherValeurCouleur(int x , int y, int r, int g, int b){
		PixelCouleur.setText("pixel: ("+x+" , "+y+"), Couleur: (Rouge :"+ r+ ", Vert :"+g+", Bleu :"+b+")");
	}

	public void afficherYUVCouleur(int x , int y, double yp, double u, double v){
		PixelCouleur.setText("pixel: ("+x+" , "+y+"), YUV: (luminance :"+ yp+ ", chrominance :"+u+" et "+v+")");
	}

	public void enleverCouleurPixel(){
		PixelCouleur.setText("");
	}

	public void affichageChoixRGB(){
		groupe=new CheckboxGroup(); 
		box1=new Checkbox("RGB",groupe,true);
		controler.addControlerRGB(box1);
		panelOption.add(box1); 
		box2=new Checkbox("YUV",groupe,false);
		controler.addControlerYUV(box2);
		panelOption.add(box2); 
		frame.validate();
	}

	public void retraitChoixRGB(){
		panelOption.remove(box1);
		panelOption.remove(box2); 
		frame.validate();
	}

	//ajout les trois histogrammes dans la barre d'option de droite
	public void ajouterHistoRgb(int[][] tabsHisto, int[][] yuv)
	{

		frameHisto = new JFrame();
		panelOption.setLayout(new BoxLayout(panelOption, BoxLayout.Y_AXIS));
		histo = new Histogramme(tabsHisto,yuv,"histogramme", new Dimension(1000,600));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel option = new JPanel();
		option.setLayout(new BoxLayout(option, BoxLayout.Y_AXIS));
		rouge= new JCheckBox("Rouge  ");
		vert= new JCheckBox("Vert   ");
		bleu= new JCheckBox("Bleu   ");
		luminance= new JCheckBox("Lum Y");
		chrominanceU= new JCheckBox("Chrom U");
		chrominanceV= new JCheckBox("Chrom V");
	
		//initalisation
		rouge.setSelected(true);
		vert.setSelected(true);
		bleu.setSelected(true);
		
		luminance.setVisible(false);
		chrominanceU.setVisible(false);
		chrominanceV.setVisible(false);
		
		luminance.setSelected(false);
		chrominanceU.setSelected(false);
		chrominanceV.setSelected(false);
		
		String[] choix = { "RGB","YUV" };
		choixRgbYuv = new JComboBox(choix);
		choixRgbYuv.setSelectedIndex(0);
		controler.addControlerHistoChoixRgbYuv(choixRgbYuv);
		
		JPanel panelBox = new JPanel();
		panelBox.add(choixRgbYuv);
		option.add(panelBox);
		
		controler.addControlerCheckCouleur(rouge,vert,bleu,luminance, chrominanceU,chrominanceV);
		option.add(rouge);
		option.add(vert);
		option.add(bleu);
		option.add(luminance);
		option.add(chrominanceU);
		option.add(chrominanceV);
		
		panel.add(histo,BorderLayout.CENTER);
		panel.add(option,BorderLayout.EAST);
		
		
	
		frameHisto.add(panel);
		controler.addNbhisto(frameHisto);
		frameHisto.setSize(1200, 600);
		frameHisto.setVisible(true);

		frame.validate();
	}

	public void ajouterComponentFusion(CadreImage cadre_ima_fusion)
	{
		int newWidth = panelOption.getWidth();
		int newHeight = (cadre_ima_fusion.getImage().getHeight()*panelOption.getWidth())/cadre_ima_fusion.getImage().getWidth();

		CadreImage cadre_ima = new CadreImage(Outil.resize(Outil.deepCopy(cadre_ima_fusion.getImage()), newWidth, 
				newHeight));

		cadre_ima.setPreferredSize(new Dimension(newWidth, newHeight));

		sliderFusion = new JSlider(0,100,0);
		JButton appliquer = new JButton("Appliquer fusion");		
		controler.addControlerFusion(sliderFusion, appliquer);
		//slider.addChangeListener(controler);
		//appliquer.addActionListener(controler);

		panelOption.removeAll();
		panelOption.add(appliquer);
		panelOption.add(sliderFusion);
		panelOption.add(cadre_ima);
		panelOption.repaint();
		frame.validate();
	}

	public JCheckBox getRouge() {
		return rouge;
	}

	public void setRouge(JCheckBox rouge) {
		this.rouge = rouge;
	}

	public void retirerComponentFusion()
	{
		panelOption.removeAll();
		panelOption.repaint();
		frame.validate();
	}

	public void ajouterComponentChoixTailleFiltre(TypeFiltre typeFiltre)
	{
		JComboBox<String> boxTypeFiltre = null;
		panelOption.removeAll();
		String[] stringTailles = {"3x3","5x5","7x7","9x9"};
		JComboBox<String> boxChoixTailleFiltre = new JComboBox<String>(stringTailles);


		switch (typeFiltre)
		{
		case GAUSSIEN:
			String[] stringsTypesFiltres =  {"Flitre Gaussien", "Filtre Moyenneur"};
			boxTypeFiltre = new JComboBox<String>(stringsTypesFiltres);
			panelOption.add(boxTypeFiltre);
			break;
		case MEDIAN:
			break;
		}
		JButton appliquer = new JButton("Valider");
		JButton annuler = new JButton("Annuler");

		controler.addControlerChoixTailleFiltre(boxChoixTailleFiltre, annuler, appliquer, typeFiltre, boxTypeFiltre);
		panelOption.add(new JLabel("Choisissez la taille du filtre :"));
		panelOption.add(boxChoixTailleFiltre);
		panelOption.add(appliquer);
		panelOption.add(annuler);
		panelOption.repaint();
		frame.validate();
	}

	//remplace l'image par une grille correspondant au filtre et affiche une image � droite pour pr�visualiser
	public void ajouterComponentFiltreUser()
	{
		JButton previsualiser = new JButton("Previsualiser");
		JButton valider = new JButton("Valider");
		JButton annuler = new JButton("Annuler");
		JLabel labelTaille = new JLabel("Taille du filtre:");
		String[] stringTailles = {"3x3","5x5","7x7","9x9"};
		JComboBox<String> boxChoixTailleFiltre = new JComboBox<String>(stringTailles);

		JPanel panelUser = ajouterGrilleFiltreUser(3);

		//		previsualiser.setEnabled(false);
		//		valider.setEnabled(false);

		controler.addControlerFiltreUser(valider, annuler, previsualiser, boxChoixTailleFiltre, panelUser);

		//modification du panelOption
		panelOption.removeAll();
		panelOption.add(previsualiser);
		panelOption.add(valider);
		panelOption.add(annuler);
		panelOption.add(labelTaille);
		panelOption.add(boxChoixTailleFiltre);

		//affichage des modifs
		panelOption.repaint();
		frame.validate();
	}
	
	public void previsualiserApplicationFiltreUser(BufferedImage im_no_modif, BufferedImage im_modif)
	{
		Outil outil = new Outil();
		JFrame framePreview = new JFrame("Previsualisation du filtre");
		JLabel labelNoModif = new JLabel("Avant");
		JLabel labelModif = new JLabel("Apres");

		Font font = new Font("Arial",Font.BOLD,18);
		
		JPanel panel = new JPanel(new GridLayout());
		JPanel panelImNoModif = new JPanel(new BorderLayout());
		JPanel panelImModif = new JPanel(new BorderLayout());
		JPanel panelScrollNoModif = new JPanel(new BorderLayout());
		JPanel panelScrollModif = new JPanel(new BorderLayout());
		JScrollPane scrollPaneNoModif = new JScrollPane();
		JScrollPane scrollPaneModif = new JScrollPane();
		
		CadreImage cadreImageNoModif=outil.initCadre(im_no_modif, controler);
		CadreImage cadreImageModif=outil.initCadre(im_modif, controler);
		
		labelNoModif.setFont(font);
		labelModif.setFont(font);
		
		panelScrollNoModif.add(cadreImageNoModif);
		scrollPaneNoModif.setViewportView(panelScrollNoModif);
		//JScrollPane scrollPaneNoModif = new JScrollPane(panelScrollNoModif);
		panelImNoModif.add(labelNoModif, BorderLayout.NORTH);
		panelImNoModif.add(scrollPaneNoModif, BorderLayout.CENTER);

		panelScrollModif.add(cadreImageModif);
		scrollPaneModif.setViewportView(panelScrollModif);
		panelImModif.add(labelModif, BorderLayout.NORTH);
		panelImModif.add(scrollPaneModif, BorderLayout.CENTER);

		panel.add(panelImNoModif);
		panel.add(panelImModif);
		framePreview.add(panel);
		
		framePreview.dispose();
		framePreview.setSize(1200, 600);
		framePreview.setVisible(true);
		framePreview.validate();
	}

	public Histogramme getHisto() {
		return histo;
	}

	public void setHisto(Histogramme histo) {
		this.histo = histo;
	}

	public JComboBox getChoixRgbYuv() {
		return choixRgbYuv;
	}

	public void setChoixRgbYuv(JComboBox choixRgbYuv) {
		this.choixRgbYuv = choixRgbYuv;
	}

	public JPanel ajouterGrilleFiltreUser(int taille)
	{
		JPanel panel = createGrilleFiltreUser(taille);
		JScrollPane scrollPane = (JScrollPane) this.tabbedPane.getSelectedComponent();
		scrollPane.setViewportView(panel);
		return panel;
	}


	private JPanel createGrilleFiltreUser(int taille)
	{
		JPanel panel = new JPanel(new GridLayout(taille, taille));
		JTextArea tmp = null;
		for(int i = 1; i<=taille; i++)
		{
			for(int j = 0; j<taille; j++)
			{
				tmp = new JTextArea("0");
				tmp.setBorder(BorderFactory.createLineBorder(Color.black));
				//TODO adapter la taille de police des JTextArea en fonction de la taille de la grille
				panel.add(tmp);
			}
		}

		return panel;
	}

	public void rafraichirComponentOption()
	{
		//panelOption.removeAll();
		//modele.calculerHistogrammeRGB();
		panelOption.repaint();
		frame.validate();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public boolean isRGB(Object o){
		boolean isRGB=false;
		if(o==box1){
			isRGB=true;
		}
		return isRGB;
	}
	
	public void setEnable(boolean enable){
		sauvegarde.setEnabled(enable);
		couleurPixel.setEnabled(enable);
		fusion.setEnabled(enable);
		imagris.setEnabled(enable);
		//moyen.setEnabled(enable);
	}

	public void run(){
		frame = new JFrame("Fenetre");
		// Ajout de notre composant de dessin dans la fenetre
		//frame.add(cadreImage);

		/////////////////////////
		// Creation d'un menu  //
		/////////////////////////

		// Menu principal
		JMenu principal = new JMenu("Fichier");

		//Menu principal => Nouveau
		//JMenuItem nouveau = new JMenuItem("Nouveau");       
		//nouveau.addActionListener(controler);
		//principal.add(nouveau);
		//Menu principal => Sauvegarde
		sauvegarde = new JMenuItem("Sauvegarde");
		controler.addControlerSauvegarder(sauvegarde);
		principal.add(sauvegarde);
		//Menu principal => Imprimer00,panelOption.getParent().getHeight())
		JMenuItem charger = new JMenuItem("Charger");
		controler.addControlerCharger(charger);
		principal.add(charger);
		/*		//Menu principal => Charger
		JMenuItem imprimer = new JMenuItem("Imprimer");
		principal.add(imprimer);
		//Menu principal => Quitter
		JMenuItem quitter = new JMenuItem("Quitter");
		//quitter.addActionListener(new QuitterApplication());
		principal.add(quitter);*/


		/*		//Edition
		JMenu edition = new JMenu("Edition");
		//Edition => copier
		JMenuItem copier = new JMenuItem("Copier");
		edition.add(copier);
		//Edition => coller
		JMenuItem coller = new JMenuItem("Coller");
		edition.add(coller);
		//Edition => annuler
		JMenuItem annuler = new JMenuItem("Annuler");
		edition.add(annuler);
		//Edition => refaire
		JMenuItem refaire = new JMenuItem("Refaire");
		edition.add(refaire);
		//Edition => refaire
		JMenuItem decouper = new JMenuItem("Decouper");
		edition.add(decouper);*/

		//Image
		JMenu image = new JMenu("Image");
		//Image => Couleur du pixel
		couleurPixel = new JMenuItem("Couleur pixel");
		controler.addControlerCouleurPixel(couleurPixel);
		image.add(couleurPixel);
		//Image => Redimenssioner
		redimensionner = new JMenuItem("Redimensionner");
		redimensionner.setEnabled(false);
		image.add(redimensionner);
		controler.addControlerRedimenssioner(redimensionner);
		//Image => Segmenter
		JMenuItem  segmenter = new JMenuItem("Segmenter");
		image.add(segmenter);
		 //Image => Transformation
		JMenu  transformation = new JMenu("Transformation");      
		//Image => transformation => fusion
		fusion = new JMenuItem("Fusion");
		fusion.addActionListener(new ControlerBoutonFusion(controler));
		transformation.add(fusion);
		//Image => transformation => Gris
		imagris = new JMenuItem("Image grise");
		controler.addControlerImagris(imagris);
		transformation.add(imagris);       
		//Image => transformation => contraste
		JMenuItem contraste = new JMenuItem("R�hausser les contrastes");
		controler.addControlerContraste(contraste);
		transformation.add(contraste);       
		image.add(transformation);
		//Image => transformation => contours
		JMenuItem contours = new JMenuItem("Accentuer les contours");
		controler.addControlerContours(contours);
		transformation.add(contours);       
		image.add(transformation);

		//Filtre
		//JMenu filtre = new JMenu("Filtre");
		//filtre => Amelioration
		//		JMenu amelioration = new JMenu("Amelioration");
		//		//filtre => Amelioration => gradiant
		//		JMenuItem gradiant = new JMenuItem("Gradiant");
		//		amelioration.add(gradiant);       
		//		filtre.add(amelioration);
		//		//filtre => Traitement
		JMenu traitement = new JMenu("Traitement");
		//filtre => Traitement => moyen
		inverser = new JMenuItem("Inverser");
		inverser.setEnabled(false);
		controler.addControlerInverser(inverser);
		traitement.add(inverser);
		etalement = new JMenuItem("Etalement");
		etalement.setEnabled(false);
		controler.addControlerEtalement(etalement);
		traitement.add(etalement);
		egalisation = new JMenuItem("Egalisation");
		egalisation.setEnabled(false);
		controler.addControlerEgalisation(egalisation);
		traitement.add(egalisation);
		JMenuItem moyen = new JMenuItem("Flouter");
		moyen.addActionListener(new ControlerFlouter(controler));
		traitement.add(moyen);
		//filtre => Traitement => gaussien
		//		JMenuItem gaussien = new JMenuItem("Gaussien");
		//		traitement.add(gaussien);
		//		//filtre => Traitement => median
		JMenuItem median = new JMenuItem("Median");
		controler.addControlerMedian(median);
		traitement.add(median);
		//		//filtre => Traitement => utilisateur
		JMenuItem utilisateur = new JMenuItem("D�finir un filtre");
		controler.addControlerBoutonFiltreUser(utilisateur);
		traitement.add(utilisateur);
		//		//filtre => Traitement => flou
		//		JMenuItem flou = new JMenuItem("Flou");
		//		traitement.add(flou);
		//		filtre.add(traitement);
		// Barre de menu
		JMenuBar barre = new JMenuBar();
		//Ajout barre Principal à barre
		barre.add(principal);
		//Ajout barre Edition à barre
		//barre.add(edition);
		//Ajout barre Image à barre
		barre.add(image);
		//Ajout barre filtre à barre
		//barre.add(filtre);
		barre.add(traitement);
		frame.setJMenuBar(barre);

		frame.setLayout(new BorderLayout());

	/*	//implementation de la toolbar
        JPanel panel = new JPanel();
        JToolBar toolBar = new JToolBar();
        panel.add(toolBar);
        toolBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toolBar.setBackground(Color.WHITE);
        toolBar.setForeground(Color.BLACK);
        /*ImageIcon imagecharger = new ImageIcon("Charger");
         */
         
		//implementation de la toolbar
		JPanel panel = new JPanel();
		JToolBar toolBar = new JToolBar();
		panel.add(toolBar);
		toolBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		toolBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		toolBar.setBackground(Color.WHITE);
		toolBar.setForeground(Color.BLACK);

  
		JButton btnCharger = new JButton();
		btnCharger.setToolTipText("Charger");
		btnCharger.setMnemonic('c');
		btnCharger.setIcon(new ImageIcon(chargerImage("Charger")));
		btnCharger.setFocusable(false);
		controler.addControlerCharger(btnCharger);
		toolBar.add(btnCharger);
		JButton btnSauver = new JButton();
		btnSauver.setIcon(new ImageIcon(chargerImage("Sauver")));
		btnSauver.setMnemonic('s');
		btnSauver.setEnabled(false);
		btnSauver.setToolTipText("Sauver");
		btnSauver.setFocusable(false);
		controler.addControlerSauvegarder(btnSauver);
		toolBar.add(btnSauver);
		JButton btnImprimer = new JButton();
		btnImprimer.setIcon(new ImageIcon(chargerImage("Imprimer")));
		btnImprimer.setToolTipText("Imprimer");
		btnImprimer.setFocusable(false);
		toolBar.add(btnImprimer);
		frame.add(panel,BorderLayout.NORTH);
		tabbedPane = new JTabbedPane();
		tabbedPane=new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		controler.addControlerOnglet(tabbedPane);
		tabbedPane.setOpaque(true);
		tabbedPane.setBackground(Color.WHITE);
		
		frame.add(tabbedPane,BorderLayout.CENTER);
		panelOption = new JPanel();
		//JTextArea texte= new JTextArea("Zone d'option/bouton rapide");
		//panelOption.add(texte);

		frame.add(panelOption,BorderLayout.EAST);
		panelOption.setPreferredSize(new Dimension(200,panelOption.getParent().getHeight()));
		afficherHisto=new JButton("Histogramme");
		controler.addControlerAfficherHisto(afficherHisto);
		afficherHisto.setVisible(false);
		panelOption.add(afficherHisto);
		panelInfo = new JPanel();
		PixelCouleur= new JTextArea();
		panelInfo.add(PixelCouleur);
		frame.add(panelInfo,BorderLayout.SOUTH);

		setEnable(false);
		// Un clic sur le bouton de fermeture clos l'application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// On fixe la taille et on demarre
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
	public JCheckBox getLuminance() {
		return luminance;
	}

	public void setLuminance(JCheckBox luminance) {
		this.luminance = luminance;
	}

	public JCheckBox getChrominanceU() {
		return chrominanceU;
	}

	public void setChrominanceU(JCheckBox chrominanceU) {
		this.chrominanceU = chrominanceU;
	}

	public JCheckBox getChrominanceV() {
		return chrominanceV;
	}

	public void setChrominanceV(JCheckBox chrominanceV) {
		this.chrominanceV = chrominanceV;
	}

	public JCheckBox getVert() {
		return vert;
	}

	public void setVert(JCheckBox vert) {
		this.vert = vert;
	}

	public JCheckBox getBleu() {
		return bleu;
	}

	public void setBleu(JCheckBox bleu) {
		this.bleu = bleu;
	}

	public JFrame getFrameHisto() {
		return frameHisto;
	}

	public JButton getAfficherHisto() {
		return afficherHisto;
	}

	public void setAfficherHisto(JButton afficherHisto) {
		this.afficherHisto = afficherHisto;
	}

	public void setFrameHisto(JFrame frameHisto) {
		this.frameHisto = frameHisto;
	}

	public JTabbedPane getTabbedAffichage() {
		return tabbedPane;
	}

	public void setTabbedAffichage(JTabbedPane tabbedAffichage) {
		this.tabbedPane = tabbedAffichage;
	}
	
	public JMenuItem getEgalisation() {
		return egalisation;
	}

	public void setEgalisation(JMenuItem egalisation) {
		this.egalisation = egalisation;
	}

	public JMenuItem getRedimensionner() {
		return redimensionner;
	}

	public void setRedimensionner(JMenuItem redimensionner) {
		this.redimensionner = redimensionner;
	}
	
	public TextArea getLargeur() {
		return largeur;
	}

	public void setLargeur(TextArea largeur) {
		this.largeur = largeur;
	}

	public TextArea getHauteur() {
		return hauteur;
	}

	public void setHauteur(TextArea hauteur) {
		this.hauteur = hauteur;
	}

	public JMenuItem getInverser() {
		return inverser;
	}

	public void setInverser(JMenuItem inverser) {
		this.inverser = inverser;
	}

	public JFrame getFrameRedim() {
		return frameRedim;
	}


	public JMenuItem getEtalement() {
		return etalement;
	}

	public void setEtalement(JMenuItem etalement) {
		this.etalement = etalement;
	}

	public void setFrameRedim(JFrame frameRedim) {
		this.frameRedim = frameRedim;
	}

}

