package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Modele.Modele;
import Vue.InterfaceGraphique;

public class ControlerFiltreUser implements ActionListener{

	InterfaceGraphique it = null;
	Modele modele = null;
	JComboBox<String> comboBox = null;
	JPanel panelUser = null;
	float[][] filtre = null;

	public ControlerFiltreUser(InterfaceGraphique i, Modele m, JComboBox<String> c, JPanel p)
	{
		it = i;
		modele = m;
		comboBox = c;
		panelUser = p;
	}

	public void actionPerformed(ActionEvent e) {
		//valider
		if(e.getActionCommand().equals("Valider"))
		{
			//si le filtre est valide
			if(isFiltreValid())
			{
				//alors appliquer le filtre à l'image 
				modele.appliquerFiltre(filtre);
				//et rafraichir l'imageIcon
				modele.actualiserImageIcon();
				it.rafraichirComponentOption();
			}
			else//TODO prévenir l'utilisateur que le filtre n'est pas valide
			{

			}
		}
		else//changer la grille du filtre
		{
			int taille = getTaille(comboBox);
			panelUser = it.ajouterGrilleFiltreUser(taille);
		}
	}

	private int getTaille(JComboBox<String> c)
	{
		if(c.getSelectedItem().equals("3x3"))
		{
			return 3;
		}
		if(c.getSelectedItem().equals("5x5"))
		{
			return 5;
		}
		if(c.getSelectedItem().equals("7x7"))
		{
			return 7;
		}
		return 9;
	}

	private boolean isFiltreValid()
	{
		int taille = (int) Math.sqrt(panelUser.getComponents().length);
		this.filtre = new float[taille][taille];
		int i = 0, j=0;
		try{
			for(i = 1; i<taille; i++)
			{
				for(j = 0; j<taille; j++)
				{
//					System.out.println(Integer.getInteger(((JTextArea)panelUser.getComponent(i+taille*j)).getText()));
					this.filtre[i][j] = new Float(Integer.parseInt(((JTextArea)panelUser.getComponent(i+taille*j)).getText()));
				}
			}
		}catch(Exception e){
			System.out.println("Filtre invalide, erreur en (i, j) = ("+i+", "+j+").");
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
