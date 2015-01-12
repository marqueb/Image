package Modele;

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
		return filtre;
	}
	
	
}
