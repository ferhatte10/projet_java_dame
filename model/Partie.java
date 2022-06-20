package model;

import java.awt.Point;
import java.util.List;

import logic.GenerateurDeplacement;
import logic.LogicDeplacement;


public class Partie {

	private Plateau plateau;
	
	private boolean isP1Turn;
	
	private int sautIndice;
	
	public Partie() {
		restart();
	}
	
	public Partie(String state) {
		setGameState(state);
	}
	
	public Partie(Plateau plateau, boolean isP1Turn, int sautIndice) {
		this.plateau = (plateau == null)? new Plateau() : plateau;
		this.isP1Turn = isP1Turn;
		this.sautIndice = sautIndice;
	}
	

	public Partie copy() {
		Partie g = new Partie();
		g.plateau = plateau.copy();
		g.isP1Turn = isP1Turn;
		g.sautIndice = sautIndice;
		return g;
	}
	

	public void restart() {
		this.plateau = new Plateau();
		this.isP1Turn = true;
		this.sautIndice = -1;
	}
	

	public boolean deplacer(Point start, Point end) {
		if (start == null || end == null) {
			return false;
		}
		return deplacer(Plateau.toIndice(start), Plateau.toIndice(end));
	}

	public boolean deplacer(int startIndice, int endIndice) {
		
		// Validate the move
		if (!LogicDeplacement.isValidDeplacement(this, startIndice, endIndice)) {
			return false;
		}
		
		// Make the move
		Point middle = Plateau.middle(startIndice, endIndice);
		int midIndice = Plateau.toIndice(middle);
		this.plateau.set(endIndice, plateau.get(startIndice));
		this.plateau.set(midIndice, Plateau.Vide);
		this.plateau.set(startIndice, Plateau.Vide);
		
		// Make the checker a king if necessary
		Point end = Plateau.toPoint(endIndice);
		int id = plateau.get(endIndice);
		boolean switchTurn = false;
		if (end.y == 0 && id == Plateau.Pion_blanc) {
			this.plateau.set(endIndice, Plateau.Dame_blanche);
			switchTurn = true;
		} else if (end.y == 7 && id == Plateau.Pion_noir) {
			this.plateau.set(endIndice, Plateau.Dame_noire);
			switchTurn = true;
		}
		
		// Verifier si le tour doit changer (i.e. pas de saut)
		boolean midValid = Plateau.isValidIndice(midIndice);
		if (midValid) {
			this.sautIndice = endIndice;
		}
		if (!midValid || GenerateurDeplacement.getSauts(plateau.copy(), endIndice).isEmpty()) {
			switchTurn = true;
		}
		if (switchTurn) {
			this.isP1Turn = !isP1Turn;
			this.sautIndice = -1;
		}
		
		return true;
	}
	


	public int getSautIndice() {
		return sautIndice;
	}
	public int getBCLeft(){
		List<Point> black = plateau.find(Plateau.Pion_noir);
		return black.size();
	}
	public int getBCKLeft(){
		List<Point> blackK = plateau.find(Plateau.Dame_noire);
		return blackK.size();
	}

	public int getWCLeft(){
		List<Point> white = plateau.find(Plateau.Pion_blanc);
		return white.size();
	}

	public int getWCKLeft(){
		List<Point> whiteK = plateau.find(Plateau.Dame_blanche);
		return whiteK.size();
	}
	public Plateau getPlateau() {
		return plateau.copy();
	}

	public boolean isGameOver() {

		// assurer qu'il y en a au moin un pion de chaque
		List<Point> black = plateau.find(Plateau.Pion_noir);
		black.addAll(plateau.find(Plateau.Dame_noire));
		if (black.isEmpty()) {
			return true;
		}
		List<Point> white = plateau.find(Plateau.Pion_blanc);
		white.addAll(plateau.find(Plateau.Dame_blanche));
		if (white.isEmpty()) {
			return true;
		}
		
		// Verifier si le joueur actuel peut de deplacer
		List<Point> test = isP1Turn? black :  white;
		for (Point p : test) {
			int i = Plateau.toIndice(p);
			if (!GenerateurDeplacement.getDeplacement(plateau, i).isEmpty() ||
					!GenerateurDeplacement.getSauts(plateau, i).isEmpty()) {
				return false;
			}
		}
		// Pas de deplacement
		return true;
	}

	public String getGameState() {
		
		StringBuilder state = new StringBuilder();
		for (int i = 0; i < 32; i ++) {
			state.append(plateau.get(i));
		}
		state.append(isP1Turn ? "1" : "0");
		state.append(sautIndice);
		
		return state.toString();
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
				this.plateau.set(i, id);
			} catch (NumberFormatException e) {
				continue;
			} // just skip
		}
		
		if (n > 32) {
			this.isP1Turn = (state.charAt(32) == '1');
		}
		if (n > 33) {
			try {
				this.sautIndice = Integer.parseInt(state.substring(33));
			} catch (NumberFormatException e) {
				this.sautIndice = -1;
			}
		}
	}

	public boolean isP1Turn() {
		return isP1Turn;
	}
}
