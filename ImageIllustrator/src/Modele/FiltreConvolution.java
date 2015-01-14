package Modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class FiltreConvolution {

	
	private static float[][] noyauLaplacien3x3 = {{1,2,1},{2,-12,2},{1,2,1}};
	private static float[][] noyauContraste3x3 = {{0,-1,0},{-1,5,-1},{0,-1,0}};
	
	public static float[][] getNoyauLaplacien3x3()
	{
		return noyauLaplacien3x3;
	}
	
	public static float[][] getNoyauContraste3x3()
	{
		return noyauContraste3x3;
	}
	
	
	public static float[][] createFiltreMoyenne(int taille)
	{
		float[][] filtre = new float[taille][taille];

		for(int i = 0; i<taille; i++)
		{
			for(int j=0; j<taille; j++)
			{
				filtre[i][j]=(float)1.0;
			}
		}
		//on met 2 sur la case central => amélioration du filtre moyenne
		if(taille>1) filtre[(taille-1)/2][(taille-1)/2]++;
		return filtre;
	}

	public static float[][] createFiltreGaussien(int taille, int sigma)
	{
		float[][] filtre = new float[taille][taille];
		float xi, yj;
		float somme = 0;

		for(int i = 0; i<taille; i++)
		{
			for(int j=0; j<taille; j++)
			{
				xi = i - ((taille-1)/2);
				yj = j - ((taille-1)/2);
				filtre[i][j] = (float) Math.exp(-(xi*xi + yj*yj) / (2*sigma*sigma));
				somme += filtre[i][j];
				//			    System.out.println(filtre[i][j]);
			}
		}
		
		//filtre[i][j] = filtre[i][j]*(1/somme) pour que la somme fasse 1
		for(int i = 0; i<taille; i++)
		{
			for(int j=0; j<taille; j++)
			{
				filtre[i][j] = filtre[i][j]/somme;
				//			    System.out.println(filtre[i][j]);
			}
		}

		//return normaliser(filtre);
		return filtre;
	}

	public static int GetValueFiltreMedian(int x, int y, int distance, BufferedImage im)
	{
		ArrayList<Integer> listVal = new ArrayList<Integer>();

		for(int i = -distance; i<=distance; i++)
		{
			for(int j = -distance; j<=distance; j++)
			{
				if(x+i>0 && x+i<im.getWidth() && y+i>0 && y+i<im.getHeight())
				{
					listVal.add(im.getRGB(x+i, y+j));
				}
			}
		}

		Collections.sort(listVal);

		if(listVal.size()>1) return listVal.get((listVal.size()-1)/2);
		return listVal.get(0);
	}

	private static float[][] normaliser(float[][] filtre)
	{
		float[][] res = new float[filtre.length][filtre[0].length];
		float coef = 1/filtre[0][0];
		

		for(int i = 0; i < filtre.length; i++)
		{
			for(int j = 0; j < filtre.length; j++)
			{
				res[i][j] = filtre[i][j]*coef;
//				System.out.println(res[i][j]);
			}
		}
		
		return res;
	}


}
