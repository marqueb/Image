package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import Vue.CadreImage;
//import com.googlecode.javacpp.Loader;
//import com.googlecode.javacv.CanvasFrame;
//import static com.googlecode.javacpp.Loader.*;



public class ControlerSegmentation implements ActionListener{

	Controler controler = null;
	static{ System.loadLibrary("opencv_java2410"); }


	public ControlerSegmentation(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
		CadreImage cadre = controler.getModele().getListCadreImage().get(controler.getModele().getInterfaceGraphique().getTabbedPane().getSelectedIndex());
		BufferedImage image = cadre.getImage();

		System.out.println("Welcome to OpenCV " + Core.VERSION);
		Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
		System.out.println("OpenCV Mat: " + m);
		Mat mr1 = m.row(1);
		mr1.setTo(new Scalar(1));
		Mat mc5 = m.col(5);
		mc5.setTo(new Scalar(5));
		System.out.println("OpenCV Mat data:\n" + m.dump());



	}
}
