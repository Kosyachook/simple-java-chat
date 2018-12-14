package com.kosyachook.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerController {
    static Vector<ConnectionHandler> ar = new Vector<>();

    static int counter = 0;
    public static void main(String [] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket s;
        while (true){

            System.out.println(ar.size());
            s = serverSocket.accept();

            System.out.println("New client request received : " + s);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");
            ConnectionHandler mth = new ConnectionHandler(s, "client " + counter, counter, dis, dos);

            Thread thread = new Thread(mth);
            System.out.println("Adding this client to active client list");
            ar.add(mth);
            thread.start();
            counter++;
        }
    }
}
