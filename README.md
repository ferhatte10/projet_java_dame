# Jeu de Dame en Java
<img src="images/Damier.png" alt="Jeu de Dame" width="500">

## Déscription
Un partie de dames avec une interface graphique implémentée en Java Swing. Le programme prend en charge un simple joueur (un humain) Vs un autre joueur en jouant tour par tour.



## Compiler et exécuter
### Manuel
1. Dans un terminal, mettez vous dans le dossier courrant du projet
2. Compilez avec `javac -d out ui/*.java model/*.java logic/*.java`
3. Exécutez avec `java -cp ./out ui.Main`

Nb : la version de Java utilisée est la version de la plateforme linux (openjdk-18.0.1.1)


## Features
### Interface graphique
Le programme de partie de dames est livré complet avec tous les composants de l'interface utilisateur graphique qui s'adaptent à la taille de la fenêtre. Il s'agit d'une interface utilisateur conviviale qui comporte des options permettant de changer le type de joueur pour les joueurs 1 et 2, et de relancer la partie. En outre, il fournit une interface utilisateur de damier pour montrer l'état actuel du partie.

