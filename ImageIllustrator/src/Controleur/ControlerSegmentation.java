package Controleur;
//
//import static com.googlecode.javacv.cpp.opencv_core.cvRect;
//import static com.googlecode.javacv.cpp.opencv_imgproc.grabCut;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.grabCut;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import Modele.Outil;
import Vue.CadreImage;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ControlerSegmentation implements ActionListener{

	Controler controler = null;

	static{ System.loadLibrary("opencv_java2410"); }
//	static{ System.loadLibrary("jniopencv_core"); }
//
//	static{ System.loadLibrary("opencv"); }
//	static{ System.loadLibrary("javacv"); }
//	static{ System.loadLibrary("javacpp"); }

	public ControlerSegmentation(Controler c)
	{
		controler = c;
		
		
//		final String iDLLName = "javacpp.dll";
//		  final InputStream iStream = ControlerSegmentation.class.getResourceAsStream("/dll/" + iDLLName);
//		  final String iTmpDir = System.getProperty("java.io.tmpdir");
//		  final File iParentFolder = new File(iTmpDir, "dll");
//		  iParentFolder.mkdir();
//		  final File iDllFile = new File(iParentFolder, iDLLName);
//		  if (!iDllFile.exists()) {
//		    try {
//		      iDllFile.createNewFile();
//		    } catch (final IOException iException) {
//		      iException.printStackTrace();
//		    }
//		  }
//		  if (iStream != null) {
//		    FileOutputStream iOutStream = null;
//		    try {
//		      iOutStream = new FileOutputStream(iDllFile);
//		      final byte[] iBuffer = new byte[1024];
//		      while (iStream.read(iBuffer) > 0) {
//		        iOutStream.write(iBuffer);
//		      }
//		    } catch (final FileNotFoundException iException) {
//		      iException.printStackTrace();
//		    } catch (final IOException iException) {
//		      iException.printStackTrace();
//		    } finally {
//		      if (iOutStream != null) {
//		        try {
//		          iOutStream.flush();
//		          iOutStream.close();
//		          iStream.close();
//		        } catch (final IOException iException) {
//		          iException.printStackTrace();
//		        }
//		      }
//		    }
//		    System.load(iDllFile.getAbsolutePath());
//		  }
	}

	public void actionPerformed(ActionEvent e) {
//		CadreImage cadre = controler.getModele().getListCadreImage().get(controler.getModele().getInterfaceGraphique().getTabbedPane().getSelectedIndex());
//		BufferedImage image = cadre.getImage();
//
//		//System.out.println("java.library.path="+System.getProperty("java.library.path"));
//        
//		
//		System.out.println("Welcome to OpenCV " + Core.VERSION);
//		Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
//		System.out.println("OpenCV Mat: " + m);
//		Mat mr1 = m.row(1);
//		mr1.setTo(new Scalar(1));
//		Mat mc5 = m.col(5);
//		mc5.setTo(new Scalar(5));
//		System.out.println("OpenCV Mat data:\n" + m.dump());
//
//		myGrabCut(image);
		



	}


	
//	public BufferedImage myGrabCut(BufferedImage bImage) {
		
//		int[] selection = controler.getModele().selection();
//		BufferedImage res = null;
//		//load an image
//        IplImage loadedImage = IplImage.createFrom(Outil.deepCopy(bImage));
//        int width = bImage.getWidth();
//        int height = bImage.getHeight();
//
//        //get the image in the format needed by grabCut
//        IplImage template = IplImage.create(width, height, CvType.CV_8U, 3);
//        IplImage image = IplImage.createIfNotCompatible(loadedImage, template);
//
//        //other parameters used in grabcut
//        IplImage mask = IplImage.create(width, height, 1, 1);
//        CvRect area = cvRect(selection[0], selection[2], selection[1]-selection[0], selection[3]-selection[2]); //This square is around Lenna's shoulder.
//        CvMat bgdModel = new CvMat();
//        CvMat fgdModel = new CvMat();
//
//        //run the grabcut algorithm
//        grabCut(image, mask, area, bgdModel, fgdModel, 0, Imgproc.GC_INIT_WITH_RECT); //CRASH ON THIS LINE
//        grabCut(image, mask, area, bgdModel, fgdModel, 2, Imgproc.GC_EVAL);
//
//        //show results
//        CanvasFrame canvas = new CanvasFrame("In (press any key)");
//        canvas.showImage(image);
//        canvas.waitKey();
//        canvas.setTitle("Out (press any key)");
//        canvas.showImage(image);
//        canvas.waitKey();
//        canvas.dispose();
//
//        //cleanup
//        image.release();
//        mask.release();
//        loadedImage.release();
//        template.release();
//        bgdModel.release();
//        fgdModel.release();
//
//        return res;
//	} 
}
