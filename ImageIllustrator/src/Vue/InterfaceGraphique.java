package Vue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	
	Modele modele;
	Controler controler;
	
	public InterfaceGraphique(Modele m, Controler c)
	{
		modele = m;
		controler = c;
	}
	
	public void run(){
        CadreImage cadre = new CadreImage();		//Aire de dessin
        JFrame frame = new JFrame("Fenetre");
        // Ajout de notre composant de dessin dans la fenetre
        frame.add(cadre);

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
        //charger.addActionListener(new ChargerApplication(mon_dessin));
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
        toolBar.add(btnCharger);
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFocusable(false);
        toolBar.add(btnAnnuler);
        JButton btnRefaire = new JButton("Refaire");
        btnRefaire.setFocusable(false);
        toolBar.add(btnRefaire);
        frame.add(panel,BorderLayout.NORTH);
       
       
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Onglet1", cadre);
        frame.add(tabbedPane,BorderLayout.CENTER);
       
        JPanel panelOption = new JPanel();
        JTextArea texte= new JTextArea("Zone d'option/bouton rapide");
        panelOption.add(texte);
        frame.add(panelOption,BorderLayout.EAST);
       
        JPanel panelOption2 = new JPanel();
        JTextArea texte2= new JTextArea("Zone d'affichage");
        panelOption2.add(texte2);
        frame.add(panelOption2,BorderLayout.SOUTH);
       
        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // On fixe la taille et on demarre
        frame.setSize(1000, 700);
        frame.setVisible(true);
	}
}
