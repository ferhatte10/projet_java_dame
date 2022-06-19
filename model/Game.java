package model;

import java.awt.Point;
import java.util.List;

import logic.MoveGenerator;
import logic.MoveLogic;

/**
 * La {@code Game} class représentant le jeu de dames et assure
 * les règles qu'on prédéfinit dans le rapport.
 */
public class Game {

	private Board board;
	
	private boolean isP1Turn;
	
	private int skipIndex;
	
	public Game() {
		restart();
	}
	
	public Game(String state) {
		setGameState(state);
	}
	
	public Game(Board board, boolean isP1Turn, int skipIndex) {
		this.board = (board == null)? new Board() : board;
		this.isP1Turn = isP1Turn;
		this.skipIndex = skipIndex;
	}
	

	public Game copy() {
		Game g = new Game();
		g.board = board.copy();
		g.isP1Turn = isP1Turn;
		g.skipIndex = skipIndex;
		return g;
	}
	

	public void restart() {
		this.board = new Board();
		this.isP1Turn = true;
		this.skipIndex = -1;
	}
	

	public boolean deplacer(Point start, Point end) {
		if (start == null || end == null) {
			return false;
		}
		return deplacer(Board.toIndex(start), Board.toIndex(end));
	}

	public boolean deplacer(int startIndex, int endIndex) {
		
		// Validate the move
		if (!MoveLogic.isValidMovement(this, startIndex, endIndex)) {
			return false;
		}
		
		// Make the move
		Point middle = Board.middle(startIndex, endIndex);
		int midIndex = Board.toIndex(middle);
		this.board.set(endIndex, board.get(startIndex));
		this.board.set(midIndex, Board.EMPTY);
		this.board.set(startIndex, Board.EMPTY);
		
		// Make the checker a king if necessary
		Point end = Board.toPoint(endIndex);
		int id = board.get(endIndex);
		boolean switchTurn = false;
		if (end.y == 0 && id == Board.WHITE_CHECKER) {
			this.board.set(endIndex, Board.WHITE_KING);
			switchTurn = true;
		} else if (end.y == 7 && id == Board.BLACK_CHECKER) {
			this.board.set(endIndex, Board.BLACK_KING);
			switchTurn = true;
		}
		
		// Check if the turn should switch (i.e. no more skips)
		boolean midValid = Board.isValidIndex(midIndex);
		if (midValid) {
			this.skipIndex = endIndex;
		}
		if (!midValid || MoveGenerator.getSkips(
				board.copy(), endIndex).isEmpty()) {
			switchTurn = true;
		}
		if (switchTurn) {
			this.isP1Turn = !isP1Turn;
			this.skipIndex = -1;
		}
		
		return true;
	}
	
	/**
	 * Gets a copy of the current board state.
	 * 
	 * @return a non-reference to the current game board state.
	 */
	public Board getBoard() {
		return board.copy();
	}

	public int getBCLeft(){
		List<Point> black = board.find(Board.BLACK_CHECKER);
		return black.size();
	}
	public int getBCKLeft(){
		List<Point> blackK = board.find(Board.BLACK_KING);
		return blackK.size();
	}

	public int getWCLeft(){
		List<Point> white = board.find(Board.WHITE_CHECKER);
		return white.size();
	}

	public int getWCKLeft(){
		List<Point> whiteK = board.find(Board.WHITE_KING);
		return whiteK.size();
	}

	public boolean isGameOver() {

		// assurer qu'il y en a au moin un pion de chaque
		List<Point> black = board.find(Board.BLACK_CHECKER);
		black.addAll(board.find(Board.BLACK_KING));
		if (black.isEmpty()) {
			return true;
		}
		List<Point> white = board.find(Board.WHITE_CHECKER);
		white.addAll(board.find(Board.WHITE_KING));
		if (white.isEmpty()) {
			return true;
		}
		
		// Verifier si le joueur actuel peut de deplacer
		List<Point> test = isP1Turn? black :  white;
		for (Point p : test) {
			int i = Board.toIndex(p);
			if (!MoveGenerator.getDeplacement(board, i).isEmpty() ||
					!MoveGenerator.getSkips(board, i).isEmpty()) {
				return false;
			}
		}
		
		// Pas de deplacement
		return true;
	}
	
	public boolean isP1Turn() {
		return isP1Turn;
	}
	
	public void setP1Turn(boolean isP1Turn) {
		this.isP1Turn = isP1Turn;
	}
	
	public int getSkipIndex() {
		return skipIndex;
	}
	

	public String getGameState() {
		
		String state = "";
		for (int i = 0; i < 32; i ++) {
			state += "" + board.get(i);
		}
		
		state += (isP1Turn? "1" : "0");
		state += skipIndex;
		
		return state;
	}

	public void setGameState(String state) {
		
		restart();
		
		if (state == null || state.isEmpty()) {
			return;
		}
		
		int n = state.length();
		for (int i = 0; i < 32 && i < n; i ++) {
			try {
				int id = Integer.parseInt("" + state.charAt(i));
				this.board.set(i, id);
			} catch (NumberFormatException e) {} // just skip
		}
		
		if (n > 32) {
			this.isP1Turn = (state.charAt(32) == '1');
		}
		if (n > 33) {
			try {
				this.skipIndex = Integer.parseInt(state.substring(33));
			} catch (NumberFormatException e) {
				this.skipIndex = -1;
			}
		}
	}
}
