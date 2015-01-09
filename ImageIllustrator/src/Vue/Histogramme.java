package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Histogramme extends JComponent{	
	private BufferedImage image = null;
	private String titre = null;
	private int[] tab = null;
	
	public Histogramme(int[] tabHisto, String t)
	{
		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		tab = tabHisto;
		titre = t;
	}
	
	public Histogramme(int[] tabHisto, String t, Dimension d)
	{
		image = new BufferedImage((int)d.getWidth(), (int)d.getHeight(), BufferedImage.TYPE_INT_ARGB);
		tab = tabHisto;
		titre = t;
	}

	public void paintComponent (Graphics g)
	{	
		System.out.println(this.getSize().toString());
		Graphics2D hist = (Graphics2D)g;
		Graphics2D zoneImage = image.createGraphics();
		
		zoneImage.setPaint(Color.black);
		zoneImage.drawRect(0, 0, this.getWidth(), this.getHeight());
		zoneImage.setPaint(Color.red);
		
		for (int x = 0; x < tab.length; x++) {
			zoneImage.drawLine(x, getSize().height, x, tab[x]*100);
		}
		
        //on affiche l'image modifié
		hist.drawImage(image, 0, 0,  null);
	}
}