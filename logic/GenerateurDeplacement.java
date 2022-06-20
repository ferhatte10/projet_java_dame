package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.Plateau;

public class GenerateurDeplacement {

	public static List<Point> getSauts(Plateau plateau, int startIndice) {

		List<Point> endPoints = new ArrayList<>();
		if (plateau == null || !Plateau.isValidIndice(startIndice)) {
			return endPoints;
		}

		// les points possibles pour le saut
		int id = plateau.get(startIndice);
		Point p = Plateau.toPoint(startIndice);
		addPoints(endPoints, p, id, 2);

		// supprimer les points invalides
		for (int i = 0; i < endPoints.size(); i ++) {

			// verifier si le saut est valide
			Point end = endPoints.get(i);
			if (!isValidSaut(plateau, startIndice, Plateau.toIndice(end))) {
				endPoints.remove(i --);
			}
		}

		return endPoints;
	}
	public static List<Point> getDeplacement(Plateau plateau, int startIndice) {
		
		List<Point> endPoints = new ArrayList<>();
		if (plateau == null || !Plateau.isValidIndice(startIndice)) {
			return endPoints;
		}

		// les points possibles pour le déplacement
		int id = plateau.get(startIndice);
		Point p = Plateau.toPoint(startIndice);
		addPoints(endPoints, p, id, 1);
		
		// supprimer les points invalides
		for (int i = 0; i < endPoints.size(); i ++) {
			Point end = endPoints.get(i);
			if (plateau.get(end.x, end.y) != Plateau.Vide) {
				endPoints.remove(i --);
			}
		}
		
		return endPoints;
	}
	public static void addPoints(List<Point> points, Point p, int id, int d) {

		// Add points moving down
		boolean isKing = (id == Plateau.Dame_noire || id == Plateau.Dame_blanche);
		if (isKing || id == Plateau.Pion_noir) {
			points.add(new Point(p.x + d, p.y + d));
			points.add(new Point(p.x - d, p.y + d));
		}

		// Add points moving up
		if (isKing || id == Plateau.Pion_blanc) {
			points.add(new Point(p.x + d, p.y - d));
			points.add(new Point(p.x - d, p.y - d));
		}
	}
	public static boolean isValidSaut(Plateau plateau,int startIndice, int endIndice) {
		
		if (plateau == null) {
			return false;
		}

		if (plateau.get(endIndice) != Plateau.Vide) {
			return false;
		}
		
		// Verifier si le milieu est l'adversaire
		int id = plateau.get(startIndice);
		int midID = plateau.get(Plateau.toIndice(Plateau.middle(startIndice, endIndice)));
		if (id == Plateau.INVALID || id == Plateau.Vide) {
			return false;
		} else if (midID == Plateau.INVALID || midID == Plateau.Vide) {
			return false;
		} else return (midID == Plateau.Pion_noir || midID == Plateau.Dame_noire) == (id == Plateau.Pion_blanc || id == Plateau.Dame_blanche);
	}

}
