package Controleur;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Modele.Modele;
import Vue.InterfaceGraphique;

public class ControlerFiltreUser implements ActionListener{

	private InterfaceGraphique it = null;
	private Modele modele = null;
	private JComboBox<String> comboBox = null;
	private JPanel panelUser = null;
	private float[][] filtre = null;
	private JLabel labelInfo = new JLabel("");


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
				JPanel info = it.getPanelInfo();
				info.removeAll();
				info.validate();
				//alors appliquer le filtre à l'image 
				modele.appliquerFiltre(filtre);
				//et rafraichir l'imageIcon
				modele.actualiserImageIcon();
				modele.getControler().setUtilisateurActive(true);
				modele.getControler().init();
			}
			it.rafraichirComponentOption();
		}
		else if(e.getActionCommand().equals("Annuler"))
		{
			modele.actualiserImageIcon();
			modele.getControler().setUtilisateurActive(true);
			modele.getControler().init();
			it.rafraichirComponentOption();
			
		}
		else if(e.getActionCommand().equals("Previsualiser"))
		{//ouvrir une nouvelle fenetre avec l'ancienne image et la nouvelle
			BufferedImage im_no_modif = null, im_modif = null;

			if(isFiltreValid())
			{
				im_no_modif = modele.getListCadreImage().get(it.getTabbedPane().getSelectedIndex()).getImage();
				im_modif = modele.calculerConvolution(filtre, im_no_modif);
				it.previsualiserApplicationFiltreUser(im_no_modif, im_modif);
			}
			it.rafraichirComponentOption();
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
		JTextArea textArea = null;
		int i = 0, j=0, cptErr = 0;
		boolean isValid = true;
		JPanel pInfo = null;


		for(i = 0; i<taille; i++)
		{
			for(j = 0; j<taille; j++)
			{
				//					System.out.println(Integer.getInteger(((JTextArea)panelUser.getComponent(i+taille*j)).getText()));
				textArea = (JTextArea)panelUser.getComponent(i+taille*j);
				textArea.setForeground(Color.BLACK);
				try{
					this.filtre[i][j] = Float.parseFloat(textArea.getText());
				}catch(Exception e){
					pInfo = it.getPanelInfo();
					this.panelUser.getComponent(i+taille*j).setForeground(Color.RED);
					isValid = false;
					cptErr++;
				}
			}
		}

		if(!isValid)
		{
			pInfo.removeAll();
			pInfo.validate();
			if(cptErr==1) labelInfo.setText("Filtre invalide. La valeur en rouge n'est pas un réel.");
			else labelInfo.setText("Filtre invalide. Les valeurs en rouge ne sont pas des réels.");
			labelInfo.setForeground(Color.RED);
			pInfo.add(labelInfo);
			pInfo.validate();
		}

		return isValid;
	}

}
