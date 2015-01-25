package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;




public class Histogramme extends JComponent{	
	private BufferedImage im = null;
	private String titre = null;
	private int[][] tab = null;
	private int[][] yuv = null;	
	private Dimension d;
	private Graphics2D buffer;
	private boolean afficheRouge,afficheVert,afficheBleu, afficheLuminence,afficheChrominanceU,afficheChrominanceV;
	public Histogramme(int[][] tabHisto,int[][]yuv, String t, Dimension d)
	{
		tab = tabHisto;
		this.yuv=yuv;
		titre = t;
		this.d=d;
		afficheRouge=true;
		afficheVert=true;
		afficheBleu=true;
		afficheLuminence=false;
		afficheChrominanceU=false;
		afficheChrominanceV=false;
	}


	public void paintComponent (Graphics g)
	{	
		im = new BufferedImage( getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		buffer = im.createGraphics();
		buffer.setColor(Color.WHITE);		
		buffer.fillRect(0, 0, getWidth(),getHeight());
		buffer.setColor(Color.BLACK);

		int max=tab[0][0];
		for (int j=0;j<tab[0].length;j++){
			if(afficheRouge){
				if (max<tab[0][j])
					max=tab[0][j];
			}
			if(afficheBleu){
				if (max<tab[2][j])
					max=tab[2][j];
			}
			if(afficheVert){
				if (max<tab[1][j])
					max=tab[1][j];
			}
			if(afficheLuminence){		
				if (max<yuv[0][j])
					max=yuv[0][j];
			}
			if(afficheChrominanceU){		
				if (max<yuv[1][j])
					max=yuv[1][j];
			}
			if(afficheChrominanceV){		
				if (max<yuv[2][j])
					max=yuv[2][j];
			}
			//	}
		}

		int largeur=getWidth()/255;
		int current=50;
		int marge=50;
		for (int i=0;i<tab[0].length-1;i++){ 
			if(afficheLuminence){		
				buffer.setColor(Color.orange);
				buffer.drawLine(current, (getHeight()- (int)(yuv[0][i]*(getHeight()-marge-5)/max)-marge), current+4, (getHeight()-(yuv[0][i+1]*(getHeight()-marge-5)/max)-marge));
			}
			if(afficheChrominanceU){
				buffer.setColor(Color.PINK);
				buffer.drawLine(current,  (getHeight()-(int)(yuv[1][i]*(getHeight()-marge-5)/max)-marge), current+4, (getHeight()-(yuv[1][i+1]*(getHeight()-marge-5)/max)-marge));
			}
			if(afficheChrominanceV){			
				buffer.setColor(Color.MAGENTA);
				buffer.drawLine(current,  (getHeight()-(int)(yuv[2][i]*(getHeight()-marge-5)/max)-marge), current+4, (getHeight()-(yuv[2][i+1]*(getHeight()-marge-5)/max)-marge));
			}
			if(afficheRouge){
				buffer.setColor(Color.RED);
				buffer.drawLine(current, getHeight()-(int)(tab[0][i]*(getHeight()-marge-5)/max)-marge, current+4,getHeight()-(int)(tab[0][i+1]*(getHeight()-marge-5)/max)-marge);
			}
			if(afficheVert){
				buffer.setColor(Color.GREEN);
				buffer.drawLine(current, getHeight()-(int) (tab[1][i]*(getHeight()-marge-5)/max)-marge, current+4,getHeight()-(int) (tab[1][i+1]*(getHeight()-marge-5)/max)-marge);
			}
			if(afficheBleu){
				buffer.setColor(Color.BLUE);
				buffer.drawLine(current, getHeight()-(int) (tab[2][i]*(getHeight()-marge-5)/max)-marge, current+4,getHeight()-(int) (tab[2][i+1]*(getHeight()-marge-5)/max)-marge);
			}


			if(i%10==0){
				//Affichage abssise
				buffer.setColor(Color.BLACK);
				buffer.drawString(""+i, current-marge/3, getHeight()-marge/2);
			}
			current=current+4;

		}
		buffer.setColor(Color.BLACK);
		buffer.drawLine(marge,getHeight()-marge, marge,0);
		buffer.drawLine(marge,getHeight()-marge,getWidth()-marge,getHeight()-marge);
		current=0;
		//Afficahge ordonnée

		int hauteur=getHeight()/10;
		//buffer.drawString(""+max,marge/5, marge/4);
		for(int i=0;i<=10;i++){		
			//(max-(max/i)
			buffer.drawString(""+(i*(max/10)),marge/5, getHeight()-hauteur*i-marge/3-marge/2);
		}
		//Affichage titre
		buffer.drawString("Effectif pour chaque niveau de couleur (0..255)",getWidth()/3, getHeight()-marge/4);
		((Graphics2D)g).drawImage(im,0,0,null);
	}

	public boolean isAfficheLuminence() {
		return afficheLuminence;
	}

	public void setAfficheLuminence(boolean afficheLuminence) {
		this.afficheLuminence = afficheLuminence;
	}

	public boolean isAfficheChrominanceU() {
		return afficheChrominanceU;
	}

	public void setAfficheChrominanceU(boolean afficheChrominanceU) {
		this.afficheChrominanceU = afficheChrominanceU;
	}

	public boolean isAfficheChrominanceV() {
		return afficheChrominanceV;
	}

	public void setAfficheChrominanceV(boolean afficheChrominanceV) {
		this.afficheChrominanceV = afficheChrominanceV;
	}

	public boolean isAfficheVert() {
		return afficheVert;
	}

	public boolean isAfficheRouge() {
		return afficheRouge;
	}

	public void setAfficheRouge(boolean afficheRouge) {
		this.afficheRouge = afficheRouge;
	}

	public void setAfficheVert(boolean afficheVert) {
		this.afficheVert = afficheVert;
	}

	public boolean isAfficheBleu() {
		return afficheBleu;
	}

	public void setAfficheBleu(boolean afficheBleu) {
		this.afficheBleu = afficheBleu;
	}

}
