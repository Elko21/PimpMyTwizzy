package ensem.Twizzy;

import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class ThreadVideo extends Thread{
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.load("C:\\Users\\polob\\Downloads\\opencv\\build\\x64\\vc14\\bin\\opencv_ffmpeg2413_64.dll");
	}
	//private string video;
	
	//public ThreadVideo(string lienvideo)
	public ThreadVideo() {
		//this.video = lienvideo;
	}
	
	public void run() {
		//AnalyseVideo.LectureVideo(this.video);
		AnalyseVideo.lectureVideo();
	}

}
