package Controleur;

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
		if(boxTypeRedim.getSelectedItem().equals("Normal"))
		{
			modele.redimensionner(Integer.parseInt(modele.getInterfaceGraphique().getLargeur().getText()),Integer.parseInt(modele.getInterfaceGraphique().getHauteur().getText()));
			modele.getInterfaceGraphique().getFrameRedim().dispose();
		}
		else//redimensionnement intélligent
		{
			modele.redimensionnerIntelligement(Integer.parseInt(modele.getInterfaceGraphique().getLargeur().getText()),Integer.parseInt(modele.getInterfaceGraphique().getHauteur().getText()));
			modele.getInterfaceGraphique().getFrameRedim().dispose();
		}
	}

}