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
 * The ServerThread class implements a thread used to execute the game's Server
 *
 * @author HUD
 */
public class ServerThread implements Runnable {    
    public static final int GAME_PORT = 22222;
    
    private final String nickname;
    private ServerSocket s;
    private Socket incoming;
    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader in;
    private PrintWriter out;
    private String opponentNickname;
    private String line;
    private final Game game;
    private final Random randomizer;
    private boolean turn;
    private final GameFrame gameFrame;

    /**
     * Constructs and starts a new ClientThread
     *
     * @param nickname user's nickname
     * @param gameFrame a reference to the game frame
     * @param game a reference to the game object
     */
    public ServerThread(String nickname, GameFrame gameFrame, Game game) {
        this.nickname = nickname;
        this.gameFrame = gameFrame;
        this.game = game;
        randomizer = new Random();
    }

    /**
     * Starts the thread and constructs a new server with the construct's values
     */
    public void run() {
        try {
            s = new ServerSocket(GAME_PORT);
            incoming = s.accept();
            inStream = incoming.getInputStream();
            outStream = incoming.getOutputStream();
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
                    if( !line.equals("") && line.charAt(0) == '#') {
                        updateBoard();
                    }

                    //if input is a chat message
                    else { 
                        gameFrame.updateChatArea("\n" + opponentNickname + " says: " + line);
                    }
                }

            }
            
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Server runtime error reading input or output.");
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
            game.clearGrid();
        }
        else { //make move
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

    private void sendInitialValues() throws IOException {
        //send your nickname
        out.println(nickname);

        //read opponent nickname
        opponentNickname = in.readLine();

        //get turn
        turn = ((randomizer.nextInt()%2) == 0);

        if(turn) game.setiAmX(true);
        //send turn
        out.println(turn);
        //set turn
        game.setMyTurn(!turn);
        //send isX
        out.println(game.isiAmX());

        gameFrame.updateChatArea(opponentNickname + " connected");
        gameFrame.updateChatArea((game.isMyTurn()) ? "\nIt's " + nickname + "'s turn." : "\nIt's " + opponentNickname + "'s turn.");
        gameFrame.updateChatArea((game.isiAmX()) ? "\nYou play with X" : "\nYou play with O");

    }

}
