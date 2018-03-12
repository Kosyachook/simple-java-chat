package portfolio.chat.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class NetworkConnection {
    private final Socket income;
    private final Thread rxThread;
    private final NetworkConnectionListener networkListenner;
    private final BufferedReader in;
    private final BufferedWriter out;

    public NetworkConnection(NetworkConnectionListener networkListenner, String ipAddress, int port) throws IOException {
        this(networkListenner, new Socket(ipAddress, port));
    }
    public NetworkConnection(NetworkConnectionListener networkListenner, Socket income) throws IOException {
        this.income = income;
        this.networkListenner = networkListenner;
        in = new BufferedReader(new InputStreamReader(income.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(income.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    networkListenner.onConnectionReady(NetworkConnection.this);
                    while (!rxThread.isInterrupted()){
                        networkListenner.onRecieveData(NetworkConnection.this, in.readLine());
                    }
                }
                catch (IOException e){
                    networkListenner.onException(NetworkConnection.this, e);
                }
                finally {
                    networkListenner.onDisconnect(NetworkConnection.this);
                }
            }
        });
        rxThread.start();
    }
    public synchronized void sendData(String value){
        try{
            out.write(value + "\r\n");
            out.flush();
        }catch (IOException e){
            networkListenner.onException(this, e);
            disconnect();
        }

    }
    public synchronized void disconnect(){
        rxThread.interrupt();
        try{
            income.close();
        }catch(IOException e){
            networkListenner.onException(this, e);
        }
    }
    @Override
    public String toString(){
        return "Connecton: " + income.getInetAddress() + "Port: " + income.getPort();
    }
}
