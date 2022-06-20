package model;

public abstract class Joueur { // cette classe est presque inutile car on qu'un seul type de joueur qui est le joueur humain (oui, c'est un peut moche) mais la classe est abstracte pour que les classes filles pudent utiliser les memes methodes si on dans le futur on ajoute un autre type de joueur tel que l'ordinateur qui va impliquer la creation d'une classe specifique pour l'ordinateur ainsi que la classe du joueur humain
	public abstract boolean isHuman(); // ce n'est pas utile pour le moment, mais peut-être utile pour le futur si on veut jouer contre l'ordinateur
	public abstract void updateGame(Partie partie); // ce n'est pas utile pour le moment, mais peut-être utile pour le futur si on veut jouer contre l'ordinateur afin de mettre à jour la partie en fonction des mouvements de l'ordinateur


}
