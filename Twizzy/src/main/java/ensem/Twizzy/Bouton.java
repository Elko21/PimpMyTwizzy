package ensem.Twizzy;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class Bouton extends JButton implements MouseListener {
	private TypeBtn type;
	private Fenetre fenetre;
	private Mat img;
	
	public Bouton(String nom,TypeBtn t, Fenetre f) {
		super(nom);
		this.type = t;
		this.addMouseListener(this);
		this.fenetre = f;
		this.img = null;
	}
	
	//Méthode appelée lors du clic de souris
	public void mouseClicked(MouseEvent event) {
		
		if (this.type == TypeBtn.Chargement){
			String baseDir = new File("").getAbsolutePath();
			JFileChooser fc = new JFileChooser(new File(baseDir));
			String relPath = "";
			
			int returnVal;
			do {
				 returnVal = fc.showOpenDialog(this.fenetre);
			} while (returnVal != JFileChooser.APPROVE_OPTION && returnVal != JFileChooser.CANCEL_OPTION);
			
			if (returnVal != JFileChooser.CANCEL_OPTION) {
				File file = fc.getSelectedFile();
				String ext = getExtension(file);
				
				if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
					relPath = cheminRelatif(new File(""),file);
					this.fenetre.actionBtnCharger(relPath);
				}
				else {
					this.fenetre.afficherException("Image non chargée car extension non supportée ... Réessayez avec une image JPEG ou PNG");
				}
			}			
		}
		
		if (this.type == TypeBtn.Conversion){
			this.fenetre.actionBtnConv();
		}
		
		if (this.type==TypeBtn.Masque){
			this.fenetre.actionBtnMasque();
		}
		
		if (this.type==TypeBtn.Contours){
			this.fenetre.actionBtnCont();
		}
		
		if (this.type==TypeBtn.Panneaux){
			this.fenetre.actionBtnPan();
		}

		if (this.type==TypeBtn.Video){
			String baseDir = new File("").getAbsolutePath();
			JFileChooser fc = new JFileChooser(new File(baseDir));
			String relPath = "";
			
			int returnVal;
			do {
				 returnVal = fc.showOpenDialog(this.fenetre);
			} while (returnVal != JFileChooser.APPROVE_OPTION && returnVal != JFileChooser.CANCEL_OPTION);
			
			if (returnVal != JFileChooser.CANCEL_OPTION) {
				File file = fc.getSelectedFile();
				String ext = getExtension(file);
				
				if (ext.equals("avi") || ext.equals("mp4")) {
					relPath = cheminRelatif(new File(""),file);
					this.fenetre.actionBtnVideo(relPath);
				}
				else {
					this.fenetre.afficherException("Vidéo non chargée car extension non supportée ... Réessayez avec une vidéo AVI ou MP4");
				}	
			}			
		}
		
		if (this.type==TypeBtn.Stop) {
			this.fenetre.actionBtnStop();
		}
		
	}
	    
	public String cheminRelatif(File rcn, File cbl) {
		URI path1 = cbl.toURI();
		URI path2 = rcn.toURI();
		
		return  path2.relativize(path1).getPath();
	}

	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
