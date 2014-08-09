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
 * The ClientThread class implements a thread used to execute the game's Client
 *
 * @author HUD
 */
public class ClientThread implements Runnable {

    private String ip;
    private Socket s;
    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader in;
    private PrintWriter out;
    private String line;
    private Game game;
    private String nickname;
    private String opponentNickname;
    private GameFrame gameFrame;

    /**
     * Constructs and starts a new ClientThread
     *
     * @param ip ip of host
     * @param nickname client's nickname
     * @param gameFrame a reference to the game frame
     * @param game a reference to the game object
     */
    public ClientThread(String ip, String nickname, GameFrame gameFrame, Game game) {
        this.ip = ip;
        this.nickname = nickname;
        this.gameFrame = gameFrame;
        this.game = game;
    }

    /**
     * Starts the thread and constructs a new client with the construct's values
     */
    public void run() {
        try {
            s = new Socket(ip, ServerThread.GAME_PORT);
            inStream = s.getInputStream();
            outStream = s.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inStream));
            out = new PrintWriter(outStream,true);
            
            sendInitialValues();            

            while(true) {

                //if user has made a move
                if(game.isSendMove())
                    sendMove();

                //if wants to send chat message
                if(game.isSendMsg())
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
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not locate host.");
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Client error.");
        }
    }

    private void sendMove() {
        //send it
        out.println(game.getMove());
        game.setSendMove(false);

        //if user won
        if(game.checkWin()) {
            gameFrame.updateChatArea("\nYou win!");
        }
    }

    private void sendMessage() {
        out.println(game.getMessage());
        game.setSendMsg(false);
        gameFrame.updateChatArea("\nMe: " + game.getMessage());
    }

    private void updateBoard() {

        //if user wants to start a new game
        if(line.charAt(1) == 'N') {
            gameFrame.clearGrid();
        }
        else { // make move
            if(game.isiAmX()){
                game.changeBoard(Integer.parseInt(Character.toString(line.charAt(1))), 'O');
                gameFrame.updateButtons(Integer.parseInt(Character.toString(line.charAt(1))), "O");
                game.setMyTurn(true);
            }
            else {
                game.changeBoard(Integer.parseInt(Character.toString(line.charAt(1))), 'X');
                gameFrame.updateButtons(Integer.parseInt(Character.toString(line.charAt(1))), "X");
                game.setMyTurn(true);
            }

            if(game.checkWin()) {
                gameFrame.updateChatArea("\nYou lose");
            }
        }
    }

    private void updateChatArea() {
        gameFrame.updateChatArea("\n" + opponentNickname + " says: " + line);
    }

    private void sendInitialValues() throws IOException {
        //send your nickname
            out.println(nickname);
            //read opponent nickname
            opponentNickname = in.readLine();
            //get turn
            game.setMyTurn(Boolean.parseBoolean(in.readLine()));

            //read is x
            game.setiAmX(!Boolean.parseBoolean(in.readLine()));

            //update chat
            gameFrame.updateChatArea("Connected to " + opponentNickname);
            gameFrame.updateChatArea((game.isMyTurn()) ? "\nIt's " + nickname + "'s turn." : "\nIt's " + opponentNickname + "'s turn.");
            gameFrame.updateChatArea((game.isiAmX()) ? "\nYou play with X" : "\nYou play with O");
    }
}
