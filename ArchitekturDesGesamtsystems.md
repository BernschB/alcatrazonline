# Designannahmen #
  1. Der Registrierungsserver sammelt CLientregistrierungen bis er maximal 4 beisammen hat, und startet mit diesen 4en ein Spiel
  1. Bereits ab 2 Spieler kann ein Spieler einen Spielstart forcieren
  1. Es existiert ein Masterserver und beliebig viele Slave Server. Die Server synchronisieren sich über Spread Messages
  1. Die Spreadsynchronisation ist gleichzeit das Mittel für die Slaves herauszufinden, ob der Master noch am Leben ist
  1. Antwortet derMaster nicht mehr, wird unter den Slaves eine Bully Election durchgeführt. Der Server mit der niedrigsten Server ID wird der neue Master
  1. Neu hinzugefügte Server werden automatisch Slave, außer sie können sich nicht mit einem Master aufsynchronisieren --> Election!
  1. Der Server erstellt PlayerAdapter Objekte und vergibt für die Player spielweit einheitlicher Player ID´s sowie eindeutige Playernamen

# Ablauf (grob) #
  1. Ein Client lässt sich von einem Server ein neues systemweit einheitliches Spielerobjekt erzeugen
  1. Mit diesem Spielerobjekt kann er sich auf einem Server nun zum Spielen registrieren (er gelangt dann in eine Warteschleife bis maximal 4 Spieler zusammenkommen, oder ein Mitspieler den Spielstart forciert)
  1. Hat die Partie die geforderte Spieleranzahl (4) erreicht wird das Spiel gestartet. Dabei trennen die Clients ihre Verbindung zum Server und bauen untereinander eine Peer2Peer Verbindung auf
  1. (Der Server teilt dem 1ten in der Runde noch mit, dass er nun mit seinem Zug dran ist) - nötig?
  1. Jeder Client der am Zug ist, sendet seinen jeweiligen Zug multicast an jeden Mitspieler und wartet ab, ob der Zug vom jeweiligen CLient auch tatsächlich empfangen wurde
  1. (antwortet ein Client nicht, wird an die anderen Mitspieler ausgesendet, dass ein Mitspieler nicht antwortet)
  1. (Bei Erfolg) Anschließend beendet er seine "Transaktion" in dem er dem nächsten in der Runde mitteilt, dass er dran ist
  1. Der nächste beginnt dann wieder wie oben.

# Ideen, Rahmenbedingungen und verwendbare Technologien #
  * Spread als Group-Communication Framework (ist gefordert)
  * Java RMI bietet mit der rmiregistry einen Naming Dienst. Ein Name-Lookup ist allerdings Host gebunden (d.h. man muss den Hostnamen auf dem rmiregistry läuft kennen --> Single-Point-of-Failure?)
  * RMI and JNDI binden bringt eventuell mehr Location Transparency? - http://download.oracle.com/javase/1.5.0/docs/guide/jndi/jndi-rmi.html
  * Clustering mit JBoss als Middleware - http://docs.jboss.org/jbossas/jboss4guide/r4/html/cluster.chapt.html
  * The Three Rings Project - [Narya](http://code.google.com/p/narya/), [Vilya](http://code.google.com/p/vilya/)