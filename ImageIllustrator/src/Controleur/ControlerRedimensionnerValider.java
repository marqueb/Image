package Controleur;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Modele.Modele;

public class ControlerRedimensionnerValider implements ActionListener {

	Modele modele;
	JComboBox<String> boxTypeRedim;

	public ControlerRedimensionnerValider(Modele modele, JComboBox<String> box){
		this.modele=modele;
		boxTypeRedim = box;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		double x=0,y=0;
		try{
			x = Float.parseFloat(modele.getInterfaceGraphique().getHauteur().getText());
			modele.getInterfaceGraphique().getHauteur().setForeground(Color.BLACK);
			try{
				y = Float.parseFloat(modele.getInterfaceGraphique().getLargeur().getText());
				modele.getInterfaceGraphique().getLargeur().setForeground(Color.BLACK);
				if(boxTypeRedim.getSelectedItem().equals("Normal"))
				{
					modele.redimensionner(Integer.parseInt(modele.getInterfaceGraphique().getHauteur().getText()),Integer.parseInt(modele.getInterfaceGraphique().getLargeur().getText()));
					modele.getInterfaceGraphique().getFrameRedim().dispose();
				}
				else//redimensionnement int�lligent
				{
					modele.redimensionnerIntelligement(Integer.parseInt(modele.getInterfaceGraphique().getHauteur().getText()),Integer.parseInt(modele.getInterfaceGraphique().getLargeur().getText()));
					modele.getInterfaceGraphique().getFrameRedim().dispose();
				}
			}catch(Exception e){
				modele.getInterfaceGraphique().getLargeur().setForeground(Color.RED);
			}
		}catch(Exception e){
			modele.getInterfaceGraphique().getHauteur().setForeground(Color.RED);
		}	
	}

}