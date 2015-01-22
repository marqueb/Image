package Modele;

import java.awt.image.BufferedImage;

//classe outils
public class TraiteurImage {
	private Outil outil;
	//Decouper
	//Couleur pixel
	//Redimensionner
	//Segmenter
	//Transformation
	//Filtre

	public TraiteurImage(Outil outil){
		this.outil=outil;
	}

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

		if(noyau == null) return null;
		return convoluer(noyau, buf_ima_in, existeSelection, selection);
	}


	public BufferedImage convoluer(float[][] ker_in, BufferedImage bufIma_in, boolean existeSelection, int[] selection)
	{
		float[][] ker = inverserNoyau(ker_in);
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = (ker.length-1)/2;
		int i=0, j=0;


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
				bufIma_out.setRGB(i, j, convolutionOneStep(bufIma_in, i, j, ker)+outil.setAlpha(255));
			}
		}

		return bufIma_out;
	}

	public BufferedImage convoluerFiltreMedian(BufferedImage bufIma_in, int nbVoisin, boolean existeSelection, int[] selection)
	{
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		int decalageBord = nbVoisin;
		int i=0, j=0;


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
				bufIma_out.setRGB(i, j, FiltreConvolution.GetValueFiltreMedian(i, j, nbVoisin, bufIma_in)+outil.setAlpha(255));
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

				im_out.setRGB(i,  j,  outil.setR(valR)+outil.setG(valG)+outil.setB(valB)+outil.setAlpha(255));
			}
		}

		return im_out;
	}
	
	public BufferedImage  redimenssioner(int largeur, int hauteur,	int newlargeur, int newhauteur, BufferedImage image) {

		Outil outil = new Outil();
		int pixel;
		BufferedImage newimage= new BufferedImage(newlargeur, newhauteur,BufferedImage.TYPE_INT_ARGB);
		double ratio=(double)((double)(newlargeur*newhauteur)/(double)(largeur*hauteur));
		double ratiocourant;
		/*	for (int i=0;i<newlargeur;i++){
			for(int j=0; j<newhauteur; j++){

				pixel=image.getRGB((int)(i/ratio),(int)(j/ratio));
				newimage.setRGB(i, j, outil.setR(outil.getR(pixel))+outil.setB(outil.getB(pixel))+outil.setG(outil.getG(pixel))+outil.setAlpha(255));				

				if(ratiocourant!=0){
					newimage.setRGB(i, j, outil.setR(outil.getR(pixel))+outil.setB(outil.getB(pixel))+outil.setG(outil.getG(pixel))+outil.setAlpha(255));	
				}
			}*/
		int r,g,b,icourant=0,jcourant=0;System.out.println(icourant+" "+jcourant);
		double reste=0;
		for (int i=0;i<largeur;i++){
			jcourant=0;
			for(int j=0; j<hauteur; j++){
				ratiocourant=ratio;
				pixel=image.getRGB(i,j);
				r=outil.getR(pixel);
				g=outil.getG(pixel);
				b=outil.getB(pixel);
				while(ratiocourant>0){
					if(reste!=0){
						//newimage.setRGB(icourant, jcourant, outil.setR((int) (r*reste))+outil.setB((int) (g*reste))+outil.setG((int) (g*reste))+outil.setAlpha(255));
						jcourant++;
						reste=0;
					}
					//newimage.setRGB(icourant, jcourant, outil.setR(r)+outil.setB(g)+outil.setG(g)+outil.setAlpha(255));
					ratiocourant--;
				}
				if(ratio+1!=0){
					//newimage.setRGB(icourant, jcourant, outil.setR((int) (r*ratio))+outil.setB((int) (g*ratio))+outil.setG((int) (g*ratio))+outil.setAlpha(255));
					reste=ratio+1;
				}

				jcourant++;	
			}
			icourant++;
		}

		//double ratiocourant=0;


		return newimage;
	}


	public BufferedImage redimenssionerLargeur(BufferedImage image, int newLargeur){
		BufferedImage nouvelle=outil.resize(image, newLargeur, image.getHeight());
		double ratio = ((double)newLargeur)/((double)image.getWidth());
		double k, reste, ratioCourant;
		for(int j=0; j<image.getHeight();j++){
			for(int i=0; i<image.getWidth();i++){
				k=i*ratio;
				reste=k;
				while(reste>1){
					reste--;
				}
				ratioCourant=k-(1-reste);
				k=k-reste;
				nouvelle.setRGB(((int)k), j,((int) (nouvelle.getRGB(((int)k), j)+(1-reste)*image.getRGB(i, j))));
				k++;
				while(ratioCourant>1){
					nouvelle.setRGB(((int)k),  j, image.getRGB(i, j));
					k++;
					ratioCourant--;
				}
				nouvelle.setRGB(((int)k), j,((int) (ratioCourant)*image.getRGB(i, j)));				
			}
		}
		return nouvelle;
	}

	public void redimenssionerHauteur(BufferedImage image, int newHauteur){
		//cadreImage.setImage(outil.resize(cadreImage.getImage(), newW, newH));
	}
}

