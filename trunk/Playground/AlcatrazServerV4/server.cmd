
REM ins arbeitsverzeichnis wechseln (anpassen!!)
c:
cd c:\eclipse\workspace\alcatrazserverv4

REM starte server
java -cp bin;spread-4.0.0.jar -Djava.rmi.server.codebase="file:/c:/eclipse/workspace/alcatrazserverv4/bin/vkm/server/server.class" -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy vkm.server.Server

