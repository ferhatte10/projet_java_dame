
package ui;

import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {
		
		//Chosir le même theme que le système d'exploitation
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Céer la fenêtre du jeu de dames
		DameFenetre fenetre = new DameFenetre();
		fenetre.setDefaultCloseOperation(DameFenetre.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}
}
