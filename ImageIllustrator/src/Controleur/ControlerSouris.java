package Controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import Modele.*;
import Vue.*;

public class ControlerSouris extends MouseMotionAdapter implements MouseListener{
	private Controler controler;

	public ControlerSouris(Controler controler){
		this.controler=controler;
	}

	public void mouseMoved(MouseEvent e){
		controler.sourisBouge(e.getX(), e.getY());
	}
	
	public void mouseEntered(MouseEvent e){
		controler.sourisEntre(e.getX(), e.getY());
	}
	
	public void mouseExited(MouseEvent e){
		controler.sourisSort(e.getX(), e.getY());
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		controler.sourisClique(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		controler.sourisPresse(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		controler.sourisRelache(e.getX(), e.getY());
	}

	public void mouseDragged(MouseEvent e){
		controler.sourisDragged(e.getX(), e.getY());
	}
}
