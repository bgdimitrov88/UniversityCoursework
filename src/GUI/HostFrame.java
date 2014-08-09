package GUI;

import Networking.ServerThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * ConnectToFrame is a subframe of the game's GUI, it has only one instance
 * during runtime and is initialized when the user clicks the Host menu
 * item.
 *
 * @author HUD
 */
public class HostFrame extends JFrame implements ActionListener{
    
    private final JLabel nicknameLabel = new JLabel("Your nick: ");
    private final JTextField nicknameField = new JTextField();
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");
    
    private final GameFrame gameFrame;

    public HostFrame(String name, GameFrame gameFrame) {

        //initialize components
        super(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(false);
        this.setSize(200,130);
        this.setResizable(false);

        nicknameLabel.setBounds(10, 23, 100,15);
        nicknameField.setBounds(75,23,100,20);
        okButton.setBounds(15, 63, 70, 30);
        okButton.addActionListener(this);
        cancelButton.setBounds(90, 63, 80, 30);
        cancelButton.addActionListener(this);

        this.add(nicknameLabel);
        this.add(nicknameField);
        this.add(okButton);
        this.add(cancelButton);
        
        this.gameFrame = gameFrame;
    }

    /**
     * Implementation of ActionListener interface's method, handles events fired
     * by the frame's components.
     * @param e fired event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            ServerThread serverThread = new ServerThread(nicknameField.getText(), gameFrame);
            new Thread(serverThread).start();
            this.dispose();
        }
        if(e.getSource() == cancelButton) {
            this.dispose();
        }
    }

}
