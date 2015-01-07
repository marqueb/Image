package Modele;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Outil {
	
	public CadreImage charger(InterfaceGraphique it){
		File monFichier;
    	int controle;
    	CadreImage cadreImage = new CadreImage();
    	JFileChooser j = new JFileChooser();
    	controle=j.showOpenDialog(cadreImage);
    	if(controle==JFileChooser.APPROVE_OPTION){
    		monFichier=j.getSelectedFile();
    		try {
    			cadreImage.setImage(ImageIO.read(monFichier));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
    	return cadreImage;
	}
	
	public int CouleurPixel(BufferedImage image, int x, int y)
	{
		try{
			return image.getRGB(x,y);
		}catch(ArrayIndexOutOfBoundsException e){
			return -1;
		}
	}
	//TODO
	//Copier
	//Coller
	//précédent
	//suivant
	
}
