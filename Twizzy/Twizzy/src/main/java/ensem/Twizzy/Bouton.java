package ensem.Twizzy;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import utilitaireAgreg.MaBibliothequeTraitementImage;

import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.awt.event.MouseEvent;

public class Bouton extends JButton implements MouseListener {
	
	private TypeBtn type;
	
	public Bouton(String nom,TypeBtn t) {
		super(nom);
		this.addMouseListener(this);
		this.type=t;
		
	}
	
	//Méthode appelée lors du clic de souris
	public void mouseClicked(MouseEvent event) {
		if (this.type==TypeBtn.Chargement){
			//Ouverture le l'image 
			
			System.loadLibrary("opencv_java2413");
			Mat m=Highgui.imread("resources/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			afficheImage("Image testée", m);
		}
	}
	public static void afficheImage(String title, Mat img){
		MatOfByte matOfByte=new MatOfByte();
		Highgui.imencode(".png",img,matOfByte);
		byte[] byteArray=matOfByte.toArray();
		BufferedImage bufImage=null;
		try{
			InputStream in=new ByteArrayInputStream(byteArray);
			bufImage=ImageIO.read(in);
			JFrame frame=new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);

		}
		catch(Exception e){
			e.printStackTrace();
		}


	}
	
	
	//Méthode appelée lors du survol de la souris
	public void mouseEntered(MouseEvent event) { }
	
	//Méthode appelée lorsque la souris sort de la zone du bouton
	public void mouseExited(MouseEvent event) { }
	
	//Méthode appelée lorsque l'on presse le bouton gauche de la souris
	public void mousePressed(MouseEvent event) { }
	
	//Méthode appelée lorsque l'on relâche le clic de souris
	public void mouseReleased(MouseEvent event) { }
}
