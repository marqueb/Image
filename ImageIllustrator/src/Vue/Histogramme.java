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
	private int[] tab = null;
	

	public Histogramme(int[] tabHisto, String t, Dimension d)
	{
		tab = tabHisto;
		titre = t;
		im = new BufferedImage((int)d.getWidth(),(int)d.getHeight(),BufferedImage.TYPE_INT_RGB);
	}

	public void paintComponent (Graphics g)
	{	
		Graphics2D buffer;
		int max=tab[0];
		buffer = im.createGraphics();
		buffer.setColor(Color.BLACK);		
		buffer.fillRect(0, 0, getSize().width,getSize().height);
		buffer.setColor(Color.WHITE);
		for (int i=1;i<tab.length;i++){
			if (max<tab[i])
				max=tab[i];
		}
		for (int i=0;i<tab.length;i++){
			buffer.drawLine(i,0, i, (int) (tab[i]*getSize().getHeight()/max));
		}
		
		((Graphics2D)g).drawImage(im,0,0,null);
	}

}
