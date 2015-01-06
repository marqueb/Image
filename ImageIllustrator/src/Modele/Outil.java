package Modele;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Outil {
	
	public CadreImage Charger(InterfaceGraphique it){
		File monFichier;
    	int controle;
    	CadreImage cadreImage = new CadreImage();
    	
    	JFileChooser j = new JFileChooser();
    	controle=j.showOpenDialog(cadreImage);
    	if(controle==JFileChooser.APPROVE_OPTION){
    		monFichier=j.getSelectedFile();
    		try {
    			JTabbedPane tmp = it.getTabbedPane();
    			cadreImage.setImage(ImageIO.read(monFichier));
    			tmp.add("Onglet "+(it.getTabbedPane().getTabCount()+1), cadreImage);
    			it.setTabbedPane(tmp);
    			it.getTabbedPane().setSelectedIndex(it.getTabbedPane().getTabCount()-1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		cadreImage.repaint();
    	}
    	return cadreImage;
	}
	
	
	//TODO
	//Copier
	//Coller
	//précédent
	//suivant
	
}
