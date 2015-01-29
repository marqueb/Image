package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
public class ControlerSegmentation implements ActionListener{

	Controler controler = null;
<<<<<<< HEAD


//	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	static{ System.loadLibrary("opencv_java2410"); }
=======

	//static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	//static{ System.loadLibrary("opencv_java2410"); }
>>>>>>> branch 'master' of https://github.com/marqueb/Image.git


	public ControlerSegmentation(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
//		controler.getModele().initAnnulerRefaire(controler.getModele().cadreImageCourant());
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//		controler.setSegmentation(true);
//		controler.setFg(new Mat(controler.getModele().cadreImageCourant().getImage().getHeight(),controler.getModele().cadreImageCourant().getImage().getWidth(),CvType.CV_8UC1));
//		controler.getModele().remplirInit(controler.getFg(), controler.getModele().cadreImageCourant().getImage().getHeight(),  controler.getModele().cadreImageCourant().getImage().getWidth());
//		JButton valider= new JButton("valider");
//		controler.addControlerSegmentationValider(valider);
//		controler.getModele().getInterfaceGraphique().getPanelOption().getComponent(1).setEnabled(false);
//		controler.getModele().getInterfaceGraphique().getPanelOption().add(valider);
//		controler.getModele().actualiserImageIcon();
		/*int[] selection = controler.getModele().selection();
		int rows = controler.getModele().cadreImageCourant().getImage().getHeight();
		int cols = controler.getModele().cadreImageCourant().getImage().getWidth();

		byte[] data;
		int r, g, b;

		int type = CvType.CV_8UC3;
		Mat img= new Mat(rows,cols,type);

		data = new byte[rows * cols * (int)img.elemSize()];
		int[] dataBuff = controler.getModele().cadreImageCourant().getImage().getRGB(0, 0, cols, rows, null, 0, cols);
		for(int i = 0; i < dataBuff.length; i++)
		{
			data[i*3+2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
			data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
			data[i*3 ] = (byte) ((dataBuff[i] >> 0) & 0xFF);
		}
		img.put(0, 0, data);
=======
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
>>>>>>> branch 'master' of https://github.com/marqueb/Image.git
	
	}
}
