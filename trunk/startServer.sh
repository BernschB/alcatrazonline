#!/bin/sh
#spread -c ./spread.conf &
java -cp ./dist/AlcatrazOnline.jar:./lib/spread.jar:./lib/alcatraz-lib.jar at.technikum.sam.remote.alcatraz.server.RegistryServer

