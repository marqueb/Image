package Vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class CadreImage extends JComponent {
	private BufferedImage image;
    private Graphics2D buffer;
    
    //constructeur a implementer
    public CadreImage( BufferedImage i)
    {
    	image = i;
    }
    
    public CadreImage( )
    {
    }
    
	public void paintComponent(Graphics g) {
    	int width = getSize().width;
	    int height = getSize().height;
	    Graphics2D drawable = (Graphics2D) g;
        if (image==null)
        {
            image = new BufferedImage(width, height,
                     BufferedImage.TYPE_INT_RGB);
            buffer = this.image.createGraphics();
       	    buffer.setPaint(Color.white);
     	    buffer.fillRect(0, 0, width, height);
     	    buffer.setPaint(Color.black);
        }
       ((Graphics2D)g).drawImage(image,0,0,null);
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
	
}
