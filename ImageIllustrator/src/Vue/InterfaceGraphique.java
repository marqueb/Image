package Vue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import Controleur.Controler;
import Modele.Modele;

public class InterfaceGraphique implements Runnable{
	private JTextArea PixelCouleur;
	private JTabbedPane tabbedPane;
	private Modele modele;
	private Controler controler;
	//private CadreImage cadreImage;

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
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
	/*
	public CadreImage getCadreImage() {
		return cadreImage;
	}

	public void setCadreImage(CadreImage cadreImage) {
		this.cadreImage = cadreImage;
	}
	*/
	public InterfaceGraphique(Modele m, Controler c)
	{
		modele = m;
		controler = c;
		//cadreImage=new CadreImage();
	}

	public void ajouterOnglet(CadreImage cadreImage){
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
		
		//Ajout au panel de la partie nom+fermer
		tab.add(labelOnglet, BorderLayout.WEST);
		tab.add(boutonFermer, BorderLayout.EAST);
		//Ajout panel à l'onglet
		tmp.addTab(null, content);

		//Parametre de l'onglet
		tmp.setTabComponentAt(tabbedPane.getTabCount()- 1, tab);
		//Ajout image à l'onglet
		tmp.setComponentAt(tabbedPane.getTabCount()-1, cadreImage);
		tmp.setSelectedIndex(tabbedPane.getTabCount()-1);
	}

	public void afficherValeurCouleur(int x , int y, int r, int g, int b){
		PixelCouleur.setText("pixel: ("+x+" , "+y+"), Couleur: (Rouge :"+ r+ ", Vert :"+g+", Bleu :"+b+")");
	}
	
	public void enleverCouleurPixel(){
		PixelCouleur.setText("");
	}

	public void run(){
		JFrame frame = new JFrame("Fenetre");
		// Ajout de notre composant de dessin dans la fenetre
		//frame.add(cadreImage);

		/////////////////////////
		// Creation d'un menu  //
		/////////////////////////

		// Menu principal
		JMenu principal = new JMenu("Fichier");

		//Menu principal => Nouveau
		JMenuItem nouveau = new JMenuItem("Nouveau");       
		//nouveau.addActionListener(new NouveauApplication(mon_dessin));
		principal.add(nouveau);
		//Menu principal => Sauvegarde
		JMenuItem sauvegarde = new JMenuItem("Sauvegarde");
		//sauvegarde.addActionListener(new SauvegardeApplication(mon_dessin));
		principal.add(sauvegarde);
		//Menu principal => Imprimer
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
		transformation.add(fusion);
		//Image => transformation => Gris
		JMenuItem imagris = new JMenuItem("Image grise");
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
		JMenuItem moyen = new JMenuItem("Moyen");
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
		tabbedPane.addChangeListener(controler);
		tabbedPane.addMouseListener(controler);
		tabbedPane.setOpaque(true);
		tabbedPane.setBackground(Color.WHITE);

		frame.add(tabbedPane,BorderLayout.CENTER);

		JPanel panelOption = new JPanel();
		JTextArea texte= new JTextArea("Zone d'option/bouton rapide");
		panelOption.add(texte);
		frame.add(panelOption,BorderLayout.EAST);

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
