package model;

public class JoueurHumain extends Joueur {

	@Override
	public boolean isHuman() { // ce n'est pas utile pour le moment mais peut-être utile pour le futur si on veut jouer contre l'ordinateur afin de distinguer les humains de l'ordinateur
		return true;
	}


	@Override
	public void updateGame(Partie partie) {} // ce n'est pas utile pour le moment, mais peut-être utile pour le futur si on veut jouer contre l'ordinateur afin de mettre à jour la partie en fonction des mouvements de l'ordinateur

}
