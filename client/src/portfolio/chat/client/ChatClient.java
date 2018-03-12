package portfolio.chat.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.*;


public class ChatClient extends JFrame implements ActionListener, NetworkConnectionListener {
    private static final String IP_ADDR = "127.0.0.1";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClient();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("alex");
    private final JTextField fieldInput = new JTextField();

    private NetworkConnection connect;

    private ChatClient(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname, BorderLayout.NORTH);

        setVisible(true);
        try {
            connect = new NetworkConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connect.sendData(fieldNickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(NetworkConnection connection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onRecieveData(NetworkConnection connection, String value) {
        printMsg(value);
        if(value.contains("Q"))
            connection.disconnect();
    }

    @Override
    public void onDisconnect(NetworkConnection connection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(NetworkConnection connection, Exception e) {
        printMsg("Connection exception: " + e);
        connection.disconnect();
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
