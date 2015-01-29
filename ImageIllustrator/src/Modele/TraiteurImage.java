package Modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//classe outils
public class TraiteurImage {
	private Outil outil;
	//Decouper
	//Couleur pixel
	//Redimensionner
	//Segmenter
	//Transformation
	//Filtre
	private CaseTableauChemins[][] cheminsRecopie;

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
		BufferedImage im_agr = null, tmp = null;
		int decalageBord = (ker.length-1)/2;
		int i=0, j=0;
		//si la convolution est sur toute l'image ou trop pr�s d'un bord
		//agrandir l'image avec les zones ajout�es initialis�es � 0
		if(selection==null || 
				(selection[0]<=decalageBord && selection[2]>=bufIma_in.getWidth()-decalageBord-1
				&& selection[1]<=decalageBord && selection[3]>=bufIma_in.getHeight()-decalageBord-1))
		{
			im_agr = new BufferedImage(bufIma_in.getWidth()+2*decalageBord, bufIma_in.getHeight()+2*decalageBord,BufferedImage.TYPE_INT_ARGB);

			for(i=0; i<decalageBord; i++)
			{
				im_agr.setRGB(i, 0, 0);
				im_agr.setRGB(im_agr.getWidth()-1-i, 0, outil.setR(0)+outil.setG(0)+outil.setB(0)+outil.setAlpha(255));
			}
			for(j=0; j<decalageBord; j++)
			{
				im_agr.setRGB(0, j, 0);
				im_agr.setRGB(0, im_agr.getHeight()-1-decalageBord, 0);
			}
			for(i=0; i<bufIma_in.getWidth()-1; i++)
			{
				for(j=0; j<bufIma_in.getHeight()-1; j++)
				{
					im_agr.setRGB(decalageBord+i, decalageBord+j, bufIma_in.getRGB(i, j));
				}
			}
		}
		else//pas besoin d'ajouter des z�ros autour de l'image
		{
			im_agr = bufIma_in;
		}

		int i_deb, i_fin, j_deb, j_fin;
		if(existeSelection && selection[0]>=decalageBord) i_deb=selection[0];
		else i_deb = decalageBord;
		if(existeSelection && selection[2]<=bufIma_in.getWidth()-decalageBord-1) i_fin=selection[2];
		else i_fin = im_agr.getWidth()-1-decalageBord;
		if(existeSelection && selection[1]>=decalageBord) j_deb=selection[1];
		else j_deb = decalageBord;
		if(existeSelection && selection[3]<=bufIma_in.getHeight()-decalageBord-1) j_fin=selection[3];
		else j_fin = im_agr.getHeight()-1-decalageBord;

		tmp = new BufferedImage(im_agr.getWidth(), im_agr.getHeight(),BufferedImage.TYPE_INT_ARGB);
		//on applique la convolution � la partie "Same" de l'image.
		for(i=i_deb; i<i_fin; i++)
		{
			for(j=j_deb; j<j_fin; j++)
			{
				tmp.setRGB(i, j, convolutionOneStep(im_agr, i, j, ker)+outil.setAlpha(255));
			}
		}

		//supprimer les bords ajout�s
		if(selection==null || 
				(selection[0]<=decalageBord && selection[2]>=bufIma_in.getWidth()-decalageBord-1
				&& selection[1]<=decalageBord && selection[3]>=bufIma_in.getHeight()-decalageBord-1))
		{
			for(i=0; i<bufIma_in.getWidth()-1; i++)
			{
				for(j=0; j<bufIma_in.getHeight()-1; j++)
				{
					bufIma_out.setRGB(i, j, tmp.getRGB(decalageBord+i, decalageBord+j));
				}
			}
		}
		else//si pas de bords ajout�s
		{
			bufIma_out = tmp;
		}

		return bufIma_out;
	}

	public BufferedImage convoluerFiltreMedian(BufferedImage bufIma_in, int nbVoisin, boolean existeSelection, int[] selection)
	{
		BufferedImage bufIma_out = Outil.deepCopy(bufIma_in);
		BufferedImage im_agr = null, tmp = null;
		int decalageBord = nbVoisin;
		int i=0, j=0;

		//si la convolution est sur toute l'image ou trop pr�s d'un bord
		//agrandir l'image avec les zones ajout�es initialis�es � 0
		if(selection==null || 
				(selection[0]<=decalageBord && selection[2]>=bufIma_in.getWidth()-decalageBord-1
				&& selection[1]<=decalageBord && selection[3]>=bufIma_in.getHeight()-decalageBord-1))
		{
			im_agr = new BufferedImage(bufIma_in.getWidth()+2*decalageBord, bufIma_in.getHeight()+2*decalageBord,BufferedImage.TYPE_INT_ARGB);

			for(i=0; i<decalageBord; i++)
			{
				im_agr.setRGB(i, 0, 0);
				im_agr.setRGB(im_agr.getWidth()-1-i, 0, outil.setR(0)+outil.setG(0)+outil.setB(0)+outil.setAlpha(255));
			}
			for(j=0; j<decalageBord; j++)
			{
				im_agr.setRGB(0, j, 0);
				im_agr.setRGB(0, im_agr.getHeight()-1-decalageBord, 0);
			}
			for(i=0; i<bufIma_in.getWidth()-1; i++)
			{
				for(j=0; j<bufIma_in.getHeight()-1; j++)
				{
					im_agr.setRGB(decalageBord+i, decalageBord+j, bufIma_in.getRGB(i, j));
				}
			}
		}
		else//pas besoin d'ajouter des z�ros autour de l'image
		{
			im_agr = tmp;
		}


		int i_deb, i_fin, j_deb, j_fin;
		if(existeSelection && selection[0]>=decalageBord) i_deb=selection[0];
		else i_deb = decalageBord;
		if(existeSelection && selection[2]<=bufIma_in.getWidth()-decalageBord-1) i_fin=selection[2];
		else i_fin = bufIma_in.getWidth()-decalageBord-1;
		if(existeSelection && selection[1]>=decalageBord) j_deb=selection[1];
		else j_deb = decalageBord;
		if(existeSelection && selection[3]<=bufIma_in.getHeight()-decalageBord-1) j_fin=selection[3];
		else j_fin = bufIma_in.getHeight()-decalageBord-1;

		tmp = new BufferedImage(im_agr.getWidth(), im_agr.getHeight(),BufferedImage.TYPE_INT_ARGB);

		//on applique la convolution � la partie "SAME" de l'image.
		for(i=i_deb; i<i_fin; i++)
		{
			for(j=j_deb; j<j_fin; j++)
			{
				tmp.setRGB(i, j, FiltreConvolution.GetValueFiltreMedian(i, j, nbVoisin, im_agr)+outil.setAlpha(255));
			}
		}

		//supprimer les bords ajout�s
		if(selection==null || 
				(selection[0]<=decalageBord && selection[2]>=bufIma_in.getWidth()-decalageBord-1
				&& selection[1]<=decalageBord && selection[3]>=bufIma_in.getHeight()-decalageBord-1))
		{
			for(i=0; i<bufIma_in.getWidth()-1; i++)
			{
				for(j=0; j<bufIma_in.getHeight()-1; j++)
				{
					bufIma_out.setRGB(i, j, tmp.getRGB(decalageBord+i, decalageBord+j));
				}
			}
		}
		else//si pas de bords ajout�s
		{
			bufIma_out = tmp;
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

	public BufferedImage redimenssionerLargeur(BufferedImage image, int newLargeur){
		BufferedImage nouvelle=new BufferedImage(newLargeur,image.getHeight(), image.getType());
		int a, b, c, d;
		double ratio = ((double)newLargeur)/((double)image.getWidth());
		double k, reste, ratioCourant;
		for(int j=0; j<image.getHeight();j++){
			a=0;
			b=0;
			c=0;
			d=0;
			k=0;
			for(int i=0; i<image.getWidth();i++){
				k=((double)i)*ratio;
				reste=k;
				while(reste>1){
					reste--;
				}
				ratioCourant=ratio-(1-reste);
				k=k-reste;
				a=a+((int)(((double)outil.getAlpha(nouvelle.getRGB(((int)k), j)))+((double)1-reste)*((double)outil.getAlpha(image.getRGB(i, j)))));
				b=b+((int)(((double)outil.getR(nouvelle.getRGB(((int)k), j)))+((double)1-reste)*((double)outil.getR(image.getRGB(i, j)))));
				c=c+((int)(((double)outil.getG(nouvelle.getRGB(((int)k), j)))+((double)1-reste)*((double)outil.getG(image.getRGB(i, j)))));
				d=d+((int)(((double)outil.getB(nouvelle.getRGB(((int)k), j)))+((double)1-reste)*((double)outil.getB(image.getRGB(i, j)))));
				nouvelle.setRGB(((int)k), j,outil.setAlpha(a)+outil.setR(b)+outil.setG(c)+outil.setB(d));
				k++;
				while(ratioCourant>1){
					nouvelle.setRGB(((int)k), j, image.getRGB(i, j));
					k++;
					ratioCourant--;
				}
				a=((int)((ratioCourant)*((double)outil.getAlpha(image.getRGB(i, j)))));
				b=((int)((ratioCourant)*((double)outil.getR(image.getRGB(i, j)))));
				c=((int)((ratioCourant)*((double)outil.getG(image.getRGB(i, j)))));
				d=((int)((ratioCourant)*((double)outil.getB(image.getRGB(i, j)))));
			}
			if(k<nouvelle.getWidth())
				nouvelle.setRGB(((int)k), j,outil.setAlpha(a)+outil.setR(b)+outil.setG(c)+outil.setB(d));
		}
		return nouvelle;
	}

	public BufferedImage redimenssionerHauteur(BufferedImage image, int newHauteur){
		BufferedImage nouvelle=new BufferedImage(image.getWidth(), newHauteur, image.getType());
		int a, b, c, d;
		double ratio = ((double)newHauteur)/((double)image.getHeight());
		double k, reste, ratioCourant;
		for(int j=0; j<image.getWidth();j++){
			a=0;
			b=0;
			c=0;
			d=0;
			k=0;
			for(int i=0; i<image.getHeight();i++){
				k=((double)i)*ratio;
				reste=k;
				while(reste>1){
					reste--;
				}
				ratioCourant=ratio-(1-reste);
				k=k-reste;
				a=a+((int)(((double)outil.getAlpha(nouvelle.getRGB(j,((int)k))))+((double)1-reste)*((double)outil.getAlpha(image.getRGB(j,i)))));
				b=b+((int)(((double)outil.getR(nouvelle.getRGB(j,((int)k))))+((double)1-reste)*((double)outil.getR(image.getRGB(j,i)))));
				c=c+((int)(((double)outil.getG(nouvelle.getRGB(j,((int)k))))+((double)1-reste)*((double)outil.getG(image.getRGB(j,i)))));
				d=d+((int)(((double)outil.getB(nouvelle.getRGB(j,((int)k))))+((double)1-reste)*((double)outil.getB(image.getRGB(j,i)))));
				nouvelle.setRGB( j,((int)k),outil.setAlpha(a)+outil.setR(b)+outil.setG(c)+outil.setB(d));
				k++;
				while(ratioCourant>1){
					nouvelle.setRGB(j,((int)k),  image.getRGB(j,i));
					k++;
					ratioCourant--;
				}
				a=((int)((ratioCourant)*((double)outil.getAlpha(image.getRGB(j,i)))));
				b=((int)((ratioCourant)*((double)outil.getR(image.getRGB(j,i)))));
				c=((int)((ratioCourant)*((double)outil.getG(image.getRGB(j,i)))));
				d=((int)((ratioCourant)*((double)outil.getB(image.getRGB(j,i)))));
			}
			if(k<nouvelle.getHeight())
				nouvelle.setRGB(j,((int)k),outil.setAlpha(a)+outil.setR(b)+outil.setG(c)+outil.setB(d));
		}
		return nouvelle;
	}

	public BufferedImage redimensionnerIntelligement(int largeur, int hauteur,	int newlargeur, int newhauteur, BufferedImage image) 
	{
		//		System.out.println("(largeur, hauteur) = ("+largeur+", "+hauteur+"); (newLargeur, newHauteur) = ("+newlargeur+", "+newhauteur+")");
		//si pas de redimensionnement
		if(largeur == newlargeur && hauteur == newhauteur)
		{
			return calculerImageEnergie(image);
		}

		//calculer image energie
		//		BufferedImage im_energie = calculerImageEnergie(image);
		CaseTableauChemins[][] chemins = null, newchemins = null;

		//si on a plus de modif en hauteur que en largeur alors on calcul d'abord le redimensionnement en largeur (moins de calcul)
		if(Math.abs(image.getWidth() - newlargeur) < Math.abs(image.getHeight() - newhauteur))
		{
			if(Math.abs(image.getWidth() - newlargeur)!=0)
			{
				//				//calculer les chemins les moins couteux
				//				chemins = calculerCheminsMoinsCouteuxEnHauteur(im_energie);
				//
				//				image = appliquerRedimensionnementIntelligentLargeur(image, chemins, newlargeur);
				//				//calculer image energie
				//				im_energie = calculerImageEnergie(image);
				image = appliquerRedimensionnementIntelligentLargeurCroisePas(image, newlargeur);
			}
			//calculer les chemins les moins couteux
			//			newchemins = calculerCheminsMoinsCouteuxEnLargeur(im_energie);

			image = appliquerRedimensionnementIntelligentHauteurCroisePas(image, newhauteur);
		}
		else//si on a plus de modif en hauteur que en largeur alors on calcul d'abord le redimensionnement en largeur (moins de calcul)
		{
			if(Math.abs(image.getHeight() - newhauteur)!=0)
			{
				//				//calculer les chemins les moins couteux
				//				newchemins = calculerCheminsMoinsCouteuxEnLargeur(im_energie);
				//
				//				image = appliquerRedimensionnementIntelligentHauteur(image, chemins, newhauteur);
				//
				//				//calculer image energie
				//				im_energie = calculerImageEnergie(image);
				image = appliquerRedimensionnementIntelligentHauteurCroisePas(image, newhauteur);
			}

			//calculer les chemins les moins couteux
			//			newchemins = calculerCheminsMoinsCouteuxEnHauteur(im_energie);
			//
			//			image = appliquerRedimensionnementIntelligentLargeur(image, newchemins, newlargeur);

			image = appliquerRedimensionnementIntelligentLargeurCroisePas(image, newlargeur);
			//			System.out.println("done...");
		}

		return image;
	}




	private BufferedImage calculerImageEnergie(BufferedImage image)
	{
		return this.convoluer(FiltreConvolution.getNoyauLaplacien3x3(), image/*outil.imagris(image, false, null)*/, false, null);
	}





	//	private CaseTableauChemins[][] calculerCheminsMoinsCouteuxEnLargeur(BufferedImage image)
	//	{
	//		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
	//		int i = 0, j = 0, min = 255;
	//		Direction direction = Direction.MILIEU;
	//
	//		for(j=0; j<image.getHeight(); j++)
	//		{
	//			chemins[0][j] = new CaseTableauChemins(image.getRGB(0, j), Direction.INIT);
	//		}
	//
	//		for(i = 1; i<image.getWidth(); i++)
	//		{
	//			for(j = 0; j < image.getHeight(); j++)
	//			{
	//				min = image.getRGB(i-1, j);
	//				direction = Direction.MILIEU;
	//
	//				if(j!=0 && image.getRGB(i-1, j-1) < min)
	//				{
	//					min = image.getRGB(i-1, j-1);
	//					direction = Direction.GAUCHE;
	//				}
	//				if(j<image.getHeight()-1 && image.getRGB(i-1, j+1) < min)
	//				{
	//					min = image.getRGB(i-1, j+1);
	//					direction = Direction.DROITE;
	//				}
	//
	//				chemins[i][j] = new CaseTableauChemins(image.getRGB(i, j)+min, direction);
	//			}
	//		}
	//
	//		return chemins;
	//	}




	//calcul des poids des chemins en hauteur pour trouver ensuite le moins couteux
	private CaseTableauChemins[][] calculerCheminsMoinsCouteuxEnLargeur(BufferedImage image)
	{
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		int i = 0, j = 0, min = Integer.MAX_VALUE, diffCouleur = 0, diffR = 0, diffG = 0, diffB = 0, ind_j = 0;
		Direction direction = Direction.MILIEU;

		for(j=0; j<image.getHeight(); j++)
		{
			chemins[0][j] = new CaseTableauChemins(outil.getR(image.getRGB(0, j))+outil.getG(image.getRGB(0, j))+outil.getB(image.getRGB(0, j)), Direction.INIT);
		}

		int valCurrent = 0;
		for(i = 1; i<image.getWidth(); i++)
		{
			for(j = 0; j < image.getHeight(); j++)
			{

				min = chemins[i-1][j].value;
				direction = Direction.MILIEU;
				ind_j = j;

				if(j!=0 && chemins[i-1][j-1].value<min)
				{
					min = chemins[i-1][j-1].value;
					direction = Direction.GAUCHE;
					ind_j = j-1;
				}
				if(j<image.getHeight()-1 && chemins[i-1][j+1].value < min)
				{
					min = chemins[i-1][j+1].value;
					direction = Direction.DROITE;
					ind_j = j+1;
				}
				valCurrent = outil.getR(image.getRGB(i, j))+outil.getG(image.getRGB(i, j))+outil.getB(image.getRGB(i, j));
				diffR = Math.abs(outil.getR(image.getRGB(i, j))-outil.getR(image.getRGB(i-1, ind_j)));
				diffG = Math.abs(outil.getG(image.getRGB(i, j))-outil.getG(image.getRGB(i-1, ind_j)));
				diffB = Math.abs(outil.getB(image.getRGB(i, j))-outil.getB(image.getRGB(i-1, ind_j)));
				diffCouleur = diffR+diffG+diffB;
				chemins[i][j] = new CaseTableauChemins(min+valCurrent+diffCouleur, direction);
			}
		}

		return chemins;
	}





	//calcul des poids des chemins en hauteur pour trouver ensuite le moins couteux
	private CaseTableauChemins[][] calculerCheminsMoinsCouteuxEnHauteur(BufferedImage image)
	{
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		int i = 0, j = 0, min = outil.setR(255)+outil.setB(255)+outil.setG(255);
		int diffCouleur = 0, diffR = 0, diffG = 0, diffB = 0, ind_i = 0;
		Direction direction = Direction.MILIEU;

		for(i=0; i<image.getWidth(); i++)
		{
			chemins[i][0] = new CaseTableauChemins(outil.getR(image.getRGB(i, 0))+outil.getG(image.getRGB(i, 0))+outil.getB(image.getRGB(i, 0)), Direction.INIT);
		}

		int valCurrent = 0;
		for(j = 1; j < image.getHeight(); j++)
		{
			for(i = 0; i<image.getWidth(); i++)
			{
				min = chemins[i][j-1].value;
				direction = Direction.MILIEU;
				ind_i = i;

				if(i!=0 && chemins[i-1][j-1].value<min)
				{
					min = chemins[i-1][j-1].value;
					direction = Direction.GAUCHE;
					ind_i = i-1;
				}
				if(i<image.getWidth()-1 && chemins[i+1][j-1].value < min)
				{
					min = chemins[i+1][j-1].value;
					direction = Direction.DROITE;
					ind_i = i+1;
				}
				valCurrent = outil.getR(image.getRGB(i, j))+outil.getG(image.getRGB(i, j))+outil.getB(image.getRGB(i, j));
				diffR = Math.abs(outil.getR(image.getRGB(i, j))-outil.getR(image.getRGB(ind_i, j-1)));
				diffG = Math.abs(outil.getG(image.getRGB(i, j))-outil.getG(image.getRGB(ind_i, j-1)));
				diffB = Math.abs(outil.getB(image.getRGB(i, j))-outil.getB(image.getRGB(ind_i, j-1)));
				diffCouleur = diffR+diffG+diffB;
				chemins[i][j] = new CaseTableauChemins(min+valCurrent+diffCouleur, direction);
			}
		}

		return chemins;
	}










	//	private BufferedImage appliquerRedimensionnementIntelligentLargeur(BufferedImage image, CaseTableauChemins[][] chemins, int newlargeur)
	//	{
	//		BufferedImage newimage = new BufferedImage(newlargeur, image.getHeight(),BufferedImage.TYPE_INT_ARGB);
	//
	//		int[] indicesCheminsASupprimer = trouverCheminsPoidsMinEnHauteur(chemins, Math.abs(image.getWidth() - newlargeur));
	//		//		int[] indicesCheminsASupprimer = trouverCheminsCroisePasPoidsMinEnHauteur(image, Math.abs(image.getWidth() - newlargeur));
	//
	//		if(image.getWidth() < newlargeur) //agrandir en largeur
	//		{
	//			newimage = agrandirImage("LARGEUR", image, chemins, indicesCheminsASupprimer);
	//		}
	//		else	//r�tr�cir en largeur
	//		{
	//
	//		}
	//
	//		return newimage;
	//	}

	//	private BufferedImage appliquerRedimensionnementIntelligentHauteur(BufferedImage image, CaseTableauChemins[][] chemins, int newhauteur)
	//	{
	//		BufferedImage newimage = new BufferedImage(image.getWidth(), newhauteur,BufferedImage.TYPE_INT_ARGB);
	//
	//		int[] indicesCheminsASupprimer = trouverCheminsPoidsMinEnLargeur(chemins, Math.abs(image.getHeight() - newhauteur));
	//
	//		if(image.getHeight() < newhauteur) //agrandir en hauteur
	//		{
	//
	//		}
	//		else	//r�tr�cir en hauteur
	//		{
	//
	//		}
	//
	//		return newimage;
	//	}

	//pour redimensionnement en largeur
	//	private int[] trouverCheminsPoidsMinEnLargeur(CaseTableauChemins[][] chemins, int nbCheminsATrouver)
	//	{
	//		int[] indices = new int[nbCheminsATrouver];
	//		ArrayList<Integer> dernieresCase = new ArrayList<Integer>();
	//
	//		for(int j = 0; j < chemins[0].length; j++)
	//		{
	//			dernieresCase.add(new Integer(chemins[chemins.length-1][j].value));
	//		}
	//
	//		Collections.sort(dernieresCase);
	//
	//		int cpt = 0, j = 0;
	//		while(cpt < nbCheminsATrouver && j < chemins[0].length)
	//		{//si la valeur de chemins est dans la list des valeurs minimales alors c'est l'indice d'un chemin de poids min
	//			if(dernieresCase.contains(chemins[chemins.length-1][j])) 
	//			{
	//				indices[cpt] = j;
	//				cpt++;
	//			}
	//			j++;
	//		}
	//
	//
	//		//		System.out.println(indices.toString());
	//		//		System.out.flush();
	//		return indices;
	//	}


	//1ere calcul des poids des chemins (utilis� une autre fonctions pour choisir les chemins suivants afin de ne pas
	//crois� de chemin d�j� choisi)
	//	private int[] trouverCheminsPoidsMinEnHauteur(CaseTableauChemins[][] chemins, int nbCheminsATrouver)
	//	{
	//		int[] indices = new int[nbCheminsATrouver];
	//		ArrayList<Integer> dernieresCase = new ArrayList<Integer>();
	//		int[] indiceCheminOrdreCroissant = new int[chemins.length];
	//
	//		//on r�cupere les valeurs des diff�rents chemins
	//		for(int i = 0; i < chemins.length; i++)
	//		{
	//			dernieresCase.add(new Integer(chemins[i][chemins[0].length-1].value));
	//			indiceCheminOrdreCroissant[i] = i;
	//		}
	//
	//		Integer[] derniereLigne = dernieresCase.toArray(new Integer[dernieresCase.size()]);
	//		//on tri les valeurs des chemins
	//		//Collections.sort(dernieresCase);
	//
	//		//on r�cupere les indices des chemins dans l'ordre des poids des chemins croissants
	//		int tmp = 0;
	//		for(int k=0; k<chemins.length; k++)
	//		{
	//			for(int i=k+1; i<chemins.length; i++)
	//			{
	//				if(derniereLigne[k]>derniereLigne[i])
	//				{
	//					//on �change les valeurs
	//					tmp = derniereLigne[k];
	//					derniereLigne[k] = derniereLigne[i];
	//					derniereLigne[i] = tmp;
	//					//on �change l'ordre des indices
	//					tmp = indiceCheminOrdreCroissant[k];
	//					indiceCheminOrdreCroissant[k] = indiceCheminOrdreCroissant[i];
	//					indiceCheminOrdreCroissant[i] = tmp;
	//				}
	//			}
	//		}
	//
	//		for(int i = 0; i<nbCheminsATrouver; i++)
	//		{
	//			indices[i]=-1;
	//		}
	//
	//		//on trouve les chemins de poids minimum qui ne se croise pas (m�thode non optimale)
	//		int cpt = 0, i = 0, indice = 0;;
	//		while(cpt<nbCheminsATrouver && i < chemins.length)
	//		{//si la valeur de chemins est dans la liste des valeurs minimales alors c'est l'indice d'un chemin de poids min
	//			//			if(dernieresCase.contains(chemins[i][chemins[0].length-1])) 
	//			//			{
	//			//				indices[cpt] = i;
	//			//				cpt++;
	//			//			}
	//			//			i++;
	//			//on r�cupere les indice des chemins de poids min
	//			indice = indiceCheminOrdreCroissant[i];
	//			if(cheminLibre(indice, chemins, "HAUTEUR"))
	//			{
	//				indices[cpt] = indice;
	//				cpt++;
	//				chemins = marquerChemin(indice, chemins, "HAUTEUR");
	//			}
	//			i++;
	//		}
	//
	//		return indices;
	//	}



	//retourne vrai si le parcourt du chemin commencant � l'indice i dans l'orientation sp�cifi� est libre (n'en croise pas un autre)
	private boolean cheminLibre(int indice, CaseTableauChemins[][] chemins, String orientation)
	{
		boolean estLibre = true;
		if(orientation.equals("HAUTEUR"))
		{
			for(int j = chemins[0].length-1; j >=0; j--)
			{
				//				System.out.println("indice: "+indice+", j = "+j);
				//si le chemin est deja pris alors on le reprend pas
				if(chemins[indice][j].estCheminLibre==false)
				{
					estLibre = false;
					break;
				}
				//trouver successeur
				if(chemins[indice][j].direction == Direction.GAUCHE)
				{
					indice--;
				}
				else if(chemins[indice][j].direction == Direction.DROITE)
				{
					indice++;
				}
			}
		}
		else//TODO largeur
		{
			//			System.out.println("pas impl�ment� 1");
			for(int i = chemins.length-1; i >=0; i--)
			{
				//				System.out.println("indice: "+indice+", j = "+j);
				//si le chemin est deja pris alors on le reprend pas
				if(chemins[i][indice].estCheminLibre==false)
				{
					estLibre = false;
					break;
				}
				//trouver successeur
				if(chemins[i][indice].direction == Direction.GAUCHE)
				{
					indice--;
				}
				else if(chemins[i][indice].direction == Direction.DROITE)
				{
					indice++;
				}
			}
		}
		return estLibre;
	}

	//	private CaseTableauChemins[][] marquerToutLesChemins(int[] indices, CaseTableauChemins[][] chemins, String orientation)
	//	{
	//		for(int i = 0; i<indices.length; i++)
	//		{
	//			chemins = marquerChemin(indices[i], chemins, orientation);
	//		}
	//		return chemins;
	//	}

	//retourne un CaseTableauChemin[][] avec les chemins occup� marqu� (boolean estCheminLibre = false)
	private CaseTableauChemins[][] marquerChemin(int indice, CaseTableauChemins[][] chemins, String orientation)
	{
		if(orientation.equals("HAUTEUR"))
		{
			for(int j = chemins[0].length-1; j >=0; j--)
			{
				//				System.out.println("indice = "+indice+", j = "+j);
				//				System.out.flush();
				chemins[indice][j].estCheminLibre = false;
				//trouver successeur
				if(chemins[indice][j].direction == Direction.GAUCHE)
				{
					indice--;
				}
				else if(chemins[indice][j].direction == Direction.DROITE)
				{
					indice++;
				}
			}
		}
		else//TODO largeur
		{
			//			System.out.println("pas impl�ment� 2");
			for(int i = chemins.length-1; i >=0; i--)
			{
				//				System.out.println("indice = "+indice+", j = "+j);
				//				System.out.flush();
				chemins[i][indice].estCheminLibre = false;
				//trouver successeur
				if(chemins[i][indice].direction == Direction.GAUCHE)
				{
					indice--;
				}
				else if(chemins[i][indice].direction == Direction.DROITE)
				{
					indice++;
				}
			}
		}
		return chemins;
	}


	//agrandi une image en largeur ou en hauteur
	private BufferedImage agrandirImage(String orientation, BufferedImage image, CaseTableauChemins[][] chemins, int[] indicesChemins)
	{
		BufferedImage res = null;

		if(orientation.equals("LARGEUR"))
		{
			//on initialise les chemins � ajouter
			res = recopierChemins("LARGEUR", image, chemins, indicesChemins);
			//on complete le reste de l'image
			res = completerImageRedimEnLargeur(res, image, chemins, "AGRANDIR");
			//			System.out.println("Image agrandie!");
		}
		else//TODO hauteur
		{
			//			System.out.println("pas impl�ment� 3");
			//on initialise les chemins � ajouter
			res = recopierChemins("HAUTEUR", image, chemins, indicesChemins);
			//on complete le reste de l'image
			res = completerImageRedimEnHauteur(res, image, chemins, "AGRANDIR");
			//			System.out.println("Image agrandie!");
		}

		return res;
		//return tmp;
	}


	//TODO
	private BufferedImage retrecirImage(String orientation, BufferedImage image, CaseTableauChemins[][] chemins, int[] indicesChemins)
	{
		BufferedImage res = null;

		if(orientation.equals("LARGEUR"))
		{
			res = new BufferedImage(image.getWidth()-indicesChemins.length, image.getHeight(),BufferedImage.TYPE_INT_ARGB);
			//on marque les chemins � supprimer (estCheminLibre = false)
			//chemins = marquerToutLesChemins(indicesChemins, chemins, "LARGEUR");
			//on complete le reste de l'image
			//			res = recopierChemins("LARGEUR", image, chemins, indicesChemins);
			res = completerImageRedimEnLargeur(res, image, chemins, "RETRECIR");
		}
		else//TODO hauteur
		{
			res = new BufferedImage(image.getWidth(), image.getHeight()-indicesChemins.length,BufferedImage.TYPE_INT_ARGB);
			//chemins = marquerToutLesChemins(indicesChemins, chemins, "HAUTEUR");
			//on complete le reste de l'image
			//			res = recopierChemins("HAUTEUR", image, chemins, indicesChemins);
			res = completerImageRedimEnHauteur(res, image, chemins, "RETRECIR");
			//			System.out.println("Image retrecie!");
		}

		return res;
	}


	//recopie les chemins pour un agrandissement
	private BufferedImage recopierChemins(String orientation, BufferedImage image, CaseTableauChemins[][] chemins, int[] indicesChemins)
	{
		BufferedImage res = null;
		int tmp = 0, i=0, j=0, indice = 0;


		if(orientation.equals("LARGEUR"))
		{
			cheminsRecopie = new CaseTableauChemins[image.getWidth()+indicesChemins.length][image.getHeight()];


			for( i = 0; i<cheminsRecopie.length; i++)
			{
				for( j = 0; j<chemins[0].length; j++)
				{
					cheminsRecopie[i][j] = new CaseTableauChemins(image.getRGB(indice, j), Direction.INIT);
					cheminsRecopie[i][j].estCheminLibre = true;
				}
			}
		}
		else//orientation == hauteur
		{
			cheminsRecopie = new CaseTableauChemins[image.getWidth()][image.getHeight()+indicesChemins.length];


			for( j = 0; j<cheminsRecopie[0].length; j++)
			{
				for( i = 0; i<chemins.length; i++)
				{
					cheminsRecopie[i][j] = new CaseTableauChemins(image.getRGB(i, indice), Direction.INIT);
					cheminsRecopie[i][j].estCheminLibre = true;
				}
			}
		}

		tmp = 0;
		//on tri le tableau des indices des chemins pour les parcourir de gauche � droite
		for( i=0; i<indicesChemins.length-1; i++)	//pour tout les chemins � dupliquer
		{
			for( j=i+1; j<indicesChemins.length; j++)	//pour tout les chemins � dupliquer
			{
				if(indicesChemins[i]>indicesChemins[j])
				{
					//on �change les valeurs
					tmp = indicesChemins[i];
					indicesChemins[i] = indicesChemins[j];
					indicesChemins[j] = tmp;
				}
			}
		}

		if(orientation.equals("LARGEUR"))
		{
			res = new BufferedImage(image.getWidth()+indicesChemins.length, image.getHeight(),BufferedImage.TYPE_INT_ARGB);

			//System.out.println(res.getRGB(0, 0));
			for( int k=0; k<indicesChemins.length; k++)	//pour tout les chemins � dupliquer
			{
				//trouver indice depart
				indice = indicesChemins[k];
				//				System.out.println(indice);
				//				System.out.flush();
				//pour toute la largeur
				for(j = image.getHeight()-1; j>=0; j--)
				{
					tmp = 0;
					//si valeur pas deja rempli
					//					if(!chemins[indice+k][j].estCheminLibre)
					//					{
					//recopier valeur 
					res.setRGB(indice+k, j, image.getRGB(indice, j));
					cheminsRecopie[indice+k][j].estCheminLibre = false;
					//res.setRGB(indice+k, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(0));
					//					}
					//					//sinon 
					//					else
					//					{
					//						System.out.println("Je dois pas passer l�...");
					//						//tant que valeur � cot� deja rempli
					//						tmp=1;
					//						while(indice+tmp+k<res.getWidth() && !chemins[indice+tmp+k][j].estCheminLibre)
					//						{
					//							//d�caler
					//							tmp++;
					//						}
					//						//si on est pas sortie de l'image
					//						if(indice+k+tmp<res.getWidth())
					//						{
					//							//remplir valeur
					//							res.setRGB(indice+k+tmp, j, image.getRGB(indice, j));
					//						}
					//						//sinon
					//						else
					//						{
					//							//d�caler dans l'autre sens
					//							tmp = 1;
					//							while(indice-tmp>=0 && !chemins[indice-tmp+k][j].estCheminLibre)
					//							{
					//								//d�caler
					//								tmp++;
					//							}
					//							//inserer la valeur
					//							res.setRGB(indice+k-tmp, j, image.getRGB(indice, j));
					//						}
					//
					//					}
					//trouver successeur
					if(chemins[indice][j].direction == Direction.GAUCHE)
					{
						indice--;
					}
					else if(chemins[indice][j].direction == Direction.DROITE)
					{
						indice++;
					}
					//					System.out.println(chemins[indice][j].direction);
				}
			}
		}
		else//hauteur TODO
		{
			//			System.out.println("pas impl�ment� 5");
			res = new BufferedImage(image.getWidth(), image.getHeight()+indicesChemins.length,BufferedImage.TYPE_INT_ARGB);

			//System.out.println(res.getRGB(0, 0));
			for( int k=0; k<indicesChemins.length; k++)	//pour tout les chemins � dupliquer
			{
				//trouver indice depart
				indice = indicesChemins[k];
				//				System.out.println(indice);
				//				System.out.flush();
				//pour toute la largeur
				for(i = image.getWidth()-1; i>=0; i--)
				{
					res.setRGB(i, indice+k, image.getRGB(i, indice));
					cheminsRecopie[i][indice+k].estCheminLibre = false;
					//trouver successeur
					if(chemins[i][indice].direction == Direction.GAUCHE)
					{
						indice--;
					}
					else if(chemins[i][indice].direction == Direction.DROITE)
					{
						indice++;
					}
					//					System.out.println(chemins[indice][j].direction);
				}
			}
		}

		return res;
	}


	//utilse pour les test, retourne une image avec les chemins choisit marqu� en rouge
	private BufferedImage marquerChemins(String orientation, BufferedImage image, CaseTableauChemins[][] chemins, int[] indicesChemins)
	{
		BufferedImage res = null;
		int tmp = 0, i=0, j=0, indice = 0;

		if(orientation.equals("LARGEUR"))
		{
			res = new BufferedImage(image.getWidth()+indicesChemins.length, image.getHeight(),BufferedImage.TYPE_INT_ARGB);

			//System.out.println(res.getRGB(0, 0));
			for( int k=0; k<indicesChemins.length; k++)	//pour tout les chemins � dupliquer
			{
				//trouver indice depart
				indice = indicesChemins[k];
				//				System.out.println(indice);
				//				System.out.flush();
				//pour toute la largeur
				for(j = image.getHeight()-1; j>=0; j--)
				{
					tmp = 0;
					//si valeur pas deja rempli
					//					if(res.getRGB(indice+k, j)==-1)
					//					{
					//recopier valeur 
					res.setRGB(indice+k, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//res.setRGB(indice+k, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(0));
					//					}
					//					//sinon 
					//					else
					//					{
					//						System.out.println("Je dois pas passer l�...");
					//						//tant que valeur � cot� deja rempli
					//						tmp=1;
					//						while(indice+tmp+k<res.getWidth() && res.getRGB(indice+tmp+k, j)==-1)
					//						{
					//							//d�caler
					//							tmp++;
					//						}
					//						//si on est pas sortie de l'image
					//						if(indice+k+tmp<res.getWidth())
					//						{
					//							//remplir valeur
					////							res.setRGB(indice+k+tmp, j, image.getRGB(indice, j));
					//							res.setRGB(indice+k+tmp, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//						}
					//						//sinon
					//						else
					//						{
					//							//d�caler dans l'autre sens
					//							tmp = 1;
					//							while(indice-tmp>=0 && res.getRGB(indice+k-tmp, j)==-1)
					//							{
					//								//d�caler
					//								tmp++;
					//							}
					//							//inserer la valeur
					////							res.setRGB(indice+k-tmp, j, image.getRGB(indice, j));
					//							res.setRGB(indice+k-tmp, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//						}
					//
					//					}
					//trouver successeur
					if(chemins[indice][j].direction == Direction.GAUCHE)
					{
						indice--;
					}
					else if(chemins[indice][j].direction == Direction.DROITE)
					{
						indice++;
					}
					//					System.out.println(chemins[indice][j].direction);
				}
			}
		}
		else//hauteur TODO
		{
			//			System.out.println("pas impl�ment� 6");
			res = new BufferedImage(image.getWidth(), image.getHeight()+indicesChemins.length,BufferedImage.TYPE_INT_ARGB);

			//System.out.println(res.getRGB(0, 0));
			for( int k=0; k<indicesChemins.length; k++)	//pour tout les chemins � dupliquer
			{
				//trouver indice depart
				indice = indicesChemins[k];
				//				System.out.println(indice);
				//				System.out.flush();
				//pour toute la largeur
				for(i = image.getWidth()-1; i>=0; i--)
				{
					tmp = 0;
					//si valeur pas deja rempli
					//					if(res.getRGB(indice+k, j)==-1)
					//					{
					//recopier valeur 
					res.setRGB(i, indice+k, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//res.setRGB(indice+k, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(0));
					//					}
					//					//sinon 
					//					else
					//					{
					//						System.out.println("Je dois pas passer l�...");
					//						//tant que valeur � cot� deja rempli
					//						tmp=1;
					//						while(indice+tmp+k<res.getWidth() && res.getRGB(indice+tmp+k, j)==-1)
					//						{
					//							//d�caler
					//							tmp++;
					//						}
					//						//si on est pas sortie de l'image
					//						if(indice+k+tmp<res.getWidth())
					//						{
					//							//remplir valeur
					////							res.setRGB(indice+k+tmp, j, image.getRGB(indice, j));
					//							res.setRGB(indice+k+tmp, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//						}
					//						//sinon
					//						else
					//						{
					//							//d�caler dans l'autre sens
					//							tmp = 1;
					//							while(indice-tmp>=0 && res.getRGB(indice+k-tmp, j)==-1)
					//							{
					//								//d�caler
					//								tmp++;
					//							}
					//							//inserer la valeur
					////							res.setRGB(indice+k-tmp, j, image.getRGB(indice, j));
					//							res.setRGB(indice+k-tmp, j, outil.setR(255)+outil.setB(0)+outil.setG(0)+outil.setAlpha(100));
					//						}
					//
					//					}
					//trouver successeur
					if(chemins[i][indice].direction == Direction.GAUCHE)
					{
						indice--;
					}
					else if(chemins[i][indice].direction == Direction.DROITE)
					{
						indice++;
					}
					//					System.out.println(chemins[indice][j].direction);
				}
			}
		}

		return res;
	}



	//	private BufferedImage completerImageRedimEnHauteur(BufferedImage toComplete, BufferedImage image)
	//	{
	//		//		System.out.println("toComplete  "+toComplete.getWidth()+"   "+toComplete.getHeight());
	//		//		System.out.println("Image avant modif    "+image.getWidth()+"   "+image.getHeight());
	//
	//		int decalage = 0;
	//		for(int i = 0; i<image.getWidth(); i++)
	//		{
	//			decalage = 0;
	//			for(int j = 0; j<image.getHeight(); j++)
	//			{
	//				//si la valeur est d�j� rempli alors on se d�cale
	//				while(toComplete.getRGB(i,  j+decalage)!=-1)
	//				{
	//					decalage++;
	//					//					System.out.println(i+"    "+(j+decalage));
	//					//					System.out.flush();
	//				}
	//				//on rempli la valeur � partir de l'initiale sans le d�calage
	//				toComplete.setRGB(i,  j+decalage, image.getRGB(i,  j));
	//			}
	//		}
	//		System.out.println(toComplete.getHeight()+"  "+toComplete.getWidth());
	//		return toComplete;
	//	}




	//complete le reste de l'image (apres ajout ou suppression des chemins de poids minimum)
	private BufferedImage completerImageRedimEnHauteur(BufferedImage toComplete, BufferedImage image,
			CaseTableauChemins[][] chemins, String typeRedim)
	{
		//		System.out.println("toComplete  "+toComplete.getWidth()+"   "+toComplete.getHeight());
		//		System.out.println("Image avant modif    "+image.getWidth()+"   "+image.getHeight());


		int decalage = 0;
		if(typeRedim.equals("AGRANDIR"))
		{
			for(int i = 0; i<image.getWidth(); i++)
			{
				decalage = 0;
				for(int j = 0; j<image.getHeight(); j++)
				{

					//si la valeur est d�j� rempli alors on se d�cale
					while( !cheminsRecopie[i][j+decalage].estCheminLibre)
					{
						decalage++;
						//					System.out.println(i+"    "+(j+decalage));
						//					System.out.flush();
					}
					//on rempli la valeur � partir de l'initiale avec le d�calage
					toComplete.setRGB(i,  j+decalage, image.getRGB(i,  j));


				}
			}
		}
		else
		{
			for(int i = 0; i<toComplete.getWidth(); i++)
			{
				decalage = 0;
				for(int j = 0; j<toComplete.getHeight(); j++)
				{

					//si la valeur est d�j� rempli alors on se d�cale
					while(!chemins[i][j+decalage].estCheminLibre)
					{
						decalage++;
						//					System.out.println(i+"    "+(j+decalage));
						//					System.out.flush();
					}
					//on rempli la valeur � partir de l'initiale avec le d�calage
					toComplete.setRGB(i,  j, image.getRGB(i,  j+decalage));

				}
			}
		}
		//		System.out.println(toComplete.getHeight()+"  "+toComplete.getWidth());
		return toComplete;
	}


	//complete le reste de l'image (apres ajout ou suppression des chemins de poids minimum)
	private BufferedImage completerImageRedimEnLargeur(BufferedImage toComplete, BufferedImage image,
			CaseTableauChemins[][] chemins, String typeRedim)
	{
		//		System.out.println("toComplete  "+toComplete.getWidth()+"   "+toComplete.getHeight());
		//		System.out.println("Image avant modif    "+image.getWidth()+"   "+image.getHeight());


		int decalage = 0;
		if(typeRedim.equals("AGRANDIR"))
		{
			for(int j = 0; j<image.getHeight(); j++)
			{
				decalage = 0;
				for(int i = 0; i<image.getWidth(); i++)
				{
					//si la valeur est d�j� rempli alors on se d�cale

					while( !cheminsRecopie[i+decalage][j].estCheminLibre)
					{
						decalage++;
					}
					//on rempli la valeur � partir de l'initiale sans le d�calage
					toComplete.setRGB(i+decalage,  j, image.getRGB(i,  j));
				}
			}
		}
		else
		{
			//			System.out.println("toComplete.getWidth() = "+toComplete.getWidth()+"; toComplete.getHeight()"+toComplete.getHeight());
			//			System.out.println("chemins.length = "+chemins.length+"; chemins[0].length = "+chemins[0].length);
			for(int j = 0; j<toComplete.getHeight(); j++)
			{
				decalage = 0;
				for(int i = 0; i<toComplete.getWidth(); i++)
				{
					//si la valeur est d�j� rempli alors on se d�cale

					while(!chemins[i+decalage][j].estCheminLibre)
					{
						decalage++;
					}

					//on rempli la valeur � partir de l'initiale sans le d�calage
					toComplete.setRGB(i,  j, image.getRGB(i+decalage,  j));
				}
			}
		}
		//		System.out.println(toComplete.getHeight()+"  "+toComplete.getWidth());
		return toComplete;
	}


	private class CaseTableauChemins
	{
		int value = 0;
		Direction direction;
		boolean estCheminLibre = true;

		public CaseTableauChemins(int val, Direction d)
		{
			value = val;
			direction = d;
		}
	}
	public enum Direction {
		GAUCHE,
		MILIEU,
		DROITE,
		INIT,
		OCCUPE
	}

	//TODO
	private BufferedImage appliquerRedimensionnementIntelligentHauteurCroisePas(BufferedImage image, int newhauteur)
	{
		BufferedImage newimage = new BufferedImage(image.getWidth(), newhauteur,BufferedImage.TYPE_INT_ARGB);


		int pas = 1;
		int nbPix = Math.abs(image.getHeight() - newhauteur);

		CheminsASupprimer cheminsASupprimer = null;
		//		int[] indicesCheminsASupprimer = trouverCheminsCroisePasPoidsMinEnHauteur(image, Math.abs(image.getWidth() - newlargeur));

		if(image.getHeight() < newhauteur) //agrandir en hauteur
		{
			pas = image.getHeight()/50;
			while(nbPix>pas)
			{
				cheminsASupprimer = calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(image, pas);
				image = agrandirImage("HAUTEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
				nbPix = nbPix - pas;
			}
			//			System.out.println(image.toString());
			//			System.out.flush();
			cheminsASupprimer = calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(image, nbPix);
			newimage = agrandirImage("HAUTEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
		}
		else	//r�tr�cir en hauteur
		{
			while(nbPix>pas)
			{
				cheminsASupprimer = calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(image, pas);
				image = retrecirImage("HAUTEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
				nbPix = nbPix - pas;
			}
			//			System.out.println(image.toString());
			//			System.out.flush();
			cheminsASupprimer = calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(image, nbPix);
			newimage = retrecirImage("HAUTEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);

		}
		//		System.out.println(outil.getR(outil.setR(255)+outil.setR(255)));

		//		cheminsASupprimer = calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(image, pas);
		//		newimage = marquerChemins("HAUTEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);


		return newimage;
	}


	private BufferedImage appliquerRedimensionnementIntelligentLargeurCroisePas(BufferedImage image, int newlargeur)
	{
		BufferedImage newimage = new BufferedImage(newlargeur, image.getHeight(),BufferedImage.TYPE_INT_ARGB);


		int pas = 1;
		int nbPix = Math.abs(image.getWidth() - newlargeur);

		CheminsASupprimer cheminsASupprimer = null;

		//test interm�diaire//
		//		int[] indicesCheminsASupprimer = trouverCheminsCroisePasPoidsMinEnHauteur(image, Math.abs(image.getWidth() - newlargeur));



		if(image.getWidth() < newlargeur) //agrandir en largeur
		{
			pas = image.getWidth()/50;
			while(nbPix>pas)
			{
				cheminsASupprimer = calculerCheminsMoinsCouteuxEnHauteurQuiCroisePas(image, pas);
				image = agrandirImage("LARGEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
				nbPix = nbPix - pas;
			}
			cheminsASupprimer = calculerCheminsMoinsCouteuxEnHauteurQuiCroisePas(image, nbPix);
			newimage = agrandirImage("LARGEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
		}
		else	//r�tr�cir en largeur
		{
			while(nbPix>pas)
			{
				cheminsASupprimer = calculerCheminsMoinsCouteuxEnHauteurQuiCroisePas(image, pas);
				image = retrecirImage("LARGEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
				nbPix = nbPix - pas;
			}
			//			System.out.println(image.toString());
			//			System.out.flush();
			cheminsASupprimer = calculerCheminsMoinsCouteuxEnHauteurQuiCroisePas(image, nbPix);
			newimage = retrecirImage("LARGEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);
		}
		//		System.out.println(outil.getR(outil.setR(255)+outil.setR(255)));
		//		newimage = marquerChemins("LARGEUR", image, cheminsASupprimer.lesChemins, cheminsASupprimer.lesIndices);

		return newimage;
	}



	//retourne une structure contenant un tableau des indices des chemins dans l'ordre croissant des poids et le tableau des chemins associ�
	private CheminsASupprimer calculerCheminsMoinsCouteuxEnLargeurQuiCroisePas(BufferedImage image, int nbCheminATrouver)
	{
		CheminsASupprimer cheminsASupprimer = null;
		int[] indicesChemins = new int[nbCheminATrouver];
		
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		
		BufferedImage im_poids = this.calculerImageEnergie(image);
		chemins = this.calculerCheminsMoinsCouteuxEnLargeur(im_poids);
		
		//pour le nombre de chemin � trouver
		for(int cpt = 0; cpt < nbCheminATrouver; cpt++)
		{
			//			-calcul des poids des chemins qui n'en traverse pas d'autre
			chemins = calculerPoidsCheminsCroisePasEnLargeur(im_poids, chemins);
			//			-m�morer indice chemin poids min
			if(nbCheminATrouver==1)
			{
				indicesChemins[cpt] = trouverCheminCroisePasPoidsMinEnLargeur(chemins, indicesChemins, cpt);
			}
			else
			{
				indicesChemins[cpt] = trouverCheminCroisePasPoidsMinEnLargeurAleatoire(chemins, indicesChemins, cpt);
			}
			//			-marquerChemin(indice chemin poids min)
			//			System.out.println(indicesChemins.toString());
			//			System.out.flush();
			chemins = marquerChemin(indicesChemins[cpt], chemins, "LARGEUR");
			//			System.out.println(indicesChemins.toString());
			//			System.out.flush();
		}

		//		for(int i = 0; i<indicesChemins.length; i++)
		//		{
		//			System.out.println("tab["+i+"] = "+indicesChemins[i]);
		//		}
		//		System.out.flush();

		cheminsASupprimer = new CheminsASupprimer(chemins, indicesChemins);
		return cheminsASupprimer;
	}

	private CheminsASupprimer calculerCheminsMoinsCouteuxEnHauteurQuiCroisePas(BufferedImage image, int nbCheminATrouver)
	{
		CheminsASupprimer cheminsASupprimer = null;
		int[] indicesChemins = new int[nbCheminATrouver];
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		BufferedImage im_poids = this.calculerImageEnergie(image);

		chemins = this.calculerCheminsMoinsCouteuxEnHauteur(im_poids);
		//pour le nombre de chemin � trouver
		for(int cpt = 0; cpt < nbCheminATrouver; cpt++)
		{
			//			-calcul des poids des chemins qui n'en traverse pas d'autre
			chemins = calculerPoidsCheminsCroisePasEnHauteur(im_poids, chemins);
			//			-m�morer indice chemin poids min
			if(nbCheminATrouver==1)
			{
				indicesChemins[cpt] = trouverCheminCroisePasPoidsMinEnHauteur(chemins, indicesChemins, cpt);
			}
			else
			{
				indicesChemins[cpt] = trouverCheminCroisePasPoidsMinEnHauteurAleatoire(chemins, indicesChemins, cpt);
			}
			//			-marquerChemin(indice chemin poids min)
			//			System.out.println(indicesChemins.toString());
			//			System.out.flush();
			chemins = marquerChemin(indicesChemins[cpt], chemins, "HAUTEUR");
			//			System.out.println(indicesChemins.toString());
			//			System.out.flush();
		}

		//		for(int i = 0; i<indicesChemins.length; i++)
		//		{
		//			System.out.println("tab["+i+"] = "+indicesChemins[i]);
		//		}
		//		System.out.flush();

		cheminsASupprimer = new CheminsASupprimer(chemins, indicesChemins);
		return cheminsASupprimer;
	}


	private CheminsASupprimer calculerCheminsMoinsCouteuxEnHauteurAleatoire(BufferedImage image, int nbCheminATrouver)
	{
		CheminsASupprimer cheminsASupprimer = null;
		int[] indicesChemins = new int[nbCheminATrouver];
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		BufferedImage im_poids = null;

		chemins = this.calculerCheminsMoinsCouteuxEnHauteur(im_poids);
		//pour le nombre de chemin � trouver
		for(int cpt = 0; cpt < nbCheminATrouver; cpt++)
		{
			im_poids = this.calculerImageEnergie(image);
			chemins = calculerPoidsCheminsEnHauteur(im_poids);
			//			indicesChemins[cpt] = trouverCheminAleatoirePoidsMinEnHauteur(chemins, indicesChemins, cpt);

		}

		//		for(int i = 0; i<indicesChemins.length; i++)
		//		{
		//			System.out.println("tab["+i+"] = "+indicesChemins[i]);
		//		}
		//		System.out.flush();

		cheminsASupprimer = new CheminsASupprimer(chemins, indicesChemins);
		return cheminsASupprimer;
	}




	private int trouverCheminCroisePasPoidsMinEnLargeur(CaseTableauChemins[][] chemins, int[] indices, int nbElmt)
	{
		int indice = 0;
		int min = Integer.MAX_VALUE;


		//		for(int i = 0; i<nbElmt; i++)
		//		{
		//			System.out.println("test : "+indices[i]);
		//		}
		//		System.out.flush();

		for(int j = 0; j < chemins[0].length; j++)
		{
			if(chemins[chemins.length-1][j].value < min && pasDejaPresent(j, indices, nbElmt) && this.cheminLibre(j, chemins, "LARGEUR"))
			{
				min = chemins[chemins.length-1][j].value;
				indice = j;
				//				System.out.println("i : "+i);
				//				System.out.println("min : "+min);
			}
			//			System.out.println("val : "+chemins[i][chemins[0].length-1].value );
		}

		return indice;
	}



	private int trouverCheminCroisePasPoidsMinEnLargeurAleatoire(CaseTableauChemins[][] chemins, int[] indices, int nbElmt)
	{
		int indice = 0;
		int nbVal = chemins[0].length/2;

		ArrayList<Integer> listIndiceMin = new ArrayList<Integer>();
		ArrayList<Integer> listVals = new ArrayList<Integer>();
		ArrayList<Integer> listValsMin = new ArrayList<Integer>();

		//on r�cupere les valeurs des chemins
		for(int j = 0; j < chemins[0].length; j++)
		{
			listVals.add(chemins[chemins.length-1][j].value);
		}
		//on tri les valeurs des chemins
		Collections.sort(listVals);
		
		//on m�morise les valeurs les plus faible
		if(listVals.size()>nbVal) for(int j = 0; j < nbVal; j++) listValsMin.add(listVals.get(j));
		else for(int j = 0; j < listVals.size(); j++) listValsMin.add(listVals.get(j));
		
		//on cherche les indices des chemins ayant les plus faibles valeurs
		for(int j = 0; j < chemins[0].length; j++)
		{
			if(listValsMin.contains(chemins[chemins.length-1][j].value) && pasDejaPresent(j, indices, nbElmt) && this.cheminLibre(j, chemins, "LARGEUR"))
			{
				listIndiceMin.add(j);
			}
		}
		//on choisi un chemin al�atoirement parmis les meilleurs
		Random generator = new Random(chemins[0].length*chemins.length);
		if(listIndiceMin.size()<nbVal)		indice = listIndiceMin.get(Math.abs(generator.nextInt())%listIndiceMin.size());
		else if(listIndiceMin.size()>0) indice = listIndiceMin.get(Math.abs(generator.nextInt())%nbVal);
		
		return indice;
	}
	
	
	
	private int trouverCheminCroisePasPoidsMinEnHauteurAleatoire(CaseTableauChemins[][] chemins, int[] indices, int nbElmt)
	{
		int indice = 0;
		int nbVal = chemins.length/2;

		ArrayList<Integer> listIndiceMin = new ArrayList<Integer>();
		ArrayList<Integer> listVals = new ArrayList<Integer>();
		ArrayList<Integer> listValsMin = new ArrayList<Integer>();

		//on r�cupere les valeurs des chemins
		for(int i = 0; i < chemins.length; i++)
		{
			listVals.add(chemins[i][chemins[0].length-1].value);
		}
		//on tri les valeurs des chemins
		Collections.sort(listVals);
		
		//on m�morise les valeurs les plus faible
		if(listVals.size()>nbVal) for(int j = 0; j < nbVal; j++) listValsMin.add(listVals.get(j));
		else for(int j = 0; j < listVals.size(); j++) listValsMin.add(listVals.get(j));
		
		//on cherche les indices des chemins ayant les plus faibles valeurs
		for(int i = 0; i < chemins.length; i++)
		{
			if(listValsMin.contains(chemins[i][chemins[0].length-1].value) && pasDejaPresent(i, indices, nbElmt) && this.cheminLibre(i, chemins, "HAUTEUR"))
			{
				listIndiceMin.add(i);
			}
		}
		//on choisi un chemin al�atoirement parmis les meilleurs
		Random generator = new Random(chemins[0].length*chemins.length);
		if(listIndiceMin.size()<nbVal)		indice = listIndiceMin.get(Math.abs(generator.nextInt())%listIndiceMin.size());
		else if(listIndiceMin.size()>0) indice = listIndiceMin.get(Math.abs(generator.nextInt())%nbVal);
		
		return indice;
	}


	private int trouverCheminCroisePasPoidsMinEnHauteur(CaseTableauChemins[][] chemins, int[] indices, int nbElmt)
	{
		int indice = 0;
		int min = Integer.MAX_VALUE;


		//		for(int i = 0; i<nbElmt; i++)
		//		{
		//			System.out.println("test : "+indices[i]);
		//		}
		//		System.out.flush();

		for(int i = 0; i < chemins.length; i++)
		{
			if(chemins[i][chemins[0].length-1].value < min && pasDejaPresent(i, indices, nbElmt) && this.cheminLibre(i, chemins, "HAUTEUR"))
			{
				min = chemins[i][chemins[0].length-1].value;
				indice = i;
				//				System.out.println("i : "+i);
				//				System.out.println("min : "+min);
			}
			//			System.out.println("val : "+chemins[i][chemins[0].length-1].value );
		}

		return indice;
	}


	private int trouverCheminAleatoirePoidsMinEnHauteur(CaseTableauChemins[][] chemins)
	{
		int indice = 0;
		int min = Integer.MAX_VALUE;
		ArrayList<Integer> listIndicesValsMin = new ArrayList<Integer>();

		for(int i = 0; i < chemins.length; i++)
		{
			if(chemins[i][chemins[0].length-1].value < min)
			{
				min = chemins[i][chemins[0].length-1].value;
				indice = i;
			}
		}

		for(int i = 0; i < chemins.length; i++)
		{
			//si la valeur du chemin n'est pas superieur � 10% du minimum alors on considere que c'est aussi un min 
			if(chemins[i][chemins[0].length-1].value < min+(10*min/10));
			{
				listIndicesValsMin.add(i);
			}
		}

		return (int) listIndicesValsMin.get((int)Math.round(Math.random())%listIndicesValsMin.size());
	}


	private boolean pasDejaPresent(int val, int[] tab, int nbElmt)
	{
		for(int i = 0; i < nbElmt; i++)
		{
			if(tab[i]==val) return false;
		}
		return true;
	}





	//calcule le tableau des poids des chemins qui ne croise pas de pr�c�dents chemins choisit
	private CaseTableauChemins[][] calculerPoidsCheminsCroisePasEnLargeur(BufferedImage image, CaseTableauChemins[][] chemins_in)
	{
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		int i = 0, j = 0, min = Integer.MAX_VALUE, rgb = 0;
		int diffCouleur = 0, diffR = 0, diffG = 0, diffB = 0, ind_j = 0;
		Direction direction = Direction.MILIEU;

		//on initialise les premieres valeurs
		for(j=0; j<image.getHeight(); j++)
		{
			rgb = image.getRGB(0, j);
			chemins[0][j] = new CaseTableauChemins(outil.getR(rgb)+outil.getG(rgb)+outil.getB(rgb), Direction.INIT);
			chemins[0][j].estCheminLibre = chemins_in[0][j].estCheminLibre;
		}

		int valCurrent = 0;
		//on calcul les suivantes

		for(i = 1; i<image.getWidth(); i++)
		{
			for(j = 0; j < image.getHeight(); j++)
			{

				min = Integer.MAX_VALUE;

				
//				diffR = Math.abs(outil.getR(image.getRGB(i, j))-outil.getR(image.getRGB(i-1, j-1)));
//				diffG = Math.abs(outil.getG(image.getRGB(i, j))-outil.getG(image.getRGB(i-1, j-1)));
//				diffB = Math.abs(outil.getB(image.getRGB(i, j))-outil.getB(image.getRGB(i-1, j-1)));
//				diffCouleur = diffR+diffG+diffB;
				
				if(j!=0 && chemins[i-1][j-1].value < min && chemins_in[i-1][j-1].estCheminLibre==true
						&& chemins_in[i-1][j].estCheminLibre==true)
				{
					min = chemins[i-1][j-1].value;
					direction = Direction.GAUCHE;
					ind_j = j-1;
				}

				if(chemins[i-1][j].value < min && chemins_in[i-1][j].estCheminLibre==true)
				{
					min = chemins[i-1][j].value;
					direction = Direction.MILIEU;
					ind_j = j;
				}

				if(j<image.getHeight()-1 && j>0 && chemins[i-1][j-1].value< min
						&& chemins_in[i-1][j+1].estCheminLibre==true)
				{
					min = chemins[i-1][j+1].value;
					direction = Direction.DROITE;
					ind_j = j+1;
				}
				if(min==Integer.MAX_VALUE) 
				{
					//					System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAYYYYYE");
					min = chemins[i-1][j].value;
					direction = Direction.MILIEU;
					ind_j = j;
				}
				valCurrent = outil.getR(image.getRGB(i, j))+outil.getG(image.getRGB(i, j))+outil.getB(image.getRGB(i, j));
				diffR = Math.abs(outil.getR(image.getRGB(i, j))-outil.getR(image.getRGB(i-1, ind_j)));
				diffG = Math.abs(outil.getG(image.getRGB(i, j))-outil.getG(image.getRGB(i-1, ind_j)));
				diffB = Math.abs(outil.getB(image.getRGB(i, j))-outil.getB(image.getRGB(i-1, ind_j)));
				diffCouleur = diffR+diffG+diffB;
				chemins[i][j] = new CaseTableauChemins(min+valCurrent+diffCouleur, direction);
				chemins[i][j].estCheminLibre = chemins_in[i][j].estCheminLibre;
			}
		}

		return chemins;
	}


	//calcule le tableau des poids des chemins qui ne croise pas de pr�c�dents chemins choisit
	private CaseTableauChemins[][] calculerPoidsCheminsCroisePasEnHauteur(BufferedImage image, CaseTableauChemins[][] chemins_in)
	{
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		int i = 0, j = 0, min = Integer.MAX_VALUE, rgb = 0;
		int diffCouleur = 0, diffR = 0, diffG = 0, diffB = 0, ind_i = 0;
		Direction direction = Direction.MILIEU;

		//on initialise les premieres valeurs
		for(i=0; i<image.getWidth(); i++)
		{
			rgb = image.getRGB(i, 0);
			chemins[i][0] = new CaseTableauChemins(outil.getR(rgb)+outil.getG(rgb)+outil.getB(rgb), Direction.INIT);
			chemins[i][0].estCheminLibre = chemins_in[i][0].estCheminLibre;
		}

		int valCurrent = 0;
		//on calcul les suivantes
		for(j = 1; j < image.getHeight(); j++)
		{

			for(i = 0; i<image.getWidth(); i++)
			{
				min = Integer.MAX_VALUE;

				if(i!=0 && chemins[i-1][j-1].value < min && chemins_in[i-1][j-1].estCheminLibre==true
						&& chemins_in[i][j-1].estCheminLibre==true)
				{
					min = chemins[i-1][j-1].value;
					direction = Direction.GAUCHE;
					ind_i = i-1;
				}

				if(chemins[i][j-1].value < min && chemins_in[i][j-1].estCheminLibre==true)
				{
					min = chemins[i][j-1].value;
					direction = Direction.MILIEU;
					ind_i = i;
				}

				if(i<image.getWidth()-1 && i>0 && chemins[i-1][j-1].value< min
						&& chemins_in[i+1][j-1].estCheminLibre==true)
				{
					min = chemins[i+1][j-1].value;
					direction = Direction.DROITE;
					ind_i = i+1;
				}
				if(min==Integer.MAX_VALUE) 
				{
					//					System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAYYYYYE");
					min = chemins[i][j-1].value;
					direction = Direction.MILIEU;
					ind_i = i;
				}
				valCurrent = outil.getR(image.getRGB(i, j))+outil.getG(image.getRGB(i, j))+outil.getB(image.getRGB(i, j));
				diffR = Math.abs(outil.getR(image.getRGB(i, j))-outil.getR(image.getRGB(ind_i, j-1)));
				diffG = Math.abs(outil.getG(image.getRGB(i, j))-outil.getG(image.getRGB(ind_i, j-1)));
				diffB = Math.abs(outil.getB(image.getRGB(i, j))-outil.getB(image.getRGB(ind_i, j-1)));
				diffCouleur = diffR+diffG+diffB;
				chemins[i][j] = new CaseTableauChemins(min+valCurrent+diffCouleur, direction);
				chemins[i][j].estCheminLibre = chemins_in[i][j].estCheminLibre;
			}
		}

		return chemins;
	}



	private CaseTableauChemins[][] calculerPoidsCheminsEnHauteur(BufferedImage image)
	{
		CaseTableauChemins[][] chemins = new CaseTableauChemins[image.getWidth()][image.getHeight()];
		int i = 0, j = 0, min = Integer.MAX_VALUE, rgb = 0;
		Direction direction = Direction.MILIEU;

		//on initialise les premieres valeurs
		for(i=0; i<image.getWidth(); i++)
		{
			rgb = image.getRGB(i, 0);
			chemins[i][0] = new CaseTableauChemins(outil.getR(rgb)+outil.getG(rgb)+outil.getB(rgb), Direction.INIT);
			chemins[i][0].estCheminLibre = chemins[i][0].estCheminLibre;
		}

		int valCurrent = 0;
		//on calcul les suivantes
		for(j = 1; j < image.getHeight(); j++)
		{

			for(i = 0; i<image.getWidth(); i++)
			{
				min = Integer.MAX_VALUE;

				if(i!=0 && chemins[i-1][j-1].value < min/* && chemins_in[i-1][j-1].estCheminLibre==true
						&& chemins_in[i][j-1].estCheminLibre==true*/)
				{
					min = chemins[i-1][j-1].value;
					direction = Direction.GAUCHE;
				}

				if(chemins[i][j-1].value < min/* && chemins_in[i][j-1].estCheminLibre==true*/)
				{
					min = chemins[i][j-1].value;
					direction = Direction.MILIEU;
				}

				if(i<image.getWidth()-1 && i>0 /*&& chemins_in[i-1][j-1].value< min
						&& chemins_in[i+1][j-1].estCheminLibre==true*/)
				{
					min = chemins[i+1][j-1].value;
					direction = Direction.DROITE;
				}
				if(min==Integer.MAX_VALUE) 
				{
					//					System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAYYYYYE");
					min = chemins[i][j-1].value;
					direction = Direction.MILIEU;
				}
				valCurrent = outil.getR(image.getRGB(i, j))+outil.getG(image.getRGB(i, j))+outil.getB(image.getRGB(i, j));
				chemins[i][j] = new CaseTableauChemins(min+valCurrent, direction);
			}
		}

		return chemins;
	}

	private class CheminsASupprimer
	{
		public CaseTableauChemins[][] lesChemins = null;
		public int[] lesIndices = null;

		public CheminsASupprimer(CaseTableauChemins[][] chemins, int[] indices )
		{
			lesChemins = chemins;
			lesIndices = indices;
		}
	}
}

