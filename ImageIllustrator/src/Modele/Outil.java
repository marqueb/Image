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
        		int index = monFichier.getName().indexOf('.');     
            	cadreImage.setNomFichier(monFichier.getName().substring(0,index));
            	
            	return cadreImage;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

    		int index = monFichier.getName().indexOf('.');     
        	cadreImage.setNomFichier(monFichier.getName().substring(0,index));
        	

    	}
    	return null;
	}
	
	public int getAlpha(int rgb){
		return (rgb >> 24 ) & 0XFF;
	}

	public int getR(int rgb){
		return (rgb >> 16 ) & 0XFF;
	}

	public int getG(int rgb){
		return (rgb >> 8 ) & 0XFF;
	}

	public int getB(int rgb){
		return rgb  & 0XFF;
	}
	
	
	public int couleurPixel(BufferedImage image, int x, int y)
	{ 
		return image.getRGB(x,y);	
	}
	//TODO
	//Copier
	//Coller
	//précédent
	//suivant
	
}
