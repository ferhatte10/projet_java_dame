package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.List;

import javax.swing.JButton;

import logic.MoveGenerator;
import model.Board;
import model.Game;
import model.HumanPlayer;
import model.Player;



public class CheckerBoard extends JButton{

	@Serial
	private static final long serialVersionUID = -6014690893709316364L;

	

	private static final int PADDING = 16;

	private Game game;
	
	private DameFenetre window;
	
	private Player player1;
	
	private Player player2;
	
	private Point selected;
	

	private boolean selectionValid;


	private int nbWhite;

	private int nbWhiteK;

	private int nbBlack;

	private int nbBlackK;

	private Color lightTile;

	private Color darkTile;
	
	private boolean isGameOver;

	
	public CheckerBoard(DameFenetre window) {
		this(window, new Game(), null, null);
	}
	
	public CheckerBoard(DameFenetre window, Game game, Player player1, Player player2) {
		
		// mettre en place les composants
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		
		// mis en place du jeu
		this.game = (game == null)? new Game() : game;
		this.lightTile = Color.WHITE;
		this.darkTile = Color.BLACK;
		this.window = window;
		setP1(player1);
		setP2(player2);
	}
	public void update() {
		this.nbBlack  	= game.getBCLeft();
		this.nbBlackK 	= game.getBCKLeft();
		this.nbWhite  	= game.getWCLeft();
		this.nbWhiteK 	= game.getWCKLeft();
		this.isGameOver = game.isGameOver();
		repaint();
	}

	public synchronized boolean setGameState(boolean testValue,
			String newState, String expected) {
		
		// Test the value if requested
		if (testValue && !game.getGameState().equals(expected)) {
			return false;
		}
		
		// Update the game state
		this.game.setGameState(newState);
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
		Game game = this.game.copy();
		
		// Perform calculations
		final int BOX_PADDING = 4;
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H?  W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);
		
		// dessiner le plateau
		g.setColor(Color.BLACK);
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
		g.setColor(lightTile);
		g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		g.setColor(darkTile);
		for (int y = 0; y < 8; y ++) {
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
						BOX_SIZE, BOX_SIZE);
			}
		}
		
		// si il a le droit de selectionner la case ca sera vert sinon rouge
		if (Board.isValidPoint(selected)) {
			g.setColor(selectionValid? Color.GREEN : Color.RED);
			g.fillRect(OFFSET_X + selected.x * BOX_SIZE,
					OFFSET_Y + selected.y * BOX_SIZE,
					BOX_SIZE, BOX_SIZE);
		}
		
		// dessiner les pions
		Board b = game.getBoard();
		for (int y = 0; y < 8; y ++) {
			int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				int id = b.get(x, y);
				

				if (id == Board.EMPTY) {
					continue;
				}
				
				int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;
				
				// pions noir
				if (id == Board.BLACK_CHECKER) {


					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// dame noir
				else if (id == Board.BLACK_KING) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// pions blancs
				else if (id == Board.WHITE_CHECKER) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// dame blanche
				else if (id == Board.WHITE_KING) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// Rajouter des effet pour n'importe quelle dame
				if (id == Board.BLACK_KING || id == Board.WHITE_KING) {
					g.setColor(new Color(255, 209, 55));
					g.drawOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
					g.drawOval(cx + 1, cy, CHECKER_SIZE - 4, CHECKER_SIZE - 4);
				}
			}
		}
		
		// Afficher le message qui indique a qui est le tour
		String msg = game.isP1Turn()? "Joueur 1 qui joue" : "Joueur 2 qui joue";
		int width = g.getFontMetrics().stringWidth(msg);
		Color back = game.isP1Turn()? Color.WHITE : Color.BLACK;
		Color front = game.isP1Turn()? Color.BLACK : Color.WHITE;

		if (!isGameOver){
			g.setColor(back);
			g.fillRect(W / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2, width + 11, 15);
			g.setColor(front);
			g.drawString(msg, W / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 12);
		}else
		// afficher le message de fin de partie
		 {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			msg = game.isP1Turn()? "Joueur 2 à gagné :) La partie est finie ! " : "Joueur 1 à gagné :) La partie est finie ! ";
			width = g.getFontMetrics().stringWidth(msg);
			g.setColor(new Color(201, 201, 229));
			g.fillRoundRect(W / 2 - width / 2 - 5, OFFSET_Y + BOX_SIZE * 4 - 16, width + 10, 30, 10, 10);
			g.setColor(Color.RED);
			g.drawString(msg, W / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
		}

		String nbW = "J1 : pions : " + game.getBCLeft() + " Dames : " +game.getBCKLeft();
		String nbB = "J2 : pions : " + game.getWCLeft() + " Dames : " +game.getWCKLeft();


		g.setFont(new Font("Arial", Font.BOLD, 13));
		g.setColor(new Color(255, 255, 255));
		g.drawString(nbW, W / 2  - 260, OFFSET_Y + 8 * BOX_SIZE + 2 + 12);
		g.drawString(nbB, W / 2 +80 , OFFSET_Y + 8 * BOX_SIZE + 2 + 12);

	}
	
	public Game getPartie() {
		return game;
	}

	public void setPartie(Game game) {
		this.game = (game == null)? new Game() : game;
	}

	public DameFenetre getfenetre() {
		return window;
	}

	public void setfenetre(DameFenetre window) {
		this.window = window;
	}

	public Player getP1() {
		return player1;
	}

	public void setP1(Player player1) {
		this.player1 = (player1 == null)? new HumanPlayer() : player1;
		if (game.isP1Turn() && !this.player1.isHuman()) {
			this.selected = null;
		}
	}

	public Player getP2() {
		return player2;
	}

	public void setP2(Player player2) {
		this.player2 = (player2 == null)? new HumanPlayer() : player2;
		if (!game.isP1Turn() && !this.player2.isHuman()) {
			this.selected = null;
		}
	}
	
	public Player getCurrentPlayer() {
		return game.isP1Turn()? player1 : player2;
	}

	public Color getLightTile() {
		return lightTile;
	}

	public void setLightTile(Color lightTile) {
		this.lightTile = (lightTile == null)? Color.WHITE : lightTile;
	}

	public Color getDarkTile() {
		return darkTile;
	}

	public void setDarkTile(Color darkTile) {
		this.darkTile = (darkTile == null)? Color.BLACK : darkTile;
	}

	private void handleClick(int x, int y) {
		
		//
		if (isGameOver || !getCurrentPlayer().isHuman()) {
			return;
		}
		
		Game copy = game.copy();
		
		// Determine what square (if any) was selected
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE; // pour avoir la pos exact sur la case
		y = (y - OFFSET_Y) / BOX_SIZE;
		Point sel = new Point(x, y);
		
		// Determine if a move should be attempted
		if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
			boolean change = copy.isP1Turn();
			String expected = copy.getGameState();
			boolean move = copy.deplacer(selected, sel);
			boolean updated = (move? setGameState(true, copy.getGameState(), expected) : false);

			change = (copy.isP1Turn() != change);
			this.selected = change? null : sel;
		} else {
			this.selected = sel;
		}
		
		// Check if the selection is valid
		this.selectionValid = isValidSelection(
				copy.getBoard(), copy.isP1Turn(), selected);
		
		update();
	}
	

	private boolean isValidSelection(Board b, boolean isP1Turn, Point selected) {

		// Trivial cases
		int i = Board.toIndex(selected), id = b.get(i);
		if (id == Board.EMPTY || id == Board.INVALID) { // no checker here
			return false;
		} else if(isP1Turn ^ (id == Board.BLACK_CHECKER ||
				id == Board.BLACK_KING)) { // wrong checker
			return false;
		} else if (!MoveGenerator.getSkips(b, i).isEmpty()) { // skip available
			return true;
		} else if (MoveGenerator.getDeplacement(b, i).isEmpty()) { // no moves
			return false;
		}
		
		// Determine if there is a skip available for another checker
		List<Point> points = b.find(
				isP1Turn? Board.BLACK_CHECKER : Board.WHITE_CHECKER);
		points.addAll(b.find(
				isP1Turn? Board.BLACK_KING : Board.WHITE_KING));
		for (Point p : points) {
			int checker = Board.toIndex(p);
			if (checker == i) {
				continue;
			}
			if (!MoveGenerator.getSkips(b, checker).isEmpty()) {
				return false;
			}
		}

		return true;
	}


	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Get the new mouse coordinates and handle the click
			Point m = CheckerBoard.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y);
			}
		}
	}
}
