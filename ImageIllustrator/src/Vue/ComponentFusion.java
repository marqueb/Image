package Vue;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

import Controleur.Controler;

public class ComponentFusion extends JComponent{
	CadreImage cadre_ima = null;
	JSlider slider = null;
	JButton appliquer = null;
	
	public ComponentFusion(CadreImage c, Controler ctrl)
	{
		cadre_ima = c;
		slider = new JSlider(0,100,0);
		appliquer = new JButton("Appliquer");
		
		slider.addChangeListener(ctrl);
		appliquer.addActionListener(ctrl);
	}
	
	public void paintComponent (Graphics g)
	{
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(appliquer, BorderLayout.NORTH);
		panel.add(cadre_ima, BorderLayout.CENTER);
		panel.add(slider, BorderLayout.SOUTH);
		
		System.out.println("panel:"+panel.getSize().toString());
	}
}
