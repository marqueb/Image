package Vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class CadreImage extends JComponent{
	private BufferedImage image;
	private Graphics2D buffer;
	private ImageIcon imageIcon;
	private JLabel labelImage;
	private String nomFichier;
	private JScrollPane imageScroller;
	private int maxScrollX, maxScrollY;
	private ArrayList<BufferedImage> annuler;
	private ArrayList<BufferedImage> refaire;

	//constructeur a implementer
	public CadreImage(BufferedImage i)
	{
		image = i;
		imageIcon = new ImageIcon(image);
		buffer = image.createGraphics();
		imageScroller=new JScrollPane();
		annuler = new ArrayList<BufferedImage>();
		refaire = new ArrayList<BufferedImage>();
	}

	public CadreImage()
	{
	}

	public void paintComponent(Graphics g) {
		int width = getSize().width;
		int height = getSize().height;
		//Graphics2D drawable = (Graphics2D) g;
		if (image==null)
		{
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			buffer = this.image.createGraphics();
			buffer.setPaint(Color.white);
			buffer.fillRect(0, 0, width, height);
			buffer.setPaint(Color.black);
		}
		((Graphics2D)g).drawImage(image,0,0,null);
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	public String getNomFichier(){
		return this.nomFichier;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Graphics2D getBuffer() {
		return buffer;
	}

	public void setBuffer(Graphics2D buffer) {
		this.buffer = buffer;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public JScrollPane getImageScroller() {
		return imageScroller;
	}

	public void setImageScroller(JScrollPane imageScroller) {
		this.imageScroller = imageScroller;
	}
	
	public int getMaxScrollX() {
		return maxScrollX;
	}

	public void setMaxScrollX(int maxScrollX) {
		this.maxScrollX = maxScrollX;
	}

	public int getMaxScrollY() {
		return maxScrollY;
	}

	public void setMaxScrollY(int maxScrollY) {
		this.maxScrollY = maxScrollY;
	}

	public ArrayList<BufferedImage> getAnnuler() {
		return annuler;
	}

	public void setAnnuler(ArrayList<BufferedImage> annuler) {
		this.annuler = annuler;
	}

	public ArrayList<BufferedImage> getRefaire() {
		return refaire;
	}

	public void setRefaire(ArrayList<BufferedImage> refaire) {
		this.refaire = refaire;
	}

	public JLabel getLabelImage() {
		return labelImage;
	}

	public void setLabelImage(JLabel labelImage) {
		this.labelImage = labelImage;
	}
}
