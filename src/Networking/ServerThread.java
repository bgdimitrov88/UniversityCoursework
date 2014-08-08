/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

/**
 * The ServerThread class implements a thread used to execute the game's Server
 *
 * @author HUD
 */
public class ServerThread implements Runnable {

    private Thread thrd;
    private int port;
    private Server server;
    private String nickname;

    /**
     * Constructs and starts a new ClientThread
     *
     * @param port port to listen to
     * @param nickname user's nickname
     */
    public ServerThread(int port, String nickname) {
        thrd = new Thread(this, "ServerThread");
        this.port = port;
        this.nickname = nickname;
        thrd.start();
    }

    /**
     * Starts the thread and constructs a new server with the construct's values
     */
    public void run() {
        server = new Server(port, nickname);
        server.Start();
    }

}
