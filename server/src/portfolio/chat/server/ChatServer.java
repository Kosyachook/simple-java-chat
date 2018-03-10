package portfolio.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements NetworkConnectionListener {
    public static void main(String[] args){
        new ChatServer();
    }
    private final ArrayList<NetworkConnection> connections = new ArrayList<>();
    private ChatServer(){
        System.out.println("Server is running....");
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while (true){
                try{
                    new NetworkConnection(this, serverSocket.accept());
                }catch(IOException e){
                    System.out.println("Network connection exception: " + e);
                }
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConnectionReady(NetworkConnection connection) {
        connections.add(connection);
        sendToAllConnectiobs("Client connected: " + connection);
    }

    @Override
    public void onRecieveData(NetworkConnection connection, String value) {
        sendToAllConnectiobs(value);
        if(value.contains("Q"))
            connection.disconnect();
    }

    @Override
    public void onDisconnect(NetworkConnection connection) {
        connections.remove(connection);
        sendToAllConnectiobs("Client disconnected: " + connection);
    }

    @Override
    public void onException(NetworkConnection connection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }
    private void sendToAllConnectiobs(String value){
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++)
            connections.get(i).sendData(value);
    }

}
