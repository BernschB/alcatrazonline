
REM ins arbeitsverzeichnis wechseln (anpassen!!)
c:
cd c:\eclipse\workspace\alcatrazserverv4

REM starte server
start java -cp bin;spread-4.0.0.jar -Djava.rmi.server.codebase="file:/c:/eclipse/workspace/alcatrazserverv4/bin/vkm/server/server.class" -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy vkm.server.Server

REM warten auf server
ping -n 1 -w 3000 1.2.3.4>nul
pause

REM starte clients
start java -cp  bin;alcatraz-lib.jar vkm.client.Client manfred 3 20010 127.0.0.1
start java -cp  bin;alcatraz-lib.jar vkm.client.Client peter 3 20020 127.0.0.1
start java -cp  bin;alcatraz-lib.jar vkm.client.Client susi 3 20030 127.0.0.1

rem start java -cp  bin;alcatraz-lib.jar vkm.client.Client thomas 4 20040 127.0.0.1
rem start java -cp  bin;alcatraz-lib.jar vkm.client.Client karli 3 20050 127.0.0.1
