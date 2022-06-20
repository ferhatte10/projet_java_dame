
package model;

public abstract class Joueur {
	public abstract boolean isHuman(); // ce n'est pas utile pour le moment mais peut-être utile pour le futur si on veut jouer contre l'ordinateur
	public abstract void updateGame(Partie partie);

}
