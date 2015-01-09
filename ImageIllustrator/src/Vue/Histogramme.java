package Vue;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Histogramme extends JComponent{	
	
	String titre = null;
	int[] tab = null;
	
	public Histogramme(int[] tabHisto, String t)
	{
		tab = tabHisto;
		titre = t;
	}

	public void paintComponent (Graphics g)
	{	
		for (int x = 0; x < 256; x++) {
			g.drawLine(x, getSize().height, x, tab[x]);
		}
		
	}
}