/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.technikum.sam.remote.alcatraz.client;

import at.falb.games.alcatraz.api.Alcatraz;
import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import at.technikum.sam.remote.alcatraz.commons.IClient;
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian
 */
public class GameClient implements GameStartedListener {

  public final static String HOST = "10.201.88.58";
  public final static int PORT = 1099;
  
  private static ClientImplementation myClient = null;
  private static PlayerAdapter myPlayer = null;
  private static Alcatraz theGame = null;

  public static void main(String[] args) {

      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String playerName = "";
      IClient clientStub = null;


      myClient = new ClientImplementation();

      System.out.println("Spielernamen eintragen:");
        try {
            playerName = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
      
        try {
            clientStub = (IClient) UnicastRemoteObject.exportObject(myClient, 0);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

      if(clientStub == null) {
          Util.printDebug("ClientStub is null");
          System.exit(-1);
      }
        try {
            clientStub.isAlive();
        } catch (RemoteException ex) {
           ex.printStackTrace();
        }

      myPlayer = new PlayerAdapter(playerName, myClient);

       try {
          myClient.init(HOST, PORT, myPlayer);
          myClient.installListener(new GameClient());

      } catch (Exception ex) {
          //TODO: ExceptionHandling
          ex.printStackTrace();
      }


      try {
          myClient.getMasterServer().register(myPlayer);
      } catch (NameAlreadyInUseException ex) {
          ex.printStackTrace();
          System.exit(-1);
      } catch (RemoteException ex) {
          ex.printStackTrace();
          System.exit(-1);
      } catch (Exception ex) {
          ex.printStackTrace();
          System.exit(-1);
      }

      Util.printDebug(
                "Player ".
                concat(myPlayer.getName()).
                concat(" is registered with server")
                );

      theLoop();

      if (theGame != null) {
          theGame.showWindow();
      } else {
          System.exit(-1);

      }

  }

  private static void theLoop() {

      byte buffer[] = new byte[80];
      do {
            try {
                System.in.read(buffer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            switch(buffer[0]) {
                case 'f': //force start
                    try {
                        myClient.getMasterServer().forceStart(myPlayer);
                    } catch (GameStartException ex) {
                        ex.printStackTrace();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        try {
                          myClient.getMasterServer().unregister(myPlayer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.exit(-1);
                    }
                    break;
                case 'u': //unregister and quit
                    try {
                        myClient.getMasterServer().unregister(myPlayer);
                    } catch (GameRegistryException ex) {
                        ex.printStackTrace();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        System.exit(-1);
                    }
                    System.exit(0);
                    break;
                case 's': //workaround to show game in main()
                    if (theGame != null) {
                        return;
                    }
                    break;
                case 'q': //quit
                    System.exit(0);
                    break;
                default:
                    System.out.println("Eingabe nicht erkannt:");
                    System.out.println("f: force-Start u: unregister and quit q: quit");
                    break;
            }
      } while (buffer[0] != 'q');

      return;

  }

    public void gameStarted(Alcatraz game) {
        
        theGame = game;
    }



}
