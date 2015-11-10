# Beschreibung #

Definiert das Interface des Registrierungsservers. Der Registrierungsserver leistet:
  * Auflisten aller derzeit am Server aktiven Spielregistrierungen (listGames())
  * Erstellen eines neuen _Player_ Objekts mit dem Ziel Systemweit eindeutige Player ID's gewährleisten zu können (createPlayer())
  * Erstellen einer neuen Spielregistrierung (createGame())
  * Möglichkeiten für andere _Player_ an einem Spiel teilzunehmen (joinGame()) bzw. sich evtl. auch wieder von einem Spiel abzumelden (leaveGame())
  * Möglichkeit ein Spiel zu starten (startGame())

# UML #
![http://alcatrazonline.googlecode.com/files/IRegistryServer.png](http://alcatrazonline.googlecode.com/files/IRegistryServer.png)


# Interface Definition #
```
interface IRegistryServer {
  List<Game> listGames();
  Player createPlayer(string Name);
  Game createGame(Player player);
  void joinGame(Game game) throws CannotJoinGameException;
  void leaveGame(Game game) throws NotParticipatingException;
  void startGame(Game game) throws StartingGameException;
}
```

# offene Fragen #
  * Ist ein (verteiltes) Objekt _Game_ notwendig, oder reicht es wenn der Registrierungsserver die Games intern (d.h. nicht sichtbar für die Player) verwaltet?
  * Weiß der Server bei RMI überhaupt WER (also welcher Player) eine Methode aufgerufen hat? Hintergrund: Müssen bei joinGame() und leaveGame() Player Objekte mitübergeben werden? Muss bei startGame() eine Game oder eine Player Referenz mitübergeben werden?
  * ...???...