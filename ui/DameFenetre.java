package ui;

import java.awt.*;
import java.io.Serial;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DameFenetre extends JFrame {

	@Serial
	private static final long serialVersionUID = 8782122389400590079L;
	private final PlateauDames Plateau;
	public static final String Titre = "Jeu de Dame en Java";
	public static final int Longueur = 620;
	public static final int Largeur = 600;
	


	public DameFenetre(int width, int height, String title) {
		
		super(title);
		super.setSize(width, height);
		super.setMinimumSize(new Dimension(width, height));
		super.setLocationRelativeTo(null);

		this.Plateau = new PlateauDames(this);

		JPanel layout = new JPanel(new BorderLayout());
		layout.add(Plateau, BorderLayout.CENTER);

		PanelOptions options = new PanelOptions(this);
		layout.add(options, BorderLayout.SOUTH);

		this.add(layout);

	}
	public DameFenetre() { // Constructeur par défaut
		this(Longueur, Largeur, Titre);
	}

	public void restart() {
		this.Plateau.setSelected(null);
		this.Plateau.getPartie().restart();
		this.Plateau.update();
	}


}
