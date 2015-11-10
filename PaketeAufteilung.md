# Aufteilung in Pakete (ToDo's) #

Als Client-Server Anwendung können einmal drei grundsätzliche Pakete festgemacht werden:
  * **Client** - Code der nur von den Clients verwendet wird
  * **Server** - Code der nur von den Server verwendet wird
  * **Commons** - Code der von beiden verwendet wird.

Die drei Hauptpakete befinden sich im Package:
**at.technikum.sam.remote.alcatraz**

## Details ##

Um die Arbeit aufzuteilen werden die Paket weiter zerlegt in folgende Pakte und Klassen:

  * **Commons**
    * _#DONE#_ Projekt Setup
    * _#DONE#_ Interfaces
      * _#Sebastian#_ mergen von createPlayer und register im IRegistryServer eventuelle Änderungen am PlayerAdapter
    * _#DONE#_ PlayerAdapter Klasse
    * Utility Klasse Grundgerüst (evtl. Logging für Debugging Zwecke)
    * Ressources, Strings, Statics


  * **Client**
    * ClientImpl Grundgerüst (RMI Managment, Startup, Shutdown, Main)
    * _#DONE#_Client Methode reportNewMaster(...)
    * _#DONE#_Client Methode isAlive()
    * _#Sebastian#_Client Methode startGame(...)
    * _#DEPRECATED#_Client Methode youtTurn()
    * _#Sebastian#_Client Methode doMove(...)
    * _#Sebastian#_Client Methoden MoveListener
    * _#Stefan#_Integration Client-Server Registrierungsablauf (abhängig von ServerImpl) inkl. GUI für die Usereingabe (Namenseingabe und eventueller Force-Start, verbergen der GUI nach Spielstart,...)


  * **Server**
    * _#Jürgen#_ ServerImpl Grundgerüst (RMI, Spread, Startup, etc...)
    * _#DONE#_ RegistryState Klasse (Game Klasse am Server)
      * _#DONE#_ Game.toString()
    * Spread Group Opening?
    * _#DONE#_ Server Methode createPlayer(...)
    * _#DONE#_ Server Methode register(...) & unregister(...)
    * Spielstart
      * _#DONE#_ Server Methode forceStart()
      * _#DONE#_ 4 Spieler Autostart
      * _#DONE#_ generische private Startmethode
    * _#Christian#_ IAdvancedListener Implementierung
      * Server Synchronisation
      * Master Failure Detection & ELection
      * Join/Leave Group
    * _#DONE#_ Spread Messages versenden (synchronize Game)
    * _#DONE#_ Spielerregistrierung umbauen
      * _#DONE#_ createPlayer() und register() zu neuer Methode register() zusammenfassen (createPlayer() fliegt raus)
      * _#DONE#_ neue register Signatur: public void register(PlayerAdapter player)
      * _#DONE#_ PlayerAdapter enthält nur mehr IClient Referenz und Playernamen als String, kein Player Objekt mehr
      * _#DONE#_ register macht:
        * _#DONE#_ prüfung ob Playernamen bereits vergeben?
        * _#DONE#_ prüfung ob IClient bereits in der Liste (nur unter anderem Namen)?
        * _#DONE#_ PlayerAdapter zur Liste hinzufügen
        * _#DONE#_ Abschluss mit synchronizeGame() um Game an Spread Gruppe zu verschicken





