package GUI;

import Game.Game;
import Networking.ClientThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * ConnectToFrame is a subframe of the game's GUI, it has only one instance
 * during runtime and is initialized when the user clicks the Connect To menu
 * item.
 *
 * @author HUD
 *
 */
public class ConnectToFrame extends JFrame implements ActionListener {
    
    private final JLabel hostIPLabel = new JLabel("Host IP: ");
    private final JLabel nicknameLabel = new JLabel("Your nick: ");
    private final JTextField hostIPField = new JTextField();
    private final JTextField nicknameField = new JTextField();
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");
    private final GameFrame gameFrame;
    private final Game game;

    public ConnectToFrame(String name, GameFrame gameFrame, Game game){
        super(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(false);
        this.setSize(200,150);
        this.setResizable(false);

        hostIPLabel.setBounds(10, 18, 50, 10);
        hostIPField.setBounds(75, 12, 100, 20);
        nicknameLabel.setBounds(10, 43, 100, 15);
        nicknameField.setBounds(75, 43, 100, 20);
        okButton.setBounds(15, 73, 70, 30);
        okButton.addActionListener(this);
        cancelButton.setBounds(90, 73, 80, 30);
        cancelButton.addActionListener(this);

        this.add(hostIPLabel);
        this.add(hostIPField);
        this.add(nicknameLabel);
        this.add(nicknameField);
        this.add(okButton);
        this.add(cancelButton);
        
        this.gameFrame = gameFrame;
        this.game = game;
    }

    /**
     * Implementation of ActionListener interface's method, handles events fired
     * by the frame's components.
     * @param e Fired event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            ClientThread client = new ClientThread(hostIPField.getText(), nicknameField.getText(), gameFrame, game);
            new Thread(client).start();
            this.dispose();
        }
        if(e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}

