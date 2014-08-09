package GUI;

import Game.Game;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 *The GameFrame class represents the main window of the game. It varios 
 * Component objects to represent the structure of the game's GUI.
 *
 * @author HUD
 * @version 1.0
 */
public class GameFrame implements ActionListener{

    private final JFrame gameWindow;
    private final JPanel gamePanel;
    private final JMenuBar menuBar;
    private final JMenu gameMenu;
    private final JMenuItem hostMenuItem;
    private final JMenuItem connectMenuItem;
    private final JMenuItem exitMenuItem;
    private final JTextArea chatArea;
    private final JScrollPane chatPane;
    private final JButton[] buttons;
    private JTextField chatInputField;
    private final JButton sendButton;
    private final JButton newGameButton;
    
    private final ConnectToFrame connectToFrame;
    private final HostFrame hostFrame;

    /**
     * Constructs a new GameFrame object and initializes it's components.
     */
    public GameFrame() {

        //initialize components
        gameWindow = new JFrame("TicTacToe");
        gameWindow.setSize(400,440);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setVisible(true);
        gameWindow.setResizable(false);
        gameWindow.setLayout(null);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3,3));
        gamePanel.setBounds(0, 0, 250, 250);
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.black,5));

        buttons = new JButton[9];
        for(int i = 0; i< 9; i++) {
                buttons[i] = new JButton();
                buttons[i].addActionListener(this);
                gamePanel.add(buttons[i]);
        }
        
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        hostMenuItem = new JMenuItem("Host");
        hostMenuItem.addActionListener(this);
        connectMenuItem = new JMenuItem("Connect To");
        connectMenuItem.addActionListener(this);
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);

        gameMenu.add(hostMenuItem);
        gameMenu.add(connectMenuItem);
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);
        
        chatArea = new JTextArea();
        chatArea.setBounds(0,255, 385,100);
        chatArea.setBorder(BorderFactory.createLineBorder(Color.black,3));

        chatPane = new JScrollPane(chatArea);
        chatPane.setBounds(0,255, 385,100);
        chatPane.setBorder(BorderFactory.createLineBorder(Color.black,2));
        chatPane.setUI(new BasicScrollPaneUI());
        chatPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        chatInputField = new JTextField();
        chatInputField.setBounds(2,360,305,20);
        chatInputField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Game.setMessage(chatInputField.getText());
                    Game.setSendMsg(true);
                    chatInputField.setText("");
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        });

        sendButton = new JButton("Send");
        sendButton.setBounds(310,360,70,19);
        sendButton.addActionListener(this);

        newGameButton = new JButton("NEW!");
        newGameButton.setBounds(260, 190, 125, 60);
        newGameButton.addActionListener(this);

        gameWindow.add(gamePanel);
        gameWindow.setJMenuBar(menuBar);
        gameWindow.add(chatPane);
        gameWindow.add(chatInputField);
        gameWindow.add(sendButton);
        gameWindow.add(newGameButton);
        
        connectToFrame = new ConnectToFrame("Connect to Host", this);
        hostFrame = new HostFrame("Host a game", this);
    }

    /**
     * Implementation of ActionListener interface's method, handles events fired
     * by the frame's components.
     *
     * @param e fired event.
     */
    public void actionPerformed(ActionEvent e) {

        Object clickedItem = e.getSource();

        //change button state and send move
        for(int i = 0; i < 9; i++) {
            if(clickedItem == buttons[i] && (Game.getGrid()[i] == 'E') && Game.isMyTurn()) {
                //JOptionPane.showMessageDialog(null, "Clicked button " + i + j);
                if(Game.isiAmX()) {
                    Game.changeBoard(i, 'X');
                    Game.setMove("#" + i );
                    Game.setSendMove(true);
                    Game.setMyTurn(false);
                    buttons[i].setText("X");
                }
                else {
                    Game.changeBoard(i,'O');
                    Game.setMove("#" + i);
                    Game.setSendMove(true);
                    Game.setMyTurn(false);
                    buttons[i].setText("O");
                }
            }
        }

        //open Connection frame
        if(clickedItem == connectMenuItem){
            connectToFrame.setVisible(true);
        }

        //open Host frame
        if(clickedItem == hostMenuItem) {
            hostFrame.setVisible(true);
        }

        //exit application
        if(clickedItem == exitMenuItem) {
            System.exit(0);
        }

        //send chat message
        if(clickedItem == sendButton) {
            Game.setMessage(chatInputField.getText());
            Game.setSendMsg(true);
            chatInputField.setText("");
        }

        //start new game
        try {
            if(clickedItem == newGameButton) {
                Game.clearGrid();
                Game.setMove("#N");
                Game.setSendMove(true);
            }
        } catch(NullPointerException exc) {
            JOptionPane.showMessageDialog(null, "No opponent yet");
        }

    }
    
    public void updateChatArea(String newMessage) {
        if(newMessage != null && !newMessage.equals("")) {
            chatArea.append(newMessage);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
    
    public void updateButtons(int buttonIndex, String updateValue) {
        try {
            if(updateValue != null && !updateValue.equals("")) {
                buttons[buttonIndex].setText(updateValue);
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            //return
        }
    } 
    
    public void clearGrid() {
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setText("");
        }
    }
    
    /**
     * Entry point for the program
     *
     * @param args Main method arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameFrame();
            }
        });
    }

}
