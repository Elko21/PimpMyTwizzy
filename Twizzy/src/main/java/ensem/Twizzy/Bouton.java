package ensem.Twizzy;

import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Bouton extends JButton implements MouseListener {
	static private String nom = "Charger image";
	static private TypeBtn type;
	
	public Bouton(TypeBtn t){
		super(nom);
		this.type = t;
		this.addMouseListener(this);
	}
	
	//Méthode appelée lors du clic de souris
	public void mouseClicked(MouseEvent event) { }
	
	//Méthode appelée lors du survol de la souris
	public void mouseEntered(MouseEvent event) { }
	
	//Méthode appelée lorsque la souris sort de la zone du bouton
	public void mouseExited(MouseEvent event) { }
	
	//Méthode appelée lorsque l'on presse le bouton gauche de la souris
	public void mousePressed(MouseEvent event) { }
	
	//Méthode appelée lorsque l'on relâche le clic de souris
	public void mouseReleased(MouseEvent event) { }
}
