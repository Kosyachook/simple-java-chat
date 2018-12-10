package com.kosyachook.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ConnectionHandler implements Runnable {
    //Scanner scn = new Scanner(System.in);
    String name;
    Socket socket;
    boolean isConnected;
    final DataInputStream dis;
    final DataOutputStream dos;
    Recipient recipient;

    public ConnectionHandler(Socket s, String name, int id, DataInputStream dis, DataOutputStream dos) {
        this.socket = s;
        this.name = name;
        this.dis = dis;
        this.dos = dos;
        this.isConnected = true;
        this.recipient = Recipient.createRecipient(name, id);
    }

    @Override
    public void run() {
        String received;

        while (true){
            try {
                received = dis.readUTF();
                System.out.println(received);

                if(received.equals("logout")){
                    this.isConnected = false;
                    this.socket.close();
                    //ConnectionController.ar.indexOf(this);
                    ConnectionController.ar.remove(this);
                    break;
                }
                String message = received;
                /*StringTokenizer stringTokenizer = new StringTokenizer(received,"#");
                String message = stringTokenizer.nextToken();
                String recipientName = stringTokenizer.nextToken();

                for(ConnectionHandler mc : ConnectionController.ar){
                    if(mc.name.equals(recipientName) && this.isConnected == true){
                        mc.dos.writeUTF(this.name + " : " + message);
                        break;
                    }
                }*/

                for(ConnectionHandler mc : ConnectionController.ar){
                    mc.dos.writeUTF(message + "\nconnected now" + ConnectionController.ar.size());
                    //System.out.println(ConnectionController.ar.indexOf(this));
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try {
            this.dis.close();
            this.dos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
