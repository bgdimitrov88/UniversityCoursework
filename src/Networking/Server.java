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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The Server class is used to connect to listen for connections to the user,
 * it implements the neccessary functions to send and receive data over the internet.
 *
 * @author HUD
 */
public class Server {

    private ServerSocket s;
    private Socket incoming;
    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private String opponentNickname;
    private String line;
    private Game game;
    private Random randomizer;
    private boolean turn;

    /**
     * Constructs a new Server listening to the designated port.
     *
     * @param port port to listen to
     * @param nickname user's nickname
     */
    public Server(int port, String nickname) {
        try {
            s = new ServerSocket(port);
            incoming = s.accept();
            inStream = incoming.getInputStream();
            outStream = incoming.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inStream));
            out = new PrintWriter(outStream,true);
            this.nickname = nickname;
            game = new Game();
            randomizer = new Random();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Server initialization error.\nCheck if port is available.");
        }
    }

     /**
     * Starts the client and begins exchanging data
     */
    public void Start(){
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
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
        else { //make move
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
        turn = ((randomizer.nextInt()%2) == 0);

        if(turn) Game.setiAmX(true);
        //send turn
        out.println(turn);
        //set turn
        Game.setMyTurn(!turn);
        //send isX
        out.println(Game.isiAmX());

        GameFrame.getChatArea().append(opponentNickname + " connected");
        GameFrame.getChatArea().append((Game.isMyTurn()) ? "\nIt's " + nickname + "'s turn." : "\nIt's " + opponentNickname + "'s turn.");
        GameFrame.getChatArea().append((Game.isiAmX()) ? "\nYou play with X" : "\nYou play with O");

    }
}
