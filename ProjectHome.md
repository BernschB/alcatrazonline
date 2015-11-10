# Projektbeschreibung #

Das Ziel des Projekts ist der Entwurf einer Architektur für ein Verteiltes System und die Einarbeitung in Middleware-Technologien für verteilte Systeme. Zu diesem Zweck ist ein verteiltes Spiel zu realisieren, an dem bis zu vier Spieler teilnehmen können. Das User Interface des Spiels und die Spielelogik werden zur Verfügung gestellt. Implementiert werden muss ein koordinierter Start des Spiels über eine fehlertolerante Registrierungsserver-Architektur. Weiters müssen nach dem Start des Spiels die Spielzüge der einzelnen Spieler untereinander ausgetauscht werden.

# Alcatraz - Spielbeschreibung #

Ziel des Spiels ist mit seinen vier Gefangenen von der Gefangeneninsel Alcatraz (unterer Rand des Spielfeldes) in ein Boot (oberer Rand des Spielfeldes) zu entkommen. Dazu kann jeder Spieler reihum mit einem beliebigen Gefangenen entweder horizontal oder vertikal auf ein freies Feld ziehen, solange man nicht von einem Wärter gefangen wird. Bei jedem Zug zieht auch der Wärter, der sich in der Zeile des Feldes befindet, auf das der Gefangene gezogen ist. Dabei fährt der Wärter dieselbe Anzahl an Feldern, die auch der Gefangene gefahren ist, in die Richtung des Gefangenen. Alle Gefangenen, die sich im Wege befinden, werden geschlagen und müssen erneut von unten beginnen. Wärter können immer nur horizontal in Ihrer Reihe ziehen. Ein Gefangener darf nicht auf ein Feld ziehen, an dem er sich selbst schlagen würde. Befindet sich ein Gefangener im roten dreieckigen Bereich, so darf er im nächsten Zug in das Boot steigen. Der erste Spieler, welcher alle seine Gefangenen ins Boot bekommt, hat gewonnen.

# Anforderungen #

Die Übung teilt sich in eine Server- und Client-Implementierung auf. Für den Registrierungs-Server ergeben sich folgende Aufgaben:

  * Entwerfen und implementieren Sie auf Basis der theoretischen Grundlagen der Lehrveranstaltung bzw. auch der Grundlagen aus Verteilte Systeme ein ausfallsicheres Serversystem zur Realisierung eines koordinierten Spielstarts (Registrierungsserver).
  * Das Registrieren bzw. Starten von Spielen soll möglich sein, sobald bzw. solange mindestens ein Registrierungsserver läuft.
  * Wenn sich ein Spieler für ein Spiel registriert hat, soll er sich auch wieder abmelden können, sofern das Spiel nicht bereits gestartet wurde.
  * Der Ausfall eines Servers darf nicht zum Verlust bzw. Abbruch von laufenden Spielregistrierungen führen.
  * Um die Server im laufenden Betrieb miteinander zu synchronisieren, ist die Verwendung von Spread vorgesehen.
  * Achten Sie darauf, dass Sie in Ihrer Architektur keinen Single-Point-of-Failure haben.

Der Spiele-Client ist durch folgende Anforderungen spezifiziert:

  * Der Client muss in Interaktion mit dem Server einen koordinierten Spielstart durchführen. Ein Spiel muss mit 2, 3 oder 4 Spielern spielbar sein.
  * Es muss möglich sein, dass die Spielernamen ausgetauscht werden können, damit jeder Spieler die anderen kennt. Ein Spielername muss innerhalb eines Spieles eindeutig sein.
  * Während eines Spiels sollen die Spielzüge direkt zwischen den Spiel-Clients ausgetauscht werden. Der Server darf im Spiel nicht mehr involviert sein.
  * Eine kurze Netzwerkunterbrechung (z.B. durch Ziehen des Netzwerkkabels) darf nicht zu inkorrektem Verhalten oder Absturz bzw. Deadlock des Spiels führen. Nach Wiederherstellung der Verbindung soll weitergespielt werden können.
  * Zur Kommunikation zwischen Client und Registrierungsserver bzw. zwischen den Clients untereinander ist eine RMI Technologie zu verwenden.

# Technologien #

Zur Realisierung der Übungsaufgabe kann Java RMI, CORBA, .NET Remoting oder .NET WCF verwendet werden. Als Programmiersprache ist Java bzw. C# für .NET vorgesehen.

Die fehlertoleranten Server sollen eine Gruppe bilden und damit über Spread miteinander kommunizieren.

# Aufgaben #

Sollten Sie Fragen oder Probleme beim Lösen der Aufgaben haben, sehen Sie bitte zuerst nach, ob diese nicht bereits in den Hinweisen und FAQs zum Projekt beschrieben sind.

  * [Projekt - Teil 1](ProjektTeil1.md) bis 09.03.11 ([Milestone-Release0.0](http://code.google.com/p/alcatrazonline/issues/list?q=label:Milestone-Release0.0))
  * [Projekt - Teil 2](ProjektTeil2.md) bis 30.03.11 ([Milestone-Release0.1](http://code.google.com/p/alcatrazonline/issues/list?q=label:Milestone-Release0.1))
  * [Projekt - Teil 3](ProjektTeil3.md) bis 04.05.11 ([Milestone-Release1.0](http://code.google.com/p/alcatrazonline/issues/list?q=label:Milestone-Release1.0))

# Downloads - Alcatraz #
## Java und CORBA ##

Die ZIP-Datei alcatraz-java.zip beinhaltet folgende Dateien:

  * alcatraz-lib.jar beinhaltet die komplette Spielelogik und das graphische User Interface.
  * alcatraz-doc.zip beinhaltet die Schnittstellendokumentation zum Spiel. Die API Dokumentation finden Sie auch [online](http://cis.technikum-wien.at/documents/bic/4/sam/semesterplan/project/api/index.html)
  * alcatraz-local-demo-src.zip beinhaltet den Quellcode einer lokalen Demoanwendung, die den Gebrauch der Schnittstelle illustriert.
  * alcatraz-local-demo.jar beinhaltet die ausführbare lokale Demoanwendung.
  * alcatraz-local-demo.bat dient zum Starten der Demoanwendung.


## .NET ##

Die ZIP Datei alcatraz-dotnet.zip enthält folgende Dateien:

  * Alcatraz.dll mit der Implementierung der Spielelogik und dem graphischen User Interface.
  * AlcatrazLib\_API\_doc.zip enthält die zugehörige Schnittstellendokumentation als CHM-Datei und [HTML-Seiten](http://cis.technikum-wien.at/documents/bic/4/sam/semesterplan/project/alcatraz-net-api/Index.html).
  * Test.cs ist eine lokalen Demo-Anwendung, welche die Verwendung der Schnittstelle illustriert.
  * AlcatrazLocalDemo.exe ist die ausführbare lokale Demo Anwendung.

## Spread ##

Um das Group Communication Toolkit Spread zu verwenden, benötigen Sie:

  * die compilierte Version von Spread und je nach Platform entweder die
  * Java API mit entsprechender API Dokumentation oder die
  * .NET API mit entsprechender API Dokumentation, die Sie hier auch online finden.
> > Da die .NET API der Java API sehr ähnlich ist, eignet sich als Unterstützung auch die Einführung in die Java API.

Sowohl für Java als auch für C# sind die Sourcen verfügbar (Spread Sourcen inklusive Java, C# Implementierung) und anhand von User.java bzw. User.cs kann die Verwendung der jeweiligen API nachvollzogen werden.


