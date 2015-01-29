package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
public class ControlerSegmentation implements ActionListener{

	Controler controler = null;


//	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	static{ System.loadLibrary("opencv_java2410"); }


	public ControlerSegmentation(Controler c)
	{
		controler = c;
	}

	public void actionPerformed(ActionEvent e) {
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
	

		Rect rect = new Rect(selection[0], selection[1],selection[2],selection[3]);
		Mat resultat = new Mat(rows,cols,type);
		resultat.setTo(new Scalar(125));
		Mat fgdModel = new Mat();
		fgdModel.setTo(new Scalar(255, 255, 255));
		Mat bgdModel = new Mat();
		bgdModel.setTo(new Scalar(255, 255, 255));


		Imgproc.grabCut(img, resultat, rect, bgdModel, fgdModel, 2, Imgproc.GC_INIT_WITH_RECT);
		byte[] data2;
		data2 = new byte[rows * cols * (int)img.elemSize()];
		resultat.get(0, 0, data2); 
		//Separation entre foreground et background si résultat= 0-2 => 0 si resultat= 1-3 =>1
		for(int i = 0; i < data2.length; i++)
		{
			if(data2[i]==1|| data2[i]==3){
				data2[i] = 1;
			}
			else
				data2[i] = 0;

		}
		resultat.put(0, 0, data2);
		BufferedImage imagegrab = new BufferedImage(cols,rows, BufferedImage.TYPE_INT_ARGB);
		MatOfByte bytemat = new MatOfByte();
		MatOfByte mb=new MatOfByte();  
        
		Mat imageretour = new Mat(rows,cols,type);
		byte[] dataretour = new byte[rows * cols*3];
		
		img.get(0, 0, data);
		int largeur=0, hauteur=0;
		for(int i = 0; i < dataBuff.length-2; i=i+1)
		{
		    if((largeur>selection[0] && largeur<selection[2]) && (hauteur>selection[1] && hauteur<selection[3])){ 
				if(data2[i]==0 || data2[i+1]==0 || data2[i+2]==0){
					data[i*3]=0;
					data[i*3+1]=0;
					data[i*3+2]=0;
				}
			}
			largeur++;
			if(largeur==cols){
				largeur=0;
				hauteur++;
			}	
		
		}
		img.put(0, 0, data);
		imageretour.put(0,0,dataretour);	
		
		Highgui.imencode(".jpg",img, mb); 
		try {
			imagegrab = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		CadreImage cadre = controler.getModele().cadreImageCourant();
		cadre.setImage(imagegrab);
		
		controler.getModele().actualiserImageIcon();*/
	}
}
