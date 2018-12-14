package com.kosyachook.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionHandler implements Runnable {
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
        this.greeting();
        while (true){
            try {
                //dos.writeUTF("Type #logout to terminate connection.\n" + "Or #X to change recipient index");
                received = dis.readUTF();
                System.out.println(received);

                String recipientName = this.name;

                if(received.equals("#logout")){
                    this.isConnected = false;
                    this.socket.close();
                    //ServerController.ar.indexOf(this);
                    ServerController.ar.remove(this);
                    break;
                }

                if(received.matches("^#client\\s\\d+")){
                    System.out.println("Matches valid mame\n" );
                    for(ConnectionHandler handler : ServerController.ar){
                        if(handler.recipient.getName().equals(received)){
                            handler.recipient.setReceiving(true);
                        }
                    }
                }
                String message = received;

                for(ConnectionHandler mc : ServerController.ar){
                    if(mc.recipient.isReceiving() && this.isConnected == true){
                        mc.dos.writeUTF(message);
                        //break;
                    }else{
                        mc.dos.writeUTF(message);
                        //break;
                    }
                    //System.out.println(ServerController.ar.indexOf(this));
                }

            }catch (IOException e){
                e.printStackTrace();
                this.terminateSession();
                break;
            }
        }
        try {
            this.dis.close();
            this.dos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void terminateSession(){
        try {
            this.isConnected = false;
            this.socket.close();
            //ServerController.ar.indexOf(this);
            ServerController.ar.remove(this);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("User Left unexcepted");
        }
    }
    private void greeting(){
        try {
            dos.writeUTF("Type #logout to terminate connection.\n" + "Or #X to change recipient index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
