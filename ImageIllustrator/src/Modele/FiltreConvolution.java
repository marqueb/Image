package Modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class FiltreConvolution {
	
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
		if(taille>1) filtre[(taille-1)/2][(taille-1)/2]++;
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
	
	
	
	
}
