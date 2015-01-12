package Modele;

public class FiltreConvolution {
	
	public static int[][] createFiltreMoyenne(int taille)
	{
		int[][] filtre = new int[taille][taille];
		
		for(int i = 0; i<taille; i++)
		{
			for(int j=0; j<taille; j++)
			{
				filtre[i][j]=1;
			}
		}
		return filtre;
	}
	
	
}
