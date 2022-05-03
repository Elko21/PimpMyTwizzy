package ensem.Twizzy;

/* Import des librairies JFrame et Swing */
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class Panneau extends JPanel {
	private String dest;
	
	public Panneau(String emp) {
		this.dest = emp;
	}
	
	public void paintComponent(Graphics g) {
		try {
			Image img = ImageIO.read(new File(this.dest));
			g.drawImage(img,0,0,this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
