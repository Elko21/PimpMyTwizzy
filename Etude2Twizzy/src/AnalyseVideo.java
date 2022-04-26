import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
public class AnalyseVideo {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.load("C:\\Program Files\\opencv\\build\\x64\\vc14\\bin\\opencv_ffmpeg2413_64.dll");
	}



	public static void main(String[] args) {
		JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture();
		camera.open("video1.avi");
		
		if(!camera.isOpened()){
	        System.out.println("Camera Error");
	    }
	    else{
	        System.out.println("Camera OK?");
	    }

		Mat PanneauAAnalyser = null;
		camera.read(frame);
		ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
		System.out.println(frame);
		
			while (camera.read(frame)) {
			//A completer
				
			

			image.setImage(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
			
			Mat m=frame;
			
			Mat transformee=MaBibliothequeTraitementImage.transformeBGRversHSV(m);
			//la methode seuillage est ici extraite de l'archivage jar du meme nom 
			Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);
			Mat objetrond = null;

			//Création d'une liste des contours à partir de l'image saturée
			List<MatOfPoint> ListeContours= MaBibliothequeTraitementImage .ExtractContours(saturee);
			int i=0;
			double [] scores=new double [6];
			//Pour tous les contours de la liste
			for (MatOfPoint contour: ListeContours  ){
				i++;
				objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);

				if (objetrond!=null){
					
					switch(identifiepanneau(objetrond)){
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
			//MaBibliothequeTraitementImage.afficheImage("Objet rond detécté", objetrond);
			scores[0]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref30.jpg");
			scores[1]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref50.jpg");
			scores[2]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref70.jpg");
			scores[3]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref90.jpg");
			scores[4]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref110.jpg");
			scores[5]=MaBibliothequeTraitementImage.Similitude(objetrond,"refdouble.jpg");

			double scoremax=scores[0];

			for(int j=1;j<scores.length;j++){
				if (scores[j]<scoremax){scoremax=scores[j];indexmax=j;}}	


		}
		return indexmax;
	}


}