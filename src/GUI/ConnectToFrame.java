/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

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

    private static ConnectToFrame connectionFrame;
    private static JLabel hostIPLabel = new JLabel("Host IP: ");
    private static JLabel hostPortLabel = new JLabel("Host port: ");
    private static JLabel nicknameLabel = new JLabel("Your nick: ");
    private static JTextField hostIPField = new JTextField();
    private static JTextField hostPortField = new JTextField();
    private static JTextField nicknameField = new JTextField();
    private static JButton okButton = new JButton("OK");
    private static JButton cancelButton = new JButton("Cancel");

    private ConnectToFrame(String name){
        super(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(200,170);
        this.setResizable(false);

        hostIPLabel.setBounds(10, 18, 50, 10);
        hostIPField.setBounds(75, 12, 100, 20);
        hostPortLabel.setBounds(10, 43, 100, 15);
        hostPortField.setBounds(75, 43, 100, 20);
        nicknameLabel.setBounds(10, 73, 100, 15);
        nicknameField.setBounds(75, 73, 100, 20);
        okButton.setBounds(15, 103, 70, 30);
        okButton.addActionListener(this);
        cancelButton.setBounds(90, 103, 80, 30);
        cancelButton.addActionListener(this);

        this.add(hostIPLabel);
        this.add(hostIPField);
        this.add(hostPortLabel);
        this.add(hostPortField);
        this.add(nicknameLabel);
        this.add(nicknameField);
        this.add(okButton);
        this.add(cancelButton);
    }


    /**
     * Singleton design pattern method, gets the instance of ConnectToFrame
     *
     * @return The single instance of ConnectToFrame
     */
    public static ConnectToFrame getConnectToFrame() {
        if(connectionFrame == null) {
            return new ConnectToFrame("ConnectTo");
        }
        else {
            return connectionFrame;
        }
    }

    /**
     * Implementation of ActionListener interface's method, handles events fired
     * by the frame's components.
     * @param e Fired event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            ClientThread client = new ClientThread(hostIPField.getText(), Integer.parseInt(hostPortField.getText()), nicknameField.getText());
            this.dispose();
        }
        if(e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}

