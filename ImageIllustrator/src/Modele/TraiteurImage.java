package Modele;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import Vue.CadreImage;

//classe outils
public class TraiteurImage {
	private CadreImage cadreImage;
	
	public TraiteurImage(CadreImage cadreImage){
		this.cadreImage=cadreImage;
	}
	
	public void Charger(){
		File monFichier;
    	int controle;
    	
    	JFileChooser j = new JFileChooser();
    	controle=j.showOpenDialog(cadreImage);
    	if(controle==JFileChooser.APPROVE_OPTION){
    		monFichier=j.getSelectedFile();
    		try {
    			cadreImage.setImage(ImageIO.read(monFichier));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		cadreImage.repaint();
    	}
	}
	//Decouper
	//Copier
	//Coller
	//Couleur pixel
	//Redimensionner
	//Segmenter
	//Transformation
	//Filtre
}
