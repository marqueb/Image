package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import Vue.CadreImage;

public class ControlerSegmentationValider implements ActionListener {

	Controler controler;
	public ControlerSegmentationValider(Controler controler) {
		this.controler=controler;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controler.getModele().initAnnulerRefaire(controler.getModele().cadreImageCourant());
		if(controler.isExisteresultat()){
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
			Mat fgdModel = new Mat();
			fgdModel.setTo(new Scalar(255, 255, 255));
			Mat bgdModel = new Mat();
			bgdModel.setTo(new Scalar(255, 255, 255));

			Rect rect = new Rect(0, 0,rows,cols);
			//System.out.println(controler.getFg().dump());
			Imgproc.grabCut(img, controler.getFg(), rect, bgdModel, fgdModel, 2, Imgproc.GC_INIT_WITH_MASK);
			byte[] data2;
			data2 = new byte[rows * cols * (int)img.elemSize()];
			controler.getFg().get(0, 0, data2); 
			//Separation entre foreground et background si r�sultat= 0-2 => 0 si resultat= 1-3 =>1
			for(int i = 0; i < data2.length; i++)
			{
				if(data2[i]==1|| data2[i]==3){
					data2[i] = 1;
				}
				else
					data2[i] = 0;

			}
			controler.getFg().put(0, 0, data2);
			BufferedImage imagegrab = new BufferedImage(cols,rows, BufferedImage.TYPE_INT_ARGB);
			MatOfByte bytemat = new MatOfByte();
			MatOfByte mb=new MatOfByte();  

			img.get(0, 0, data);
			for(int i = 0; i < dataBuff.length-2; i=i+1)
			{

				if(data2[i]==0 || data2[i+1]==0 || data2[i+2]==0){
					data[i*3]=0;
					data[i*3+1]=0;
					data[i*3+2]=0;
				}

			}
			img.put(0, 0, data);
			Highgui.imencode(".jpg",img, mb); 
			try {
				imagegrab = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			controler.setSegmentation(false);
			controler.getModele().getInterfaceGraphique().getSegmenter().setEnabled(true);
			controler.getModele().getInterfaceGraphique().getPanelOption().removeAll();
			CadreImage cadre = controler.getModele().cadreImageCourant();
			controler.getModele().getInterfaceGraphique().getPanelInfo().removeAll();
			controler.getModele().getInterfaceGraphique().getPanelInfo().repaint();
			controler.getModele().getInterfaceGraphique().getPanelOption().repaint();
			controler.getModele().getInterfaceGraphique().getFrame().validate();
			cadre.setImage(imagegrab);
			controler.getModele().actualiserImageIcon();
			controler.getModele().remplirInit(controler.getFg(),controler.getModele().cadreImageCourant().getImage().getHeight(),controler.getModele().cadreImageCourant().getImage().getWidth());
		}
		else{
			JLabel info = new JLabel("Pas de sélection du premier plan, Annulation");
			controler.getModele().getInterfaceGraphique().getPanelInfo().add(info);
			controler.getModele().getInterfaceGraphique().getPanelInfo().repaint();
			controler.getModele().getInterfaceGraphique().getFrame().validate();
		}

	}

}
