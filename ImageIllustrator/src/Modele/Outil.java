package Modele;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import Controleur.Controler;
import Vue.CadreImage;
import Vue.InterfaceGraphique;

public class Outil {

	public CadreImage charger(){
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

	public File lectureFichier(){
		JFileChooser j = new JFileChooser();
		int controle=j.showOpenDialog(new JFrame());
		if(controle==JFileChooser.APPROVE_OPTION){
			return j.getSelectedFile();
		}
		return null;
	}

	public BufferedImage lectureImage(File monFichier){
		try {
			BufferedImage image=ImageIO.read(monFichier);
			return image;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public CadreImage initCadre(BufferedImage image, Controler controler){
		CadreImage cadreImage=new CadreImage(image);
		JLabel icon=new JLabel(cadreImage.getImageIcon());
		JScrollPane imageScroller =new JScrollPane(icon);
		imageScroller.setViewportView(icon);
		imageScroller.setAutoscrolls(true);
		imageScroller.setWheelScrollingEnabled(true);
		imageScroller.setPreferredSize(new Dimension(200,200)); 
		cadreImage.setImageScroller(imageScroller);
		imageScroller.getHorizontalScrollBar().setValue(imageScroller.getHorizontalScrollBar().getMaximum());
		imageScroller.getVerticalScrollBar().setValue(imageScroller.getVerticalScrollBar().getMaximum());
		/*cadreImage.setMaxScrollX(imageScroller.getHorizontalScrollBar().getValue());
        cadreImage.setMaxScrollY(imageScroller.getVerticalScrollBar().getValue());
        imageScroller.getHorizontalScrollBar().setValue(0);
        imageScroller.getVerticalScrollBar().setValue(0);*/
		//scrollPanel.add(imageScroller); 
		controler.addControlerSouris(icon);
		//interfaceGraphique.getTabbedPane().setComponentAt(interfaceGraphique.getTabbedPane().getSelectedIndex(), imageScroller);
		return cadreImage;
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

	public BufferedImage imagris(BufferedImage image, boolean existeSelection, int[] selection){
		int r,g,b,gris;
		int i_deb, i_fin, j_deb, j_fin;
		int couleur;
		if(existeSelection){
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
		}else{
			i_deb=0;
			i_fin=image.getWidth();
			j_deb=0;
			j_fin=image.getHeight();
		}

		for (int i=i_deb;i<i_fin;i++){
			for (int j=j_deb;j<j_fin;j++){
				couleur=couleurPixel(image,i,j);
				r=getR(couleur);
				g=getG(couleur);
				b=getB(couleur);
				gris=(r+b+g)/3;
				image.setRGB(i, j,setR(gris)+setB(gris)+setG(gris));
			}
		}
		return image;
	}

	//copier une buffered image
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	//retourne un tableau 2d correspondant � la repr�sentation des effectifs de chaques valeurs des 3 composantes R, G et B.
	//tab[x][0] : effectif de la valeurs x pour la composante rouge (1: green; 2: blue)
	public int[][] getTabRgbHisto(BufferedImage ima)
	{
		int[][] tab = new int[3][256];
		int rgb = 0;

		//initialisation du tableau � 0
		for(int i = 0; i<3; i++)
		{
			for(int j = 0; j<256; j++)
			{
				tab[i][j] = 0;
			}
		}

		//cacule des effectifs de chaques valeurs pour chaques composantes
		for(int i = 0; i<ima.getTileWidth(); i++)
		{
			for(int j = 0; j<ima.getTileHeight(); j++)
			{

				rgb = ima.getRGB(i, j);
				tab[0][getR(rgb)]++;
				tab[1][getG(rgb)]++;
				tab[2][getB(rgb)]++;
			}
		}
		return tab;
	}

	//retourne un tableau 2d correspondant � la repr�sentation des effectifs de chaque niveau de gris.
	//tab[x] : effectif de la valeurs x pour la composante rouge (1: green; 2: blue)
	public int[] getTabgrisHisto(BufferedImage ima)
	{
		int[] tab = new int[256];
		int rgb = 0;

		//initialisation du tableau � 0
		for(int i = 0; i<256; i++)
		{
			tab[i] = 0;
		}

		//cacule des effectifs de chaques valeurs
		for(int i = 0; i<ima.getTileWidth(); i++)
		{
			for(int j = 0; j<ima.getTileHeight(); j++)
			{
				rgb = ima.getRGB(i, j);
				tab[getR(rgb)]++;	//remarque: les trois composantes on la m�me valeurs sur une image en noir et blanc
			}
		}

		return tab;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	} 

	public void tracer (BufferedImage image, int xPrec, int yPrec, int xCour, int yCour){
		//System.out.println(xPrec+" "+yPrec+" "+xCour+" "+yCour);
		for (int i=xPrec; i<= xCour; i++){
			image.setRGB(i, yPrec,0);
			image.setRGB(i, yCour,0);
		}
		for (int j=yPrec; j<= yCour; j++){
			image.setRGB(xPrec, j,0);
			image.setRGB(xCour, j,0);
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
}
