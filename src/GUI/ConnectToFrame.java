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
    
    private JLabel hostIPLabel = new JLabel("Host IP: ");
    private JLabel hostPortLabel = new JLabel("Host port: ");
    private JLabel nicknameLabel = new JLabel("Your nick: ");
    private JTextField hostIPField = new JTextField();
    private JTextField hostPortField = new JTextField();
    private JTextField nicknameField = new JTextField();
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");

    public ConnectToFrame(String name){
        super(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(false);
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

