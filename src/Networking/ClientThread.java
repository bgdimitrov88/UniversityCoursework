/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

/**
 * The ClientThread class implements a thread used to execute the game's Client
 *
 * @author HUD
 */
public class ClientThread implements Runnable {

    private Thread thrd;
    private String ip;
    private int port;
    private String nickname;

    /**
     * Constructs and starts a new ClientThread
     *
     * @param ip ip of host
     * @param port port of host
     * @param nickname client's nickname
     */
    public ClientThread(String ip, int port, String nickname) {
        thrd = new Thread(this, "ClientThread");
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        thrd.start();
    }

    /**
     * Starts the thread and constructs a new client with the construct's values
     */
    public void run() {
        Client client = new Client(ip, port, nickname);
        client.Start();
    }

}
