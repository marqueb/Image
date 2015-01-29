package Controleur;

import java.awt.Point;
import java.awt.color.CMMException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.crypto.spec.GCMParameterSpec;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Imgproc.*;

import Modele.Modele;
import Modele.Outil;
import Vue.CadreImage;
public class ControlerSegmentation implements ActionListener{

	Controler controler = null;

	//static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	//static{ System.loadLibrary("opencv_java2410"); }


	public ControlerSegmentation(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		controler.init();
		controler.setExisteresultat(false);
		controler.getModele().initAnnulerRefaire(controler.getModele().cadreImageCourant());
		controler.setSegmentation(true);
		controler.setFg(new Mat(controler.getModele().cadreImageCourant().getImage().getHeight(),controler.getModele().cadreImageCourant().getImage().getWidth(),CvType.CV_8UC1));
		controler.getModele().remplirInit(controler.getFg(), controler.getModele().cadreImageCourant().getImage().getHeight(),  controler.getModele().cadreImageCourant().getImage().getWidth());
		JButton valider= new JButton("valider");
		controler.getModele().getInterfaceGraphique().getSegmenter().setEnabled(false);
		JLabel info = new JLabel("S�lection clique gauche pour premier plan, s�lection clique droit pour arri�re plan");
		controler.getModele().getInterfaceGraphique().getPanelOption().add(valider);
		controler.getModele().getInterfaceGraphique().getPanelInfo().add(info);
		controler.getModele().getInterfaceGraphique().getPanelOption().repaint();
		controler.getModele().getInterfaceGraphique().getFrame().validate();
		controler.addControlerSegmentationValider(valider);
		controler.getModele().getInterfaceGraphique().getPanelOption().add(valider);
		controler.getModele().actualiserImageIcon();
	
	}
}
