package ui;

import java.awt.*;
import java.io.Serial;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Player;

public class DameFenetre extends JFrame {

	@Serial
	private static final long serialVersionUID = 8782122389400590079L;
	
	public static final int DEFAULT_WIDTH = 620;
	
	public static final int DEFAULT_HEIGHT = 600;
	
	public static final String DEFAULT_TITLE = "Jeu de Dame en Java";
	
	private CheckerBoard board;
	
	private OptionPanel opts;

	
	public DameFenetre() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
	}
	
	public DameFenetre(Player player1, Player player2) {
		this();
		setP1(player1);
		setP2(player2);
	}
	
	public DameFenetre(int width, int height, String title) {
		
		// Setup the window
		super(title);
		super.setSize(width, height);
		super.setMinimumSize(new Dimension(width, height));
		super.setLocationByPlatform(true);
		
		// Setup the components
		JPanel layout = new JPanel(new BorderLayout());
		this.board = new CheckerBoard(this);
		this.opts = new OptionPanel(this);
		layout.add(board, BorderLayout.CENTER);
		layout.add(opts, BorderLayout.SOUTH);
		this.add(layout);

	}
	
	public CheckerBoard getBoard() {
		return board;
	}


	public void setP1(Player player1) {
		this.board.setP1(player1);
		this.board.update();
	}
	

	public void setP2(Player player2) {
		this.board.setP2(player2);
		this.board.update();
	}
	

	public void restart() {
		this.board.getPartie().restart();
		this.board.update();
	}
	
	public void setGameState(String state) {
		this.board.getPartie().setGameState(state);
	}
	

}
