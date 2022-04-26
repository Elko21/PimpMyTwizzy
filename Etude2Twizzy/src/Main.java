import java.awt.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class Main {
	
	public static void main(String[] args) {
		//AnalyseVideo vod = new AnalyseVideo("video1.avi");
		//vod.lectureVideo();
		AnalyseImage image = new AnalyseImage("p10.jpg");
		System.out.println("Panneau découvert pour l'image");
		image.determinationImage();
	}

}
