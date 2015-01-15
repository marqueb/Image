package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Modele.TypeFiltre;

public class ControlerChoixTailleFiltre implements ActionListener, ChangeListener{
	private Controler controler = null;
	private TypeFiltre typeFiltre = null;
	JComboBox<String> boxTypeFiltre = null;
	JComboBox<String> boxTailleFiltre = null;

	public ControlerChoixTailleFiltre(Controler controler, TypeFiltre filtre, JComboBox<String> boxType, JComboBox<String> boxTaille){
		this.controler=controler;
		this.typeFiltre = filtre;
		this.boxTypeFiltre = boxType;
		this.boxTailleFiltre = boxTaille;
		//on affiche avec le nouveau filtre
		controler.getModele().traiterChangementTailleFiltre(typeFiltre, getTaille(this.boxTailleFiltre));
	}
	
	public void stateChanged(ChangeEvent arg0)
	{
		controler.getModele().traiterChangementTailleFiltre(typeFiltre);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Valider"))
		{
			controler.valider();
		}
		else if(e.getActionCommand().equals("Annuler"))
		{
			controler.annuler();
		}
		else if(e.getSource().equals(this.boxTypeFiltre))
		{
			this.typeFiltre = getTypeFiltre(this.boxTypeFiltre);
			//on affiche avec le nouveau filtre
			controler.getModele().traiterChangementTailleFiltre(typeFiltre, getTaille(this.boxTailleFiltre));
		}
		else if(e.getSource().equals(this.boxTailleFiltre))
		{
			controler.getModele().traiterChangementTailleFiltre(typeFiltre, getTaille(this.boxTailleFiltre));
		}
	}
	
	
	private TypeFiltre getTypeFiltre(JComboBox<String> c)
	{
		if(c.getSelectedItem().equals("Flitre Gaussien"))
		{
			return TypeFiltre.GAUSSIEN;
		}
		else
		{
			return TypeFiltre.MOYENNEUR;
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
}