package Controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

public class ControlerSouris extends MouseMotionAdapter implements MouseListener{
	private Controler controler;

	public ControlerSouris(Controler controler){
		this.controler=controler;
	}

	public void mouseMoved(MouseEvent e){
		controler.sourisBouge(e.getX(), e.getY(), ((JLabel)e.getSource()).bounds().width, ((JLabel)e.getSource()).bounds().height);
	}
	
	public void mouseEntered(MouseEvent e){
		controler.sourisEntre(e.getX(), e.getY(), ((JLabel)e.getSource()).bounds().width, ((JLabel)e.getSource()).bounds().height);
	}
	
	public void mouseExited(MouseEvent e){
		controler.sourisSort(e.getX(), e.getY(), ((JLabel)e.getSource()).bounds().width, ((JLabel)e.getSource()).bounds().height);
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		controler.sourisClique(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		controler.sourisPresse(e.getX(), e.getY(), ((JLabel)e.getSource()).bounds().width, ((JLabel)e.getSource()).bounds().height);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		controler.sourisRelache(e.getX(), e.getY(), ((JLabel)e.getSource()).bounds().width, ((JLabel)e.getSource()).bounds().height);
	}

	public void mouseDragged(MouseEvent e){
		controler.sourisDragged(e.getX(), e.getY());
	}
}
