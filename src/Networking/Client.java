/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

import GUI.GameFrame;
import Game.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The Client class is used to connect to the remote host, it implements the 
 * neccessary functions to send and receive data over the internet.
 *
 * @author HUD
 */
public class Client {

    private Socket s;
    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader in;
    private PrintWriter out;
    private String line;
    private Game game;
    private String nickname;
    private String opponentNickname;

    /**
     * Constructs a new Client and connects it to the appropriate host
     *
     * @param ip the ip of the host
     * @param port the port to which the host is listening for connection
     * @param nickname the user's nickname
     */
    Client(String ip, int port, String nickname) {
        try {
            s = new Socket(ip, port);
            inStream = s.getInputStream();
            outStream = s.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inStream));
            out = new PrintWriter(outStream,true);
            this.nickname = nickname;
            game = new Game();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not locate host.");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Client initialization error.");
        }
    }

    /**
     * Starts the client and begins exchanging data
     */
    public void Start() {
        try {

            sendInitialValues();
            

            while(true) {

                //if user has made a move
                if(Game.isSendMove())
                    sendMove();

                //if wants to send chat message
                if(Game.isSendMsg())
                    sendMessage();

                //read input
                if(in.ready()) {
                    line = in.readLine();

                    //if input is a move
                    if( !line.equals("") && line.charAt(0) == '#')
                        updateBoard();

                    //if input is a chat message
                    else
                        updateChatArea();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        private void sendMove() {

        //send it
        out.println(Game.getMove());
        Game.setSendMove(false);

        //if user won
        if(game.checkWin()) {
            GameFrame.getChatArea().append("\nYou win!");
            GameFrame.getChatArea().setCaretPosition(GameFrame.getChatArea().getDocument().getLength());
        }
    }

    private void sendMessage() {
        out.println(Game.getMessage());
        Game.setSendMsg(false);
        GameFrame.getChatArea().append("\nMe: " + Game.getMessage());
        GameFrame.getChatArea().setCaretPosition(GameFrame.getChatArea().getDocument().getLength());
    }

    private void updateBoard() {

        //if user wants to start a new game
        if(line.charAt(1) == 'N') {
            Game.clearGrid();
        }
        else { // make move
            if(Game.isiAmX()){
                Game.changeBoard(Integer.parseInt(Character.toString(line.charAt(1))), 'O');
                GameFrame.getButtons()[Integer.parseInt(Character.toString(line.charAt(1)))].setText("O");
                Game.setMyTurn(true);
            }
            else {
                Game.changeBoard(Integer.parseInt(Character.toString(line.charAt(1))), 'X');
                GameFrame.getButtons()[Integer.parseInt(Character.toString(line.charAt(1)))].setText("X");
                Game.setMyTurn(true);
            }

            if(game.checkWin()) {
                GameFrame.getChatArea().append("\nYou lose");
                GameFrame.getChatArea().setCaretPosition(GameFrame.getChatArea().getDocument().getLength());
            }
        }
    }

    private void updateChatArea() {
        GameFrame.getChatArea().append("\n" + opponentNickname + " says: " + line);
                        GameFrame.getChatArea().setCaretPosition(GameFrame.getChatArea().getDocument().getLength());

    }

    private void sendInitialValues() throws IOException {
        //send your nickname
            out.println(nickname);
            //read opponent nickname
            opponentNickname = in.readLine();
            //get turn
            Game.setMyTurn(Boolean.parseBoolean(in.readLine()));

            //read is x
            Game.setiAmX(!Boolean.parseBoolean(in.readLine()));

            //update chat
            GameFrame.getChatArea().append("Connected to " + opponentNickname);
            GameFrame.getChatArea().append((Game.isMyTurn()) ? "\nIt's " + nickname + "'s turn." : "\nIt's " + opponentNickname + "'s turn.");
            GameFrame.getChatArea().append((Game.isiAmX()) ? "\nYou play with X" : "\nYou play with O");
    }
}
