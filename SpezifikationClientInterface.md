# Beschreibung #

Definiert das Interface zwischen den Clients untereinander bzw. der Interaktion mit dem Spiel. Jeder Client bietet seine eigene Interfaceimplementierung an. Über doMove() können die Clients Ihre Spielzüge austauschen. Die Züge werden mit einer aufsteigenden MoveNumber als Timestamp synchronisiert:
  * Zug durchführen (doMove())
  * evtl. Möglichkeit die Aktivität abzufragen (poll()) ???

# UML #
![http://alcatrazonline.googlecode.com/files/IClient.png](http://alcatrazonline.googlecode.com/files/IClient.png)

# Interface Definition #
```
interface IClient {
  doMove(int moveNr) throws MoveException;
}
```

# offene Fragen #
  * Wie werden die Interfaces angeboten? Verteiltes RMI Objekt auf jedem Client? Jeder Client hat seine eigene RMIRegistry?
  * zentrale RMI Registry? Ausfallsicherheit?