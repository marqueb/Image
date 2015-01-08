package Modele;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
		}
		return null;
	}

	public void sauvegarder(BufferedImage image){
		JFileChooser nom_fichier =new JFileChooser();
		int result =nom_fichier.showSaveDialog(null);
		if(result ==JFileChooser.APPROVE_OPTION){
			try {			
				ImageIO.write(image, "png",nom_fichier.getSelectedFile() );
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

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

	public int setR(int rgb){
		return (rgb & 0XFF) << 16;
	}

	public int setG(int rgb){
		return (rgb & 0XFF) << 8;
	}

	public int setB(int rgb){
		return (rgb & 0XFF) ;
	}

	public double getY(int r, int g, int b){
		return (0.299*r+0.587*g+0.114*b);
	}

	public double getU(int b, double y){
		return 0.492*(b-y);
	}
	
	public double getV(int r, double y){
		return 0.877*(r-y);
	}

	public int couleurPixel(BufferedImage image, int x, int y)
	{ 
		return image.getRGB(x,y);	
	}

	public void imagris(BufferedImage image){
		int r,g,b,gris;
		int couleur;
		for (int i=0;i<image.getWidth();i++){
			for (int j=0;j<image.getHeight();j++){
				couleur=couleurPixel(image,i,j);
				r=getR(couleur);
				g=getG(couleur);
				b=getB(couleur);
				gris=(r+b+g)/3;
				image.setRGB(i, j,setR(gris)+setB(gris)+setG(gris));
				//image.setRGB(i, j,setR(r/3)+setB(b/3)+setG(g/3));
			}
		}
	}

	//copier une buffered image
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}


	//TODO
	//Copier
	//Coller
	//précédent
	//suivant

}
