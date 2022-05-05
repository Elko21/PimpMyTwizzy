package ensem.Twizzy;

/* Import des librairies JFrame et Swing */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.awt.BorderLayout;
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

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;



public class Fenetre extends JFrame {
	private Bouton btn_charger = new Bouton("Charger image",TypeBtn.Chargement, this);
	private Bouton btn_conv = new Bouton("Convertir couleur",TypeBtn.Conversion, this);
	private Bouton btn_extcol = new Bouton("Extraire couleur",TypeBtn.Masque, this);
	private Bouton btn_cont = new Bouton("Detecter contours",TypeBtn.Contours, this);
	private Bouton btn_extpan = new Bouton("Extraire panneau",TypeBtn.Panneaux, this);
	
	private JLabel txt_info = new JLabel("Informations sur évènements");
	private JLabel txt_seuil = new JLabel("Seuils couleur");
	private JLabel infoMin = new JLabel("Min");
	private JLabel infoMax = new JLabel("Max");
	
	
	private JTextField inMin1 = new JTextField("0");
	private JTextField inMin2 = new JTextField("160");
	private JTextField inMax1 = new JTextField("6");
	private JTextField inMax2 = new JTextField("179");
	
	
	private JPanel btnPan = new JPanel();
	private JPanel entPan = new JPanel();
	private Panneau imgPan = new Panneau("resources/img/p1.jpg");
	private JPanel conteneur = new JPanel();
	
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
		
		entPan.setPreferredSize(new Dimension(width/5,height));
		btnPan.setBackground(Color.gray);
		
		conteneur.setBackground(Color.white);
		conteneur.setLayout(new BorderLayout());
		conteneur.add(btnPan,BorderLayout.WEST);
		conteneur.add(entPan,BorderLayout.EAST);
		//conteneur.add(imgPan,BorderLayout.CENTER);
		conteneur.add(txt_info,BorderLayout.SOUTH);
		
		this.setContentPane(conteneur);
		this.setVisible(true);
	}
	
	/*
	public static Mat LectureImage(String fichier) {
		File f = new File(fichier);
		Mat m = Highgui.imread(f.getAbsolutePath());
		return m;
	}

	public static void ImShow(String title, Mat img) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;

		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
