
package logic;

import java.awt.Point;
import java.util.List;

import model.Plateau;
import model.Partie;


public class LogicDeplacement {


	public static boolean isValidDeplacement(Plateau plateau, boolean isP1Turn, int startIndice, int endIndice, int sautIndice) {
		
		// verifications basiques
		if (plateau == null || !Plateau.isValidIndice(startIndice) ||
				!Plateau.isValidIndice(endIndice)) {
			return false;
		} else if (startIndice == endIndice) {
			return false;
		} else if (Plateau.isValidIndice(sautIndice) && sautIndice != startIndice) {
			return false;
		}
		
		// executer les tests pour verifier si le deplacement est valide
		if (!validerIDs(plateau, isP1Turn, startIndice, endIndice)) {
			return false;
		} else return validerDistance(plateau, isP1Turn, startIndice, endIndice);
		
		// valider tous les tests
	}

	private static boolean validerDistance(Plateau plateau, boolean isP1Turn, int startIndice, int endIndice) {
		
		// Check that it was a diagonal move
		Point start = Plateau.toPoint(startIndice);
		Point end = Plateau.toPoint(endIndice);
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}
		
		// Check that it was in the right direction
		int id = plateau.get(startIndice);
		if ((id == Plateau.Pion_blanc && dy > 0) ||
				(id == Plateau.Pion_noir && dy < 0)) {
			return false;
		}
		
		// Verifier que ce n'est pas un saut et il n'y a pas de saut disponible
		Point middle = Plateau.middle(startIndice, endIndice);
		int midID = plateau.get(Plateau.toIndice(middle));
		if (midID < 0) {
			

			List<Point> pions;
			if (isP1Turn) {
				pions = plateau.find(Plateau.Pion_noir);
				pions.addAll(plateau.find(Plateau.Dame_noire));
			} else {
				pions = plateau.find(Plateau.Pion_blanc);
				pions.addAll(plateau.find(Plateau.Dame_blanche));
			}
			

			// verifier si il y a un saut disponible
			for (Point p : pions) {
				int indice = Plateau.toIndice(p);
				if (!GenerateurDeplacement.getSauts(plateau, indice).isEmpty()) {
					return false;
				}
			}
		}
		
		// Passed all tests
		return true;
	}
	public static boolean isValidDeplacement(Partie partie, int startIndice, int endIndice) {
		return partie != null && isValidDeplacement(partie.getPlateau(),partie.isP1Turn(), startIndice, endIndice, partie.getSautIndice());
	}
	private static boolean validerIDs(Plateau plateau, boolean isP1Turn, int startIndice, int endIndice) {

		if (plateau.get(endIndice) != Plateau.Vide) {
			return false;
		}

		int id = plateau.get(startIndice);
		if ((isP1Turn && id != Plateau.Pion_noir && id != Plateau.Dame_noire) || (!isP1Turn && id != Plateau.Pion_blanc && id != Plateau.Dame_blanche)) {
			return false;
		}

		// Verifier le milieu du deplacement
		Point middle = Plateau.middle(startIndice, endIndice);
		int midID = plateau.get(Plateau.toIndice(middle));
		return midID == Plateau.INVALID || ((isP1Turn || midID == Plateau.Pion_noir || midID == Plateau.Dame_noire) && (!isP1Turn || midID == Plateau.Pion_blanc || midID == Plateau.Dame_blanche));

		// valider tous les tests
	}

}
