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



	public BufferedImage convoluer(int[][] noyau, BufferedImage buf_ima_in)
	{
		BufferedImage dst = new BufferedImage(buf_ima_in.getWidth(), buf_ima_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Kernel ker = new Kernel(noyau.length, noyau[0].length, TabTabIntToTabFloat(noyau));
		ConvolveOp convolveOp = new ConvolveOp(ker);

		convolveOp.filter(buf_ima_in,dst);

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


	
}
