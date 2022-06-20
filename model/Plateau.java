package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Plateau {

	private int[] state;
	public static final int INVALID = -1;
	public static final int Vide = 0;
	public static final int Pion_noir = 6;
	public static final int Dame_noire = 7;
	public static final int Pion_blanc = 4;
	public static final int Dame_blanche = 5;
	

	public Plateau() {
		reset();
	}

	public Plateau copy() {
		Plateau copy = new Plateau();
		copy.state = state.clone();
		return copy;
	}
	public void reset() {
		
		this.state = new int[3];
		for (int i = 0; i < 12; i ++) {
			set(i, Pion_noir);
			set(31 - i, Pion_blanc);
		}
	}
	public List<Point> find(int id) {
		List<Point> points = new ArrayList<>();
		for (int i = 0; i < 32; i ++) {
			if (get(i) == id) {
				points.add(toPoint(i));
			}
		}
		
		return points;
	}
	public void set(int x, int y, int id) {
		set(toIndice(x, y), id);
	}

	public void set(int indice, int id) {
		
		// Out of range
		if (!isValidIndice(indice)) {
			return;
		}
		
		// Si ce n'est pas valide on mets a 0 (vide)
		if (id < 0) {
			id = Vide;
		}
		
		// 
		for (int i = 0; i < state.length; i ++) {
			boolean set = ((1 << (state.length - i - 1)) & id) != 0;
			this.state[i] = setBit(state[i], indice, set);
		}
	}
	public static Point toPoint(int indice) {
		int y = indice / 4;
		int x = 2 * (indice % 4) + (y + 1) % 2;
		return !isValidIndice(indice)? new Point(-1, -1) : new Point(x, y);
	}
	public int get(int indice) {
		if (!isValidIndice(indice)) {
			return INVALID;
		}
		return getBit(state[0], indice) * 4 + getBit(state[1], indice) * 2 + getBit(state[2], indice);
	}
	public static int toIndice(int x, int y) {

		if (!isValidPoint(new Point(x, y))) {
			return -1;
		}
		
		return y * 4 + x / 2;
	}
	public int get(int x, int y) {
		return get(toIndice(x, y));
	}
	
	public static int toIndice(Point p) {
		return (p == null)? -1 : toIndice(p.x, p.y);
	}

	
	public static int getBit(int cible, int bit) {
		
		// Out of range
		if (bit < 0 || bit > 31) {
			return 0;
		}
		
		return (cible & (1 << bit)) != 0? 1 : 0;
	}
	

	public static Point middle(Point p1, Point p2) {
		if (p1 == null || p2 == null) {
			return new Point(-1, -1);
		}
		return middle(p1.x, p1.y, p2.x, p2.y);
	}
	public static boolean isValidIndice(int testIndice) {
		return testIndice >= 0 && testIndice < 32;
	}
	public static Point middle(int i1, int i2) {
		return middle(toPoint(i1), toPoint(i2));
	}
	
	public static int setBit(int cible, int bit, boolean set) {
		
		if (bit < 0 || bit > 31) {
			return cible;
		}
		if (set) {
			cible |= (1 << bit);
		}
		else {
			cible &= (~(1 << bit));
		}
		return cible;
	}
	
	

	public static boolean isValidPoint(Point Point_test) {
		
		if (Point_test == null) {
			return false;
		}
		
		// Verifier que le point est dans le plateau
		final int x = Point_test.x;
		final int y = Point_test.y;
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return false;
		}
		
		return x % 2 != y % 2;
	}

	public static Point middle(int x1, int y1, int x2, int y2) {

		int dx = x2 - x1, dy = y2 - y1;
		if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || // Not in the board
				x1 > 7 || y1 > 7 || x2 > 7 || y2 > 7) {
			return new Point(-1, -1);
		} else if (x1 % 2 == y1 % 2 || x2 % 2 == y2 % 2) { // white tile
			return new Point(-1, -1);
		} else if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) != 2) {
			return new Point(-1, -1);
		}

		return new Point(x1 + dx / 2, y1 + dy / 2);
	}


}
