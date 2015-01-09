package Vue;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import Modele.Modele;

public class InterfaceGraphique implements Runnable{
	private JFrame frame;
	private JPanel panelOption;
	private JTextArea PixelCouleur;
	private JTabbedPane tabbedPane;
	private Modele modele;
	private Controler controler;
	private JMenuItem sauvegarde;
	private CheckboxGroup groupe;
	private Checkbox box1, box2;
	private Histogramme histoR, histoG, histoB;
	
	
	public JMenuItem getSauvegarde() {
		return sauvegarde;
	}

	public void setSauvegarde(JMenuItem sauvegarde) {
		this.sauvegarde = sauvegarde;
	}
	
	public void setEnableSauvegarde(boolean enable) {
		this.sauvegarde.setEnabled(enable);
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
	public JButton ajouterOnglet(CadreImage cadreImage){
		final JPanel content = new JPanel();
		JPanel tab = new JPanel();
		tab.setOpaque(false);	
		JTabbedPane tmp = getTabbedPane();	
		cadreImage.addMouseMotionListener(controler);
		cadreImage.addMouseListener(controler);

		//partie onglet nom
		JLabel labelOnglet = new JLabel(cadreImage.getNomFichier()+(getTabbedPane().getTabCount()+1));
		//partie onglet fermer
		JButton boutonFermer = new JButton("X");
		boutonFermer.addActionListener(controler);
		//boutonFermer.addMouseListener(controler);
		//Ajout au panel de la partie nom+fermer
		tab.add(labelOnglet, BorderLayout.WEST);
		tab.add(boutonFermer, BorderLayout.EAST);
		//Ajout panel à l'onglet
		tmp.addTab(null, content);

		
		/*aPanel myAPanel = new aPanel();
			// ... maybe adding some components to myAPanel
			JScrollPane jsp = new JScrollPane(myAPanel);
			then add jsp to your JTabbedPane
			
		//Parametre de l'onglet*/
		tmp.setTabComponentAt(tabbedPane.getTabCount()- 1, tab);
		

		
		JScrollPane scrollPane = new JScrollPane(cadreImage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tmp.setComponentAt(tabbedPane.getTabCount()-1, scrollPane);
	//	FilterTab tab = (FilterTab)tmp.getSelectedComponent();
		//Ajout image à l'onglet
		tmp.setComponentAt(tabbedPane.getTabCount()-1, scrollPane);
		tmp.setSelectedIndex(tabbedPane.getTabCount()-1);
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
	    box1.addMouseListener(controler);
	    panelOption.add(box1); 
	    box2=new Checkbox("YUV",groupe,false);
	    box2.addMouseListener(controler);
	    panelOption.add(box2); 
	    frame.validate();
	}
	
	public void retraitChoixRGB(){
		panelOption.remove(box1);
		panelOption.remove(box2); 
		frame.validate();
	}
	
	//ajout les trois histogrammes dans la barre d'option de droite
	public void ajouterHistoRgb(int[][] tabsHisto)
	{
		panelOption.removeAll();
		panelOption.setLayout(new BoxLayout(panelOption, BoxLayout.Y_AXIS));
		histoR = new Histogramme(tabsHisto[0], "Rouge",new Dimension(panelOption.getWidth(),panelOption.getHeight()/3));
		histoG = new Histogramme(tabsHisto[1], "Vert",new Dimension(panelOption.getWidth(),panelOption.getHeight()/3));
		histoB = new Histogramme(tabsHisto[2], "Bleu",new Dimension(panelOption.getWidth(),panelOption.getHeight()/3));

		panelOption.add(histoR);
		panelOption.add(histoG);
		panelOption.add(histoB);
	
		panelOption.repaint();
		frame.validate();
	}
	
	public void retirerHistoRgb(int[][] tabsHisto)
	{
		panelOption.remove(histoR);
		panelOption.remove(histoG);
		panelOption.remove(histoB);
		panelOption.repaint();
		frame.validate();
	}
	
	public void ajouterComponentFusion(CadreImage cadre_ima_fusion)
	{
		CadreImage cadre_ima = cadre_ima_fusion;
		//TODO redimensionner l'image pour qu'elle rentre dans le panelOption.
		JSlider slider = new JSlider(0,100,0);
		JButton appliquer = new JButton("Appliquer fusion");
			
		slider.addChangeListener(controler);
		appliquer.addActionListener(controler);
		
		panelOption.removeAll();
		panelOption.add(appliquer);
		panelOption.add(slider);
		panelOption.add(cadre_ima);
		panelOption.repaint();
		frame.validate();
	}
	
	public void retirerComponentFusion()
	{
		panelOption.removeAll();
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
		sauvegarde.setEnabled(false);
		sauvegarde.addActionListener(controler);
		principal.add(sauvegarde);
		//Menu principal => Imprimer00,panelOption.getParent().getHeight())
		JMenuItem charger = new JMenuItem("Charger");
		charger.addActionListener(controler);
		principal.add(charger);
		//Menu principal => Charger
		JMenuItem imprimer = new JMenuItem("Imprimer");
		principal.add(imprimer);
		//Menu principal => Quitter
		JMenuItem quitter = new JMenuItem("Quitter");
		//quitter.addActionListener(new QuitterApplication());
		principal.add(quitter);


		//Edition
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
		edition.add(decouper);

		//Image
		JMenu image = new JMenu("Image");
		//Image => Couleur du pixel
		JMenuItem couleurpixel = new JMenuItem("Couleur pixel");
		couleurpixel.addActionListener(controler);
		image.add(couleurpixel);
		//Image => Redimenssioner
		JMenuItem redimensionner = new JMenuItem("Redimensionner");
		image.add(redimensionner);
		//Image => Segmenter
		JMenuItem  segmenter = new JMenuItem("Segmenter");
		image.add(segmenter);
		//Image => Transformation
		JMenu  transformation = new JMenu("Transformation");      
		//Image => transformation => fusion
		JMenuItem fusion = new JMenuItem("Fusion");
		fusion.addActionListener(controler);
		transformation.add(fusion);
		//Image => transformation => Gris
		JMenuItem imagris = new JMenuItem("Image grise");
		imagris.addActionListener(controler);
		transformation.add(imagris);       
		image.add(transformation);

		//Filtre
		JMenu filtre = new JMenu("Filtre");
		//filtre => Amelioration
		JMenu amelioration = new JMenu("Amelioration");
		//filtre => Amelioration => gradiant
		JMenuItem gradiant = new JMenuItem("Gradiant");
		amelioration.add(gradiant);       
		filtre.add(amelioration);
		//filtre => Traitement
		JMenu traitement = new JMenu("Traitement");
		//filtre => Traitement => moyen
		JMenuItem moyen = new JMenuItem("Moyenneur (flouter)");
		moyen.addActionListener(controler);
		traitement.add(moyen);
		//filtre => Traitement => gaussien
		JMenuItem gaussien = new JMenuItem("Gaussien");
		traitement.add(gaussien);
		//filtre => Traitement => median
		JMenuItem median = new JMenuItem("Median");
		traitement.add(median);
		//filtre => Traitement => utilisateur
		JMenuItem utilisateur = new JMenuItem("Utilisateur");
		traitement.add(utilisateur);
		//filtre => Traitement => flou
		JMenuItem flou = new JMenuItem("Flou");
		traitement.add(flou);
		filtre.add(traitement);

		// Barre de menu
		JMenuBar barre = new JMenuBar();
		//Ajout barre Principal à barre
		barre.add(principal);
		//Ajout barre Edition à barre
		barre.add(edition);
		//Ajout barre Image à barre
		barre.add(image);
		//Ajout barre filtre à barre
		barre.add(filtre);
		frame.setJMenuBar(barre);

		frame.setLayout(new BorderLayout());

		//implementation de la toolbar
		JPanel panel = new JPanel();
		JToolBar toolBar = new JToolBar();
		panel.add(toolBar);
		toolBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		toolBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		toolBar.setBackground(Color.WHITE);
		toolBar.setForeground(Color.BLACK);
		/*ImageIcon imagecharger = new ImageIcon("Charger");
		JLabel label = new JLabel(imagecharger);
		toolBar.add(charger);*/
		JButton btnCharger = new JButton("Charger");
		btnCharger.setFocusable(false);
		btnCharger.addActionListener(controler);
		toolBar.add(btnCharger);
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.setFocusable(false);
		toolBar.add(btnAnnuler);
		JButton btnRefaire = new JButton("Refaire");
		btnRefaire.setFocusable(false);
		toolBar.add(btnRefaire);
		frame.add(panel,BorderLayout.NORTH);

		tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addChangeListener(controler);
		tabbedPane.setOpaque(true);
		tabbedPane.setBackground(Color.WHITE);

		frame.add(tabbedPane,BorderLayout.CENTER);
		panelOption = new JPanel();
		//JTextArea texte= new JTextArea("Zone d'option/bouton rapide");
		//panelOption.add(texte);
		
		frame.add(panelOption,BorderLayout.EAST);
		panelOption.setPreferredSize(new Dimension(200,panelOption.getParent().getHeight()));
		
		JPanel panelOption2 = new JPanel();
		PixelCouleur= new JTextArea();
		panelOption2.add(PixelCouleur);
		frame.add(panelOption2,BorderLayout.SOUTH);

		// Un clic sur le bouton de fermeture clos l'application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// On fixe la taille et on demarre
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}
