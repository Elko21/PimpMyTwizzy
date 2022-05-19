

import java.util.Arrays;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.*;

public class AnalyseImage {
		static {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		}
		Mat m;
		
		public AnalyseImage(String imageFournie) {
		this.m=Highgui.imread(imageFournie,Highgui.CV_LOAD_IMAGE_COLOR);
		//MaBibliothequeTraitementImage.afficheImage("Image test�e", m);
		//Mat transformee=MaBibliothequeTraitementImage.transformeBGRversHSV(m);
		//la methode seuillage est ici extraite de l'archivage jar du meme nom 
		//Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);

		//Cr�ation d'une liste des contours � partir de l'image satur�e
		//List<MatOfPoint> ListeContours= MaBibliothequeTraitementImage .ExtractContours(saturee);
		}
		
		public void determinationImage() {
		MaBibliothequeTraitementImage.afficheImage("Image test�e", m);
		Mat transformee=MaBibliothequeTraitementImage.transformeBGRversHSV(m);
			//la methode seuillage est ici extraite de l'archivage jar du meme nom 
		Mat saturee=MaBibliothequeTraitementImage.seuillage(transformee, 6, 170, 110);

			//Cr�ation d'une liste des contours � partir de l'image satur�e
		List<MatOfPoint> ListeContours= MaBibliothequeTraitementImage .ExtractContours(saturee);
		int i=0;
		double [] scores=new double [6];
		Mat objetrond = null;
		//Pour tous les contours de la liste
		for (MatOfPoint contour: ListeContours  ){
			i++;
			objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);

			if (objetrond!=null){
				MaBibliothequeTraitementImage.afficheImage("Objet rond det�ct�", objetrond);
				scores[0]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref30.jpg");
				scores[1]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref50.jpg");
				scores[2]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref70.jpg");
				scores[3]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref90.jpg");
				scores[4]=MaBibliothequeTraitementImage.Similitude(objetrond,"ref110.jpg");
				scores[5]=MaBibliothequeTraitementImage.Similitude(objetrond,"refdouble.jpg");


				//recherche de l'index du maximum et affichage du panneau detect�
				double scoremax=10000;
				int indexmax=0;
				for(int j=0;j<scores.length;j++){
					if (scores[j]<scoremax){scoremax=scores[j];indexmax=j;}}	
				if(scoremax<0){System.out.println("Aucun Panneau d�t�ct�");}
				else{switch(indexmax){
				case -1:;break;7 MAI ANNIVERSAIRE ELKO
				case 0:System.out.println("Panneau 30 d�t�ct�");break;
				case 1:System.out.println("Panneau 50 d�t�ct�");break;
				case 2:System.out.println("Panneau 70 d�t�ct�");break;
				case 3:System.out.println("Panneau 90 d�t�ct�");break;
				case 4:System.out.println("Panneau 110 d�t�ct�");break;
				case 5:System.out.println("Panneau interdiction de d�passer d�t�ct�");break;
				}
				}

			}
		}
		}
}
		
	


