package Modele;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import Vue.CadreImage;
import Vue.InterfaceGraphique;

//classe outils
public class TraiteurImage {

	//Decouper
	//Couleur pixel
	//Redimensionner
	//Segmenter
	//Transformation
	//Filtre

//	private static final float[] MOYENNE = {
//		0.11111f, 0.11111f, 0.11111f,
//		0.11111f, 0.11111f, 0.11111f,
//		0.11111f, 0.11111f, 0.11111f};
	private static final float[][] MOYENNE = {
		{0.11111f, 0.11111f, 0.11111f},
		{0.11111f, 0.11111f, 0.11111f},
		{0.11111f, 0.11111f, 0.11111f}};

	public BufferedImage convoluer(TypeFiltre type, BufferedImage buf_ima_in)
	{
		//float[] tab = null;
		float[][] tab = null;

		switch (type){
			case MOYENNEUR:
				tab = TraiteurImage.MOYENNE;
			break;
		}



		BufferedImage dst = new BufferedImage(buf_ima_in.getWidth(), buf_ima_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//Kernel ker = new Kernel(noyau.length, noyau[0].length, TabTabIntToTabFloat(noyau));

//		Kernel ker = new Kernel(3,3,tab);
//		ConvolveOp convolveOp = new ConvolveOp(ker, ConvolveOp.EDGE_NO_OP,null);
//
//		convolveOp.filter(buf_ima_in,dst);
		
		

		return dst;
	}
	//convertir un tableau 2 dimensions d'entiers en un vecteur de float
	private float[] TabTabIntToTabFloat(int[][] in)
	{
		float[] out = new float[in.length*in[0].length];

		for(int i = 0; i<in.length; i++)
		{
			for(int j=0; j<in[0].length; j++)
			{
				out[i*in[0].length+j] = in[i][j];
			}
		}

		return out;
	}

	//Convolution pour des noyaux CARRE de TAILLE IMPAIRE
//	public CadreImage convoluer(float[][] noyau, CadreImage cadreImage, ModeConvolution mode)
//	{
//		int nbCaseEnPlus = noyau.length-1;
//		BufferedImage buf_ima_out = new BufferedImage(cadreImage.getImage().getWidth()+nbCaseEnPlus, 
//				cadreImage.getImage().getHeight()+nbCaseEnPlus, BufferedImage.TYPE_INT_ARGB);
//		BufferedImage buf_ima_tmp = new BufferedImage(cadreImage.getImage().getWidth()+nbCaseEnPlus, 
//				cadreImage.getImage().getHeight()+nbCaseEnPlus, BufferedImage.TYPE_INT_ARGB);
//
//
//
//		//METHODE : on calcule une convolution full puis on retournera les portions utiles suivant le mode
//
//
//
//		//on ajoute les 0 sur les contours pour calculer les convolutions des bords
//		int i=0, j=0;
//		for(i=0; i<nbCaseEnPlus/2; i++)
//		{
//			for(j=0; j<nbCaseEnPlus/2; j++)
//			{
//				buf_ima_tmp.setRGB(i, j, 0);
//				buf_ima_tmp.setRGB(buf_ima_tmp.getWidth()-i-1, buf_ima_tmp.getHeight()-j-1, 0);
//				buf_ima_tmp.setRGB(buf_ima_tmp.getWidth()-i-1, j, 0);
//				buf_ima_tmp.setRGB(i, buf_ima_tmp.getHeight()-j-1, 0);
//				buf_ima_out.setRGB(i, j, 0);
//				buf_ima_out.setRGB(buf_ima_tmp.getWidth()-i-1, buf_ima_tmp.getHeight()-j-1, 0);
//				buf_ima_out.setRGB(buf_ima_tmp.getWidth()-i-1, j, 0);
//				buf_ima_out.setRGB(i, buf_ima_tmp.getHeight()-j-1, 0);
//			}
//		}
//
//
//
//		//on copie l'image_d'entr�e dans sa zone
//		for(i=nbCaseEnPlus/2; i<buf_ima_tmp.getWidth()-(nbCaseEnPlus/2)-1; i++)
//		{
//			for(j=nbCaseEnPlus/2; j<buf_ima_tmp.getHeight()-(nbCaseEnPlus/2)-1; j++)
//			{
//				buf_ima_tmp.setRGB(i, j, cadreImage.getImage().getRGB(i,  j));
//			}
//		}
//
//
//
//		//on calcule la CONVOLUTION pour chaque case
//		float[][] ker = inverserNoyau(noyau);
//		float tmp, coefs;
//
//		for(i=nbCaseEnPlus/2; i<buf_ima_tmp.getWidth()-(nbCaseEnPlus/2)-1; i++)
//		{
//			for(j=nbCaseEnPlus/2; j<buf_ima_tmp.getHeight()-(nbCaseEnPlus/2)-1; j++)
//			{
//				tmp=0;
//				coefs=0;
//
//				for(int x=-nbCaseEnPlus/2; x<nbCaseEnPlus/2; x++)
//				{
//					for(int y=-nbCaseEnPlus/2; y<nbCaseEnPlus/2; y++)
//					{
//						//tmp += cadreImage.getImage().getRGB(i+x, j+y)*ker[x+nbCaseEnPlus/2][y+nbCaseEnPlus/2];
//
//						tmp += operationConvolution(cadreImage.getImage().getRGB(i+x, j+y), ker[x+nbCaseEnPlus/2][y+nbCaseEnPlus/2]);
//
//						coefs += ker[x+nbCaseEnPlus/2][y+nbCaseEnPlus/2];
//					}
//				}
//
//				if(coefs>1) tmp = tmp/coefs;
//				buf_ima_out.setRGB(i, j, tmp);
//			}
//		}
//
//
//
//
//		//si le mode est same on retourne les portions sans les 0 ajout�s.
//		if(mode == ModeConvolution.SAME)
//		{
//			BufferedImage buf_ima_same = new BufferedImage(cadreImage.getImage().getWidth(), 
//					cadreImage.getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//			for(i=nbCaseEnPlus/2; i<buf_ima_tmp.getWidth()-(nbCaseEnPlus/2)-1; i++)
//			{
//				for(j=nbCaseEnPlus/2; j<buf_ima_tmp.getHeight()-(nbCaseEnPlus/2)-1; j++)
//				{
//					//System.out.println("i : "+i+", j : "+j);
//					buf_ima_same.setRGB(i-nbCaseEnPlus/2, j-nbCaseEnPlus/2, cadreImage.getImage().getRGB(i,  j));
//				}
//			}
//
//			cadreImage.setImage(buf_ima_same);
//
//		}
//
//
//		//si le mode est valid on retourne que les portions calcul�es sans les 0
//		else if(mode == ModeConvolution.VALID)
//		{
//			BufferedImage buf_ima_valid = new BufferedImage(cadreImage.getImage().getWidth()-nbCaseEnPlus, 
//					cadreImage.getImage().getHeight()-nbCaseEnPlus, BufferedImage.TYPE_INT_ARGB);
//
//			for(i=nbCaseEnPlus; i<buf_ima_tmp.getWidth()-nbCaseEnPlus; i++)
//			{
//				for(j=nbCaseEnPlus; j<buf_ima_tmp.getHeight()-nbCaseEnPlus; j++)
//				{
//					buf_ima_valid.setRGB(i-nbCaseEnPlus, j-nbCaseEnPlus, cadreImage.getImage().getRGB(i, j));
//				}
//			}
//
//			cadreImage.setImage(buf_ima_valid);
//		}
//
//
//		//si le mode est full on retourne tout (avec les 0)
//		else cadreImage.setImage(buf_ima_out);
//
//
//
//		return cadreImage;
//	}
//
//
//	private float[][] inverserNoyau(float[][] in)
//	{
//		float[][] out = new float[in.length][in[1].length];
//
//		for(int i = 0; i<in.length; i++)
//		{
//			for(int j = 0; j<in[1].length; j++)
//			{
//				out[in.length-i-1][in[1].length-j-1] = in[i][j];
//			}
//		}
//
//		return out;
//	}
//
//	private int operationConvolution(int rgb, int coefNoyau)
//	{
//		Outil outil = new Outil();
//		int valR = outil.getR(outil.getR(rgb)*coefNoyau);
//		int valG = outil.getG(outil.getG(rgb)*coefNoyau);
//		int valB = outil.getB(outil.getB(rgb)*coefNoyau);
//		return valR+valG+valB;
//	}
//


}
