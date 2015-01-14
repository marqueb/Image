package Modele;

import java.awt.image.BufferedImage;

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

	public BufferedImage convoluer(TypeFiltre type, BufferedImage buf_ima_in)
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
		return convoluer(noyau, buf_ima_in);
	}


	public BufferedImage convoluer(float[][] ker_in, BufferedImage bufIma_in)
	{
		float[][] ker = inverserNoyau(ker_in);
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = (ker.length-1)/2;
		int i=0, j=0;
		
		//on recopie les parties ou on n'applique pas la convolution (on applique la convolution sur la partie "VALID" de l'image).
		for(i=0; i<decalageBord; i++)
		{
			for(j=0; j<decalageBord; j++)
			{
				bufIma_out.setRGB(i, j, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, j, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(i, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
			}
		}

		//on applique la convolution � la partie "VALID" de l'image.
		for(i=decalageBord; i<bufIma_in.getWidth()-decalageBord-1; i++)
		{
			for(j=decalageBord; j<bufIma_in.getHeight()-decalageBord-1; j++)
			{
				bufIma_out.setRGB(i, j, convolutionOneStep(bufIma_in, i, j, ker));
			}
		}

		return bufIma_out;
	}
	
	public BufferedImage convoluerFiltreMedian(BufferedImage bufIma_in, int nbVoisin)
	{
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = nbVoisin;
		int i=0, j=0;
		
		//on recopie les parties ou on n'applique pas la convolution (on applique la convolution sur la partie "VALID" de l'image).
		for(i=0; i<decalageBord; i++)
		{
			for(j=0; j<decalageBord; j++)
			{
				bufIma_out.setRGB(i, j, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(bufIma_in.getWidth()-i-1, j, bufIma_in.getRGB(i,  j));
				bufIma_out.setRGB(i, bufIma_in.getHeight()-j-1, bufIma_in.getRGB(i,  j));
			}
		}

		//on applique la convolution � la partie "VALID" de l'image.
		for(i=decalageBord; i<bufIma_in.getWidth()-decalageBord-1; i++)
		{
			for(j=decalageBord; j<bufIma_in.getHeight()-decalageBord-1; j++)
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
	
	public BufferedImage rehausserContours(BufferedImage im)
	{
		float alpha = 1.0f/8.0f;
		int rgb_im, rgb_laplacien, valR, valG, valB;
		BufferedImage im_out = Outil.deepCopy(im);
		int width = im.getWidth(), height = im.getHeight();
		Outil outil = new Outil();
		
		BufferedImage im_laplacien = convoluer(FiltreConvolution.getNoyauLaplacien3x3(), im);
		
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
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
}
