package ensem.Twizzy;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
		if (this.type==TypeBtn.Panneaux){
			System.loadLibrary("opencv_java2413");
			m=Highgui.imread("resources/img/p1.jpg",Highgui.CV_LOAD_IMAGE_COLOR);
			Mat transformee=transformeBGRversHSV(m);
			//la methode seuillage est ici extraite de l'archivage jar du meme nom 
			Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
			Mat objetrond = null;

			//Création d'une liste des contours à partir de l'image saturée
			List<MatOfPoint> ListeContours=ExtractContours(saturee);
			int i=0;
			double [] scores=new double [6];
			//Pour tous les contours de la liste
			for (MatOfPoint contour: ListeContours  ){
				i++;
				objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);

				if (objetrond!=null){
					//MaBibliothequeTraitementImage.afficheImage("Objet rond detécté", objetrond);
					scores[0]=Similitude(objetrond,"resources/img/ref30.jpg");
					scores[1]=Similitude(objetrond,"resources/img/ref50.jpg");
					scores[2]=Similitude(objetrond,"resources/img/ref70.jpg");
					scores[3]=Similitude(objetrond,"resources/img/ref90.jpg");
					scores[4]=Similitude(objetrond,"resources/img/ref110.jpg");
					scores[5]=Similitude(objetrond,"resources/img/refdouble.jpg");


					//recherche de l'index du maximum et affichage du panneau detecté
					double scoremax=-1;
					int indexmax=0;
					for(int j=0;j<scores.length;j++){
						if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	
					if(scoremax<0){System.out.println("Aucun Panneau détécté");}
					else{switch(indexmax){
					case -1:;break;
					case 0:System.out.println("Panneau 30 détécté");break;
					case 1:System.out.println("Panneau 50 détécté");break;
					case 2:System.out.println("Panneau 70 détécté");break;
					case 3:System.out.println("Panneau 90 détécté");break;
					case 4:System.out.println("Panneau 110 détécté");break;
					case 5:System.out.println("Panneau interdiction de dépasser détécté");break;
					}}

				}
			}
		}

		if (this.type==TypeBtn.Video){
			AnalyseVideo vod = new AnalyseVideo("resources/img/video2.avi");
			vod.lectureVideo();
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
		public static double Similitude(Mat object,String signfile) {

			// Conversion du signe de reference en niveaux de gris et normalisation
			Mat panneauref = Highgui.imread(signfile);
			Mat graySign = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
			Imgproc.cvtColor(panneauref, graySign, Imgproc.COLOR_BGRA2GRAY);
			Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
			Mat signeNoirEtBlanc=new Mat();
			


			//Conversion du panneau extrait de l'image en gris et normalisation et redimensionnement à la taille du panneau de réference
			Mat grayObject = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
			Imgproc.resize(object, object, graySign.size());
			//afficheImage("Panneau extrait de l'image",object);
			Imgproc.cvtColor(object, grayObject, Imgproc.COLOR_BGRA2GRAY);
			Core.normalize(grayObject, grayObject, 0, 255, Core.NORM_MINMAX);
			//Imgproc.resize(grayObject, grayObject, graySign.size());	
			
			Mat score=new Mat();
			Size taille;
			Core.bitwise_xor(grayObject,graySign,score);
			int sc=Core.countNonZero(score);
			double sco=sc;
			taille=graySign.size();
			double res;
			res=taille.height*taille.width-sco;
			
			return res;
		}
		public static BufferedImage Mat2bufferedImage(Mat image) {
			MatOfByte bytemat = new MatOfByte();
			Highgui.imencode(".jpg", image, bytemat);
			byte[] bytes = bytemat.toArray();
			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage img = null;
			try {
				img = ImageIO.read(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return img;
		}
		public static int identifiepanneau(Mat objetrond){
			
			double [] scores=new double [6];
			int indexmax=-1;
			if (objetrond!=null){
				MaBibliothequeTraitementImage.afficheImage("Objet rond detécté", objetrond);
				scores[0]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/ref30.jpg");
				scores[1]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/ref50.jpg");
				scores[2]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/ref70.jpg");
				scores[3]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/ref90.jpg");
				scores[4]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/ref110.jpg");
				scores[5]=MaBibliothequeTraitementImage.Similitude(objetrond,"resources/img/refdouble.jpg");
				double scoremax=scores[0];
				for(int j=1;j<scores.length;j++){
					if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	
			}
			return indexmax;
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
