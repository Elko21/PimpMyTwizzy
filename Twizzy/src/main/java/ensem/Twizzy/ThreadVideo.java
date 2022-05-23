package ensem.Twizzy;

import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class ThreadVideo extends Thread{
	private String source;
	private Fenetre fenetre;
	
	public ThreadVideo(String src, Fenetre f) {
		this.source = src;
		this.fenetre = f;
	}
	
	public void setSource(String src) {
		this.source = src;
	}
	
	public void run() {
		this.fenetre.lectureVideo(this.source);
	}

}
