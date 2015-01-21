package Modele;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import javax.swing.ImageIcon;

import Vue.CadreImage;

//classe outils
public class TraiteurImage {

	//Decouper
	//Couleur pixel
	//Redimensionner
	//Segmenter
	//Transformation
	//Filtre

	private static final float[][] MOYENNE = {
		{1.0f, 1.0f, 1.0f},
		{1.0f, 1.0f, 1.0f},
		{1.0f, 1.0f, 1.0f}};
	//private static float unNeuvieme = Float.valueOf((float)1/(float)9);
	//	private static final float[][] MOYENNE = {
	//		{unNeuvieme, unNeuvieme, unNeuvieme},
	//		{unNeuvieme, unNeuvieme, unNeuvieme},
	//		{unNeuvieme, unNeuvieme, unNeuvieme}};

	public BufferedImage convoluer(TypeFiltre type, BufferedImage buf_ima_in, boolean existeSelection, int[] selection)
	{
		//float[] noyau = null;
		float[][] noyau = null;

		switch (type){
		case MOYENNEUR:
			noyau = TraiteurImage.MOYENNE;
			break;
		}

		//BufferedImage dst = new BufferedImage(buf_ima_in.getWidth(), buf_ima_in.getHeight(), BufferedImage.TYPE_INT_ARGB);

		//		Kernel ker = new Kernel(3,3,noyau);
		//		ConvolveOp convolveOp = new ConvolveOp(ker, ConvolveOp.EDGE_NO_OP,null);
		//
		//		convolveOp.filter(buf_ima_in,dst);
		//		return dst;
		//return convoluer(noyau, buf_ima_in, ModeConvolution.SAME);
		if(noyau == null) return null;
		return convoluer(noyau, buf_ima_in, existeSelection, selection);
	}


	public BufferedImage convoluer(float[][] ker_in, BufferedImage bufIma_in, boolean existeSelection, int[] selection)
	{
		float[][] ker = inverserNoyau(ker_in);
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = (ker.length-1)/2;
		int i=0, j=0;

		//on recopie les parties ou on n'applique pas la convolution (on applique la convolution sur la partie "VALID" de l'image).
		//		for(i=0; i<decalageBord; i++)
		//		{
		//			for(j=0; j<decalageBord; j++)
		//			{
		//				bufIma_out.setRGB(i, j, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, j, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(i, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
		//			}
		//		}
		int i_deb, i_fin, j_deb, j_fin;
		if(existeSelection && selection[0]>=decalageBord) i_deb=selection[0];
		else i_deb = decalageBord;
		if(existeSelection && selection[2]<=bufIma_in.getWidth()-decalageBord-1) i_fin=selection[2];
		else i_fin = bufIma_in.getWidth()-decalageBord-1;
		if(existeSelection && selection[1]>=decalageBord) j_deb=selection[1];
		else j_deb = decalageBord;
		if(existeSelection && selection[3]<=bufIma_in.getHeight()-decalageBord-1) j_fin=selection[3];
		else j_fin = bufIma_in.getHeight()-decalageBord-1;

		//on applique la convolution � la partie "VALID" de l'image.
		for(i=i_deb; i<i_fin; i++)
		{
			for(j=j_deb; j<j_fin; j++)
			{
				bufIma_out.setRGB(i, j, convolutionOneStep(bufIma_in, i, j, ker));
			}
		}

		return bufIma_out;
	}

	public BufferedImage convoluerFiltreMedian(BufferedImage bufIma_in, int nbVoisin, boolean existeSelection, int[] selection)
	{
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = nbVoisin;
		int i=0, j=0;

		//on recopie les parties ou on n'applique pas la convolution (on applique la convolution sur la partie "VALID" de l'image).
		//		for(i=0; i<decalageBord; i++)
		//		{
		//			for(j=0; j<decalageBord; j++)
		//			{
		//				bufIma_out.setRGB(i, j, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, j, bufIma_in.getRGB(i,  j));
		//				bufIma_out.setRGB(i, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
		//			}
		//		}
		int i_deb, i_fin, j_deb, j_fin;
		if(existeSelection && selection[0]>=decalageBord) i_deb=selection[0];
		else i_deb = decalageBord;
		if(existeSelection && selection[2]<=bufIma_in.getWidth()-decalageBord-1) i_fin=selection[2];
		else i_fin = bufIma_in.getWidth()-decalageBord-1;
		if(existeSelection && selection[1]>=decalageBord) j_deb=selection[1];
		else j_deb = decalageBord;
		if(existeSelection && selection[3]<=bufIma_in.getHeight()-decalageBord-1) j_fin=selection[3];
		else j_fin = bufIma_in.getHeight()-decalageBord-1;

		//on applique la convolution � la partie "VALID" de l'image.
		for(i=i_deb; i<i_fin; i++)
		{
			for(j=j_deb; j<j_fin; j++)
			{
				bufIma_out.setRGB(i, j, FiltreConvolution.GetValueFiltreMedian(i, j, nbVoisin, bufIma_in));
			}
		}
		return bufIma_out;
	}

	//retourne le noyau de convolution
	private float[][] inverserNoyau(float[][] in)
	{
		float[][] out = new float[in.length][in[0].length];

		for(int i = 0; i<in.length; i++)
		{
			for(int j = 0; j<in[0].length; j++)
			{
				out[in.length-i-1][in[0].length-j-1] = in[i][j];
			}
		}

		return out;
	}

	//applique un pas de convolution (centr�e sur x, y)
	private int convolutionOneStep(BufferedImage ima, int x, int y, float[][] ker)
	{
		Outil outil = new Outil();
		int valR = 0, valG = 0, valB = 0, rgb = 0, resPix = 0, sommeCoef = 0;
		int decalageBord = (ker.length-1)/2;

		for(int i=-decalageBord; i<=decalageBord; i++)
		{
			for(int j=-decalageBord; j<=decalageBord; j++)
			{
				rgb = ima.getRGB(x+i, y+j);
				valR += outil.getR(rgb)*ker[i+decalageBord][j+decalageBord];
				valG += outil.getG(rgb)*ker[i+decalageBord][j+decalageBord];
				valB += outil.getB(rgb)*ker[i+decalageBord][j+decalageBord];
				sommeCoef += ker[i+decalageBord][j+decalageBord];
			}
		}

		if(sommeCoef>1)
		{
			valR = Outil.getValidValuePixel(valR/sommeCoef);
			valG = Outil.getValidValuePixel(valG/sommeCoef);
			valB = Outil.getValidValuePixel(valB/sommeCoef);
		}
		else
		{
			valR = Outil.getValidValuePixel(valR);
			valG = Outil.getValidValuePixel(valG);
			valB = Outil.getValidValuePixel(valB);
		}

		resPix = outil.setR(valR)+outil.setG(valG)+outil.setB(valB);
		return resPix;
	}

	public BufferedImage rehausserContours(BufferedImage im, boolean existeSelection, int[] selection)
	{
		float alpha = 1.0f/8.0f;
		int rgb_im, rgb_laplacien, valR, valG, valB;
		BufferedImage im_out = Outil.deepCopy(im);
		Outil outil = new Outil();

		BufferedImage im_laplacien = convoluer(FiltreConvolution.getNoyauLaplacien3x3(), im, existeSelection, selection);

		int i_deb, i_fin, j_deb, j_fin;
		if(existeSelection){
			i_deb=selection[0];
			i_fin=selection[2];
			j_deb=selection[1];
			j_fin=selection[3];
		}else{
			i_deb=0;
			i_fin=im.getWidth();
			j_deb=0;
			j_fin=im.getHeight();
		}

		for(int i = i_deb; i<i_fin; i++)
		{
			for(int j = j_deb; j<j_fin; j++)
			{
				rgb_im = im.getRGB(i, j);
				rgb_laplacien = im_laplacien.getRGB(i, j);

				valR = Outil.getValidValuePixel((int)(outil.getR(rgb_im) - alpha * outil.getR(rgb_laplacien)));
				valG = Outil.getValidValuePixel((int)(outil.getG(rgb_im) - alpha * outil.getG(rgb_laplacien)));
				valB = Outil.getValidValuePixel((int)(outil.getB(rgb_im) - alpha * outil.getB(rgb_laplacien)));

				im_out.setRGB(i,  j,  outil.setR(valR)+outil.setG(valG)+outil.setB(valB));
			}
		}

		return im_out;
	}



	public BufferedImage redimenssioner(int largeur, int hauteur,	int newlargeur, int newhauteur) {
		Outil outil = new Outil();
		BufferedImage image= new BufferedImage(newlargeur, newhauteur,BufferedImage.TYPE_INT_ARGB);
		for (int i=0;i<newlargeur;i++){
			for(int j=0; j<newhauteur; j++){
				image.setRGB(i, j, outil.setR(0)+outil.setB(0)+outil.setG(150));
			}
		}
		
		//cadre= new CadreImage(image);
		//cadre.update(buffer);
//		cadre.setImage(image);
		//cadre.setBuffer((Graphics2D) image.getGraphics());
		//cadre.setImageIcon(new ImageIcon(image));
		//cadre.repaint();
		return image;
	}
}
