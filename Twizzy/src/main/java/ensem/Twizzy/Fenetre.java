package ensem.Twizzy;

/* Import des librairies JFrame et Swing */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class Fenetre extends JFrame {
	static {
		System.loadLibrary("opencv_java2413");
		System.load(new File("").getAbsolutePath() + "/resources/bin/opencv_ffmpeg2413_64.dll");
	}
	
	private Bouton btn_charger = new Bouton("Charger image",TypeBtn.Chargement, this);
	private Bouton btn_conv = new Bouton("Convertir couleur",TypeBtn.Conversion, this);
	private Bouton btn_extcol = new Bouton("Extraire couleur",TypeBtn.Masque, this);
	private Bouton btn_cont = new Bouton("Detecter contours",TypeBtn.Contours, this);
	private Bouton btn_extpan = new Bouton("Extraire panneau",TypeBtn.Panneaux, this);
	private Bouton btn_video = new Bouton("Analyser video",TypeBtn.Video, this);
	private Bouton btn_stop = new Bouton("Stopper video",TypeBtn.Stop, this);
	
	private JLabel txt_info = new JLabel("Informations sur évènements");
	
	private JLabel txt_seuil = new JLabel("Seuils couleur");
	private JLabel infoMin = new JLabel("Min");
	private JLabel infoMax = new JLabel("Max");
	
	
	private JTextField inMin1 = new JTextField("0");
	private JTextField inMin2 = new JTextField("160");
	private JTextField inMax1 = new JTextField("6");
	private JTextField inMax2 = new JTextField("179");
	private JButton btn_def = new JButton("Valeurs par défaut");
	private JButton btn_nouv = new JButton("Nouvelles valeurs");
	
	private JPanel btnPan = new JPanel();
	private JPanel entPan = new JPanel();
	private Panneau imgPan = new Panneau("resources/img/blanc.jpg");
	private JPanel conteneur = new JPanel();
	
	private Mat img, transformee, saturee, objetrond;
	private List<MatOfPoint> ListeContours;
	private List<Mat> ListeContoursDetectes;
	
	private ThreadVideo video = new ThreadVideo("",this);
	
	public Fenetre(int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		this.setTitle("PimpMyTwizzy");
		this.setSize(width,height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		btnPan.setPreferredSize(new Dimension(width/5,height));
		btnPan.setBackground(Color.gray);
		btnPan.setLayout(new GridBagLayout());
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;		// GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		btnPan.add(btn_charger,gbc);
		
		gbc.gridy = 2;
		btnPan.add(btn_conv,gbc);
		
		gbc.gridy = 4;
		btnPan.add(btn_extcol,gbc);
		
		gbc.gridy = 6;
		btnPan.add(btn_cont,gbc);
		
		gbc.gridy = 8;
		btnPan.add(btn_extpan,gbc);
		
		gbc.gridy = 10;
		btnPan.add(btn_video,gbc);
		
		gbc.gridy = 12;
		btnPan.add(btn_stop,gbc);
		
		
		entPan.setPreferredSize(new Dimension(width/5,height));
		btnPan.setBackground(Color.gray);
		
		/*
		entPan.setLayout(new GridLayout(4,2));
		entPan.add(txt_seuil);
		// entPan.add(btn_def);
		entPan.add(infoMin);
		entPan.add(infoMax);
		entPan.add(inMin1);
		entPan.add(inMin2);
		entPan.add(inMax1);
		entPan.add(inMax2);
		// entPan.add(btn_nouv);
		*/
		
		/*
		entPan.setLayout(new GridBagLayout());
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		entPan.add(txt_seuil,gbc);
		
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		entPan.add(infoMin,gbc);
		
		gbc.gridx = 1;
		entPan.add(infoMax,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		entPan.add(inMin1,gbc);
		
		gbc.gridx = 1;
		entPan.add(inMax1,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		entPan.add(inMin2,gbc);
		
		gbc.gridx = 1;
		entPan.add(inMax2,gbc);
		*/
		
		conteneur.setBackground(Color.white);
		conteneur.setLayout(new BorderLayout());
		conteneur.add(btnPan,BorderLayout.WEST);
		conteneur.add(entPan,BorderLayout.EAST);
		conteneur.add(imgPan,BorderLayout.CENTER);
		conteneur.add(txt_info,BorderLayout.SOUTH);
		
		this.setContentPane(conteneur);
		this.setVisible(true);
	}
	
	public Panneau getImgPan() {
		return this.imgPan;
	}
	
	public JPanel getConteneur() {
		return this.conteneur;
	}
	
	public void actionBtnCharger(String des) {
		this.imgPan.setDest(des);
		this.imgPan.repaint();
		
		this.img = Highgui.imread(des,Highgui.CV_LOAD_IMAGE_COLOR);
		this.txt_info.setText("Image chargée avec succès");
	}
	
	public void actionBtnConv() {
		this.transformee = transformeBGRversHSV(this.img);
		afficheImage("Image HSV", this.transformee);
		this.txt_info.setText("Conversion couleurs de l'image ...");
	}
	
	public void actionBtnMasque() {
		this.saturee = seuillage(this.transformee, 6, 170, 110);
		afficheImage("Image saturee", this.saturee);
		this.txt_info.setText("Saturation de l'image pour masque ...");
	}
	
	public void actionBtnCont() {
		this.ListeContours = ExtractContours(this.saturee);
		this.ListeContoursDetectes = new ArrayList<Mat>();
		for (MatOfPoint contour: ListeContours  ){
			Mat objetrond = DetectForm(this.img,contour);
			if (objetrond != null){
				this.ListeContoursDetectes.add(objetrond);
			}
		}
		afficheContour("Contours", this.ListeContoursDetectes);
		this.txt_info.setText("Détection de contours sur l'image ...");
	}
	
	public void actionBtnPan() {
		this.objetrond = null;

		int i = 0;
		double [] scores = new double [6];
		//Pour tous les contours de la liste
		for (MatOfPoint contour : ListeContours){
			i++;
			this.objetrond = DetectForm(this.img,contour);

			if (this.objetrond != null) {
				//MaBibliothequeTraitementImage.afficheImage("Objet rond detécté", objetrond);
				scores[0] = Similitude(this.objetrond,"resources/img/ref30.jpg");
				scores[1] = Similitude(this.objetrond,"resources/img/ref50.jpg");
				scores[2] = Similitude(this.objetrond,"resources/img/ref70.jpg");
				scores[3] = Similitude(this.objetrond,"resources/img/ref90.jpg");
				scores[4] = Similitude(this.objetrond,"resources/img/ref110.jpg");
				scores[5] = Similitude(this.objetrond,"resources/img/refdouble.jpg");

				//recherche de l'index du maximum et affichage du panneau detecté
				double scoremax = -1;
				int indexmax = 0;
				
				for (int j = 0; j < scores.length; j++) {
					if (scores[j] > scoremax) { scoremax = scores[j]; indexmax=j; } 
				}	
				if (scoremax < 0) {
					this.txt_info.setText("Aucun Panneau détécté");
				}
				else {
					switch (indexmax) {
						case -1: break;
						case 0: this.txt_info.setText("Panneau 30 détécté");break;
						case 1: this.txt_info.setText("Panneau 50 détécté");break;
						case 2: this.txt_info.setText("Panneau 70 détécté");break;
						case 3: this.txt_info.setText("Panneau 90 détécté");break;
						case 4: this.txt_info.setText("Panneau 110 détécté");break;
						case 5: this.txt_info.setText("Panneau interdiction de dépasser détécté");break;
					}
				}

			}
		}
	}
	
	public void actionBtnVideo(String des) {
		this.video.setSource(des);
		
		this.video.start();
		this.txt_info.setText("Analyse vidéo lancée ...");
	}

	public void actionBtnStop() {
		
		this.video.interrupt();
		this.txt_info.setText("Analyse vidéo stoppée ...");
	}
	
	public void afficheImage(String title, Mat im){
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg",im,matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try{
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
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
		for (int i = 0; i < longueur; i++) {
			MatOfByte matOfByte = new MatOfByte();
			Highgui.imencode(".png",contours.get(i),matOfByte);
			byte[] byteArray = matOfByte.toArray();
			BufferedImage bufImage=null;
			try{
				InputStream in = new ByteArrayInputStream(byteArray);
				bufImage = ImageIO.read(in);
				JFrame frame = new JFrame();
				frame.setTitle(title);
				if (frame.getContentPane().getComponentCount() > 3 && i == 0) {
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
	
	public Mat transformeBGRversHSV(Mat matriceBGR){
		Mat matriceHSV = new Mat(matriceBGR.height(),matriceBGR.cols(),matriceBGR.type());
		Imgproc.cvtColor(matriceBGR,matriceHSV,Imgproc.COLOR_BGR2HSV);
		return matriceHSV;
	}
	
	//Methode qui permet d'extraire les contours d'une image donnee
	public List<MatOfPoint> ExtractContours(Mat input) {
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
	
	//Methode qui permet de decouper et identifier les contours carrés, triangulaires ou rectangulaires.
    //Renvoie null si aucun contour rond n'a été trouvé.   
    //Renvoie une matrice carrée englobant un contour rond si un contour rond a été trouvé
    public static Mat DetectForm(Mat img, MatOfPoint contour) {
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        float[] radius = new float[1];
        Point center = new Point();
        Rect rect = Imgproc.boundingRect(contour);
        double contourArea = Imgproc.contourArea(contour);


        matOfPoint2f.fromList(contour.toList());
        // Cherche le plus petit cercle entourant le contour
        Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
        //on dit que c'est un cercle si l'aire occupé par le contour est à supérieure à  80% de l'aire occupée par un cercle parfait
        if ((contourArea / (Math.PI*radius[0]*radius[0])) >=0.8) {
            Core.circle(img, center, (int)radius[0], new Scalar(255, 0, 0), 2);
            Core.rectangle(img, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar (0, 255, 0), 2);
            Mat tmp = img.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
            Mat sign = Mat.zeros(tmp.size(),tmp.type());
            tmp.copyTo(sign);
            return sign;
        }else {

            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            if (total == 3 ) { // is triangle
                Point [] pt = approxCurve.toArray();
                Core.line(img, pt[0], pt[1], new Scalar(255,0,0),2);
                Core.line(img, pt[1], pt[2], new Scalar(255,0,0),2);
                Core.line(img, pt[2], pt[0], new Scalar(255,0,0),2);
                Core.rectangle(img, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar (0, 255, 0), 2);
                Mat tmp = img.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
                Mat sign = Mat.zeros(tmp.size(),tmp.type());
                tmp.copyTo(sign);
                return null;
            }
            if (total >= 4 && total <= 6) {
                List<Double> cos = new ArrayList<>();
                Point[] points = approxCurve.toArray();
                for (int j = 2; j < total + 1; j++) {
                    cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                }
                Collections.sort(cos);
                Double minCos = cos.get(0);
                Double maxCos = cos.get(cos.size() - 1);
                boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
                if (isRect) {
                    double ratio = Math.abs(1 - (double) rect.width / rect.height);
                    //drawText(rect.tl(), ratio <= 0.02 ? "SQU" : "RECT");
                    Core.rectangle(img, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar (0, 255, 0), 2);
                    Mat tmp = img.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
                    Mat sign = Mat.zeros(tmp.size(),tmp.type());
                    tmp.copyTo(sign);
                    return null;
                }
                if (isPolygon) {
                    //drawText(rect.tl(), "Polygon");
                }
            }           
        }
        return null;
    }
    
    public static double angle(Point a, Point b, Point c) {
        Point ab = new Point( b.x - a.x, b.y - a.y );
        Point cb = new Point( b.x - c.x, b.y - c.y );
        double dot = (ab.x * cb.x + ab.y * cb.y); // dot product
        double cross = (ab.x * cb.y - ab.y * cb.x); // cross product
        double alpha = Math.atan2(cross, dot);
        return Math.floor(alpha * 180. / Math.PI + 0.5);
    }
    
	public double Similitude(Mat object,String signfile) {

		// Conversion du signe de reference en niveaux de gris et normalisation
		Mat panneauref = Highgui.imread(signfile);
		Mat graySign = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
		Imgproc.cvtColor(panneauref, graySign, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
		Mat signeNoirEtBlanc = new Mat();
		


		//Conversion du panneau extrait de l'image en gris et normalisation et redimensionnement à la taille du panneau de réference
		Mat grayObject = new Mat(panneauref.rows(), panneauref.cols(), panneauref.type());
		Imgproc.resize(object, object, graySign.size());
		//afficheImage("Panneau extrait de l'image",object);
		Imgproc.cvtColor(object, grayObject, Imgproc.COLOR_BGRA2GRAY);
		Core.normalize(grayObject, grayObject, 0, 255, Core.NORM_MINMAX);
		//Imgproc.resize(grayObject, grayObject, graySign.size());	
		
		Mat score = new Mat();
		Size taille;
		Core.bitwise_xor(grayObject,graySign,score);
		int sc = Core.countNonZero(score);
		double sco = sc;
		taille = graySign.size();
		double res;
		res = taille.height*taille.width-sco;
		
		return res;
	}
	
	public BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public int identifiepanneau(Mat objetrond){
		
		double [] scores = new double [6];
		int indexmax = -1;
		if (objetrond != null){
			afficheImage("Objet rond detécté", objetrond);
			scores[0] = Similitude(objetrond,"resources/img/ref30.jpg");
			scores[1] = Similitude(objetrond,"resources/img/ref50.jpg");
			scores[2] = Similitude(objetrond,"resources/img/ref70.jpg");
			scores[3] = Similitude(objetrond,"resources/img/ref90.jpg");
			scores[4] = Similitude(objetrond,"resources/img/ref110.jpg");
			scores[5] = Similitude(objetrond,"resources/img/refdouble.jpg");
			double scoremax = scores[0];
			for(int j = 1; j < scores.length; j++){
				if (scores[j] > scoremax){ scoremax = scores[j]; indexmax=j;} }	
		}
		return indexmax;
	}
	
	public Mat seuillage(Mat input, int seuilRougeOrange, int seuilRougeViolet,int seuilSaturation){
        //à completer
        Vector<Mat> channels = splitHSVChannels(input);
        Scalar rougeviolet = new Scalar(seuilRougeViolet);
        Scalar rougeorange = new Scalar(seuilRougeOrange);
        Scalar sat = new Scalar(seuilSaturation);
        Mat or =new Mat();
        Mat finale= new Mat();
        Mat Herve = new Mat();
        Core.compare(channels.get(0), rougeviolet, Herve, Core.CMP_GT);
        Mat Heros = new Mat();
        Core.compare(channels.get(0),rougeorange,Heros,Core.CMP_LT);
        Mat rouges = new Mat();
        Core.compare(channels.get(1),sat,rouges,Core.CMP_GT);
        //image saturée à retourner
        Core.bitwise_or(Herve, Heros,or);
        Core.bitwise_and(or, rouges,finale );
        return finale;
    }
	
	//Methode qui convertit une matrice avec 3 cannaux en un vecteur de 3 matrices monocanal (un canal par couleur)
    public Vector<Mat> splitHSVChannels(Mat input) {
        Vector<Mat> channels = new Vector<Mat>();
        Core.split(input, channels);
        return channels;
    }
    
    public void lectureVideo(String des) {		
		VideoCapture camera = new VideoCapture();
		camera.open(des);
		
		if(!camera.isOpened()){
	        this.txt_info.setText("Camera Error");
	    }
	    else{
	    	this.txt_info.setText("Camera OK ? ");
	    }
		
		JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo"); // this;	// pour la fenetre actuelle
		//jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);
		Mat frame = new Mat();
		Mat PanneauAAnalyser = null;
		camera.read(frame);
		ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
		
			while (camera.read(frame)) {
			image.setImage(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
			
			Mat m = frame;
			
			Mat transformee = transformeBGRversHSV(m);
			//la methode seuillage est ici extraite de l'archivage jar du meme nom 
			Mat saturee = seuillage(transformee, 6, 170, 110);
			Mat objetrond = null;

			//Création d'une liste des contours à partir de l'image saturée
			List<MatOfPoint> ListeContours = ExtractContours(saturee);
			int i = 0;
			double [] scores=new double [6];
			//Pour tous les contours de la liste
			for (MatOfPoint contour: ListeContours  ){
				i++;
				objetrond = DetectForm(m,contour);
				

				if (objetrond != null){
					switch(identifiepanneau(objetrond)){
						case -1:break;
						case 0:this.txt_info.setText("Panneau 30 détécté");break;
						case 1:this.txt_info.setText("Panneau 50 détécté");break;
						case 2:this.txt_info.setText("Panneau 70 détécté");break;
						case 3:this.txt_info.setText("Panneau 90 détécté");break;
						case 4:this.txt_info.setText("Panneau 110 détécté");break;
						case 5:this.txt_info.setText("Panneau interdiction de dépasser détécté");break;
						}
					}
				}
			}
		}
}
