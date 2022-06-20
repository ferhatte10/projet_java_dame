package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import logic.GenerateurDeplacement;
import model.Plateau;
import model.Partie;
import model.Joueur;
import java.io.Serial;



public class PlateauDames extends JButton{

	@Serial
	private static final long serialVersionUID = -6014690893709316364L;
	private Joueur J1;
	private Joueur J2;
	private final Partie partie;
	private final DameFenetre fenetre;

	private Color CaseBlanche;
	private Color CaseNoire;
	private Point selected;
	private boolean isGameOver;
	private boolean selectionValid;
	private static final int PADDING = 16;
	private int nbWhite;
	private int nbWhiteK;
	private int nbBlack;
	private int nbBlackK;




	public PlateauDames(DameFenetre fenetre, Partie partie, Joueur J1, Joueur J2) {
		
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		
		// mis en place du jeu
		this.partie = (partie == null)? new Partie() : partie;
		this.fenetre = fenetre;
		this.CaseBlanche = Color.WHITE;
		this.CaseNoire = Color.BLACK;

		setP1(J1);
		setP2(J2);
	}
	public PlateauDames(DameFenetre fenetre) { // constructeur par défaut
		this(fenetre, new Partie(), null, null);
	}
	public void update() {
		this.nbBlack  	= partie.getBCLeft();
		this.nbBlackK 	= partie.getBCKLeft();
		this.nbWhite  	= partie.getWCLeft();
		this.nbWhiteK 	= partie.getWCKLeft();
		this.isGameOver = partie.isGameOver();
		repaint();
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public synchronized boolean setGameState(boolean testValue, String newState, String expected) {
		
		if (testValue && !partie.getGameState().equals(expected)) {
			return false;
		}
		
		this.partie.setGameState(newState);
		repaint();
		
		return true;
	}


	public int getNbWhite() {
		return nbWhite;
	}

	public int getNbWhiteK() {
		return nbWhiteK;
	}

	public int getNbBlack() {
		return nbBlack;
	}

	public int getNbBlackK() {
		return nbBlackK;
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Partie partie = this.partie.copy();


		final int Longueur = getWidth();
		final int Largeur = getHeight();

		final int DIM = Math.min(Longueur, Largeur);
		final int BOX_SIZE = (DIM - 2 * PADDING) / 8;

		final int OFFSET_X = (Longueur - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (Largeur - BOX_SIZE * 8) / 2;

		final int BOX_PADDING = 4;
		final int Taille_damier = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);
		
		// dessiner le plateau

		g.setColor(Color.BLACK);
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
		g.setColor(CaseBlanche);
		g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		g.setColor(CaseNoire);

		for (int i = 0; i < 8; i ++) {
			for (int j = (i + 1) % 2; j < 8; j += 2) {
				g.fillRect(OFFSET_X + j * BOX_SIZE, OFFSET_Y + i * BOX_SIZE, BOX_SIZE, BOX_SIZE);
			}
		}

		// si il a le droit de selectionner la case ca sera vert sinon rouge
		if (Plateau.isValidPoint(selected)) {
			g.setColor(selectionValid? Color.GREEN : Color.RED);
			g.fillRect(OFFSET_X + selected.x * BOX_SIZE, OFFSET_Y + selected.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
		}
		
		// dessiner les pions
		Plateau b = partie.getPlateau();
		for (int y = 0; y < 8; y ++) {
			int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
			for (int x = (y + 1) % 2; x < 8; x += 2) {

				int id = b.get(x, y); // get l'id de la case pour savoir si c'est la pos d'un pion noir, blanc ou dame noire, blanche
				

				if (id == Plateau.Vide) {
					continue;
				}
				
				int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;

				// pions noirs
				if (id == Plateau.Pion_blanc) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.BLACK);
					g.fillOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, Taille_damier, Taille_damier);
				}
				// pions blancs
				else if (id == Plateau.Pion_noir) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.WHITE);
					g.fillOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, Taille_damier, Taille_damier);
				}
				
				// dame blanche
				else if (id == Plateau.Dame_blanche) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.BLACK);
					g.fillOval(cx - 1, cy - 2, Taille_damier, Taille_damier);
				}
				// dame noir
				else if (id == Plateau.Dame_noire) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, Taille_damier, Taille_damier);
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, Taille_damier, Taille_damier);
					g.setColor(Color.WHITE);
					g.fillOval(cx - 1, cy - 2, Taille_damier, Taille_damier);
				}
				
				// Rajouter des effet pour n'importe quelle dame
				if (id == Plateau.Dame_noire || id == Plateau.Dame_blanche) {
					g.setColor(new Color(255, 209, 55));
					g.drawOval(cx - 1, cy - 2, Taille_damier, Taille_damier);
					g.drawOval(cx + 1, cy, Taille_damier - 4, Taille_damier - 4);
				}
			}
		}
		
		// Afficher le message qui indique a qui est le tour
		String msg = partie.isP1Turn()? "Joueur 1 qui joue" : "Joueur 2 qui joue";
		int width = g.getFontMetrics().stringWidth(msg);
		Color back = partie.isP1Turn()? Color.WHITE : Color.BLACK;
		Color front = partie.isP1Turn()? Color.BLACK : Color.WHITE;

		if (!isGameOver){
			g.setColor(back);
			g.fillRect(Longueur / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2, width + 11, 15);
			g.setColor(front);
			g.drawString(msg, Longueur / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 12);
		}else
		// afficher le message de fin de partie
		 {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			msg = partie.isP1Turn()? "Joueur 2 à gagné :) La partie est finie ! " : "Joueur 1 à gagné :) La partie est finie ! ";
			width = g.getFontMetrics().stringWidth(msg);
			g.setColor(new Color(201, 201, 229));
			g.fillRoundRect(Longueur / 2 - width / 2 - 5, OFFSET_Y + BOX_SIZE * 4 - 16, width + 10, 30, 10, 10);
			g.setColor(Color.RED);
			g.drawString(msg, Longueur / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
		}

		String nbW = "J1 : pions : " + partie.getBCLeft() + " Dames : " + partie.getBCKLeft();
		String nbB = "J2 : pions : " + partie.getWCLeft() + " Dames : " + partie.getWCKLeft();


		g.setFont(new Font("Arial", Font.BOLD, 13));
		g.setColor(new Color(255, 255, 255));
		g.drawString(nbW, Longueur / 2  - 260, OFFSET_Y + 8 * BOX_SIZE + 2 + 12);
		g.drawString(nbB, Longueur / 2 +80 , OFFSET_Y + 8 * BOX_SIZE + 2 + 12);

	}
	
	public Partie getPartie() {
		return partie;
	}


	public void setP1(Joueur joueur1) {
		this.J1 = joueur1;
	}



	public void setP2(Joueur joueur2) {
		this.J2 = joueur2;
	}
	
	public Joueur getCurrentPlayer() {
		return partie.isP1Turn()? J1 : J2;
	}

	public Color getCaseBlanche() {
		return CaseBlanche;
	}

	public void setCaseBlanche(Color caseBlanche) {
		this.CaseBlanche = (caseBlanche == null)? Color.WHITE : caseBlanche;
	}

	public Color getCaseNoire() {
		return CaseNoire;
	}

	public void setCaseNoire(Color caseNoire) {
		this.CaseNoire = (caseNoire == null)? Color.BLACK : caseNoire;
	}

	private void handleClick(int x, int y) {
		

		if (isGameOver) { // si la partie est finie, on ne fait rien
			return;
		}

		// Determiner si un carre est cliqué
		final int Longeur = getWidth(), Largeur = getHeight();

		final int DIM = Math.min(Longeur, Largeur), BOX_SIZE = (DIM - 2 * PADDING) / 8;

		final int OFFSET_X = (Longeur - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE; // pour avoir la pos x  exact sur la case
		final int OFFSET_Y = (Largeur - BOX_SIZE * 8) / 2;


		y = (y - OFFSET_Y) / BOX_SIZE; // pour avoir la pos y  exact sur la case
		
		Point Case = new Point(x, y);

		Partie copy = partie.copy();

		// Si le deplacement est valide, on le fait
		if (Plateau.isValidPoint(Case) && Plateau.isValidPoint(selected)) {

			boolean change = copy.isP1Turn();
			String expected = copy.getGameState();
			boolean delpacer = copy.deplacer(selected, Case);
			boolean updated = (delpacer && setGameState(true, copy.getGameState(), expected));
			change = (copy.isP1Turn() != change);
			this.selected = change? null : Case;
		
		} else {
			this.selected = Case;
		}
		
		// Verifier si al selection est valide
		this.selectionValid = isValidSelection(copy.getPlateau(), copy.isP1Turn(), selected);
		
		update();
	}
	

	private boolean isValidSelection(Plateau b, boolean isP1Turn, Point selected) {

		int i = Plateau.toIndice(selected), id = b.get(i);
		if (id == Plateau.Vide || id == Plateau.INVALID) {
			return false;
		} else if(isP1Turn ^ (id == Plateau.Pion_noir || id == Plateau.Dame_noire)) {
			return false;
		} else if (!GenerateurDeplacement.getSauts(b, i).isEmpty()) {
			return true;
		} else if (GenerateurDeplacement.getDeplacement(b, i).isEmpty()) {
			return false;
		}
		
		List<Point> points = b.find(isP1Turn? Plateau.Pion_noir : Plateau.Pion_blanc);
		points.addAll(b.find(isP1Turn? Plateau.Dame_noire : Plateau.Dame_blanche));
		for (Point p : points) {
			int pion = Plateau.toIndice(p);
			if (pion == i) {
				continue;
			}
			if (!GenerateurDeplacement.getSauts(b, pion).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private class ClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Point m = PlateauDames.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y);
			}
		}
	}
}
