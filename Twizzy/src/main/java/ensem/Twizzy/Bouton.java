package ensem.Twizzy;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import utilitaireAgreg.MaBibliothequeTraitementImage;

import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class Bouton extends JButton implements MouseListener {
	
	private TypeBtn type;
	private Fenetre fenetre;
	
	public Bouton(String nom,TypeBtn t, Fenetre f) {
		super(nom);
		this.addMouseListener(this);
		this.type=t;
		this.fenetre=f;
		
	}
	
	//Méthode appelée lors du clic de souris
	public void mouseClicked(MouseEvent event) {
		Mat m;
		if (this.type==TypeBtn.Chargement){
			//Ouverture le l'image 
			
			System.loadLibrary("opencv_java2413");
			m=Highgui.imread("resources/img/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			afficheImage("Image testee", m);
		}
		if (this.type==TypeBtn.Conversion){
			System.loadLibrary("opencv_java2413");
			m=Highgui.imread("resources/img/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			Mat transformee=transformeBGRversHSV(m);
			afficheImage("Image HSV", transformee);
		}
		if (this.type==TypeBtn.Masque){
			System.loadLibrary("opencv_java2413");
			m=Highgui.imread("resources/img/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			Mat transformee=transformeBGRversHSV(m);
			Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
			afficheImage("Image saturee", saturee);
		}
		if (this.type==TypeBtn.Contours){
			System.loadLibrary("opencv_java2413");
			m=Highgui.imread("resources/img/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			Mat transformee=transformeBGRversHSV(m);
			Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
			List<MatOfPoint> ListeContours= ExtractContours(saturee);
			List<Mat> ListeContoursDetectes = new ArrayList<Mat>();
			for (MatOfPoint contour: ListeContours  ){
				Mat objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);
				if (objetrond!=null){
					ListeContoursDetectes.add(objetrond);
				}
			}
			afficheContour("Contours", ListeContoursDetectes);
		}
		
	}
	
	private void afficheImage(String title, Mat img){
		MatOfByte matOfByte=new MatOfByte();
		Highgui.imencode(".png",img,matOfByte);
		byte[] byteArray=matOfByte.toArray();
		BufferedImage bufImage=null;
		try{
			InputStream in=new ByteArrayInputStream(byteArray);
			bufImage=ImageIO.read(in);
			JFrame frame=this.fenetre;
			frame.setTitle(title);
			while (frame.getContentPane().getComponentCount()>3) {
				frame.getContentPane().remove(3);
			}
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)), BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void afficheContour(String title, List<Mat> contours){
		int longueur=contours.size();
		for (int i=0; i<longueur; i++) {
			MatOfByte matOfByte=new MatOfByte();
			Highgui.imencode(".png",contours.get(i),matOfByte);
			byte[] byteArray=matOfByte.toArray();
			BufferedImage bufImage=null;
			try{
				InputStream in=new ByteArrayInputStream(byteArray);
				bufImage=ImageIO.read(in);
				JFrame frame=this.fenetre;
				frame.setTitle(title);
				if (frame.getContentPane().getComponentCount()>3 && i==0) {
					frame.getContentPane().remove(3);
				}
				frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
				frame.pack();
				frame.setVisible(true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static Mat transformeBGRversHSV(Mat matriceBGR){
		Mat matriceHSV=new Mat(matriceBGR.height(),matriceBGR.cols(),matriceBGR.type());
		Imgproc.cvtColor(matriceBGR,matriceHSV,Imgproc.COLOR_BGR2HSV);
		return matriceHSV;
	}
	
	//Methode qui permet d'extraire les contours d'une image donnee
		public static List<MatOfPoint> ExtractContours(Mat input) {
			// Detecter les contours des formes trouvées
			int thresh = 100;
			Mat canny_output = new Mat();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfInt4 hierarchy = new MatOfInt4();
			Imgproc.Canny( input, canny_output, thresh, thresh*2);


			/// Find extreme outer contours
			Imgproc.findContours( canny_output, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

			Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
			Random rand = new Random();
			for( int i = 0; i< contours.size(); i++ )
			{
				Scalar color = new Scalar( rand.nextInt(255 - 0 + 1) , rand.nextInt(255 - 0 + 1),rand.nextInt(255 - 0 + 1) );
				Imgproc.drawContours( drawing, contours, i, color, 1, 8, hierarchy, 0, new Point() );
			}
			//afficheImage("Contours",drawing);

			return contours;
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
