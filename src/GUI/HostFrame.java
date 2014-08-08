/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    private static HostFrame hostFrame;
    private static JLabel portLabel = new JLabel("Host port: ");
    private static JLabel nicknameLabel = new JLabel("Your nick: ");
    private static JTextField portField = new JTextField();
    private static JTextField nicknameField = new JTextField();
    private static JButton okButton = new JButton("OK");
    private static JButton cancelButton = new JButton("Cancel");

    private HostFrame(String name) {

        //initialize components
        super(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(200,130);
        this.setResizable(false);

        portLabel.setBounds(10, 3, 100, 15);
        portField.setBounds(75, 3, 100, 20);
        nicknameLabel.setBounds(10, 33, 100,15);
        nicknameField.setBounds(75,33,100,20);
        okButton.setBounds(15, 63, 70, 30);
        okButton.addActionListener(this);
        cancelButton.setBounds(90, 63, 80, 30);
        cancelButton.addActionListener(this);

        this.add(portLabel);
        this.add(nicknameLabel);
        this.add(portField);
        this.add(nicknameField);
        this.add(okButton);
        this.add(cancelButton);
    }

    /**
     * Singleton design pattern method, gets the instance of HostFrame
     *
     * @return The single instance of HostFrame
     */
    public static HostFrame getHostFrame() {
        if(hostFrame == null) {
            return new HostFrame("Host game");
        }
        else {
            return hostFrame;
        }
    }

    /**
     * Implementation of ActionListener interface's method, handles events fired
     * by the frame's components.
     * @param e fired event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            ServerThread serverThread = new ServerThread(Integer.parseInt(portField.getText()), nicknameField.getText());
            this.dispose();
        }
        if(e.getSource() == cancelButton) {
            this.dispose();
        }
    }

}
