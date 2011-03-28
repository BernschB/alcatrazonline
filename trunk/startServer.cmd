@echo off
@title Starting registry server...

java -cp ./dist/AlcatrazOnline.jar;./lib/spread.jar;./lib/alcatraz-lib.jar at.technikum.sam.remote.alcatraz.server.RegistryServer

pause
