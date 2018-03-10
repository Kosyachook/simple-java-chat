package portfolio.echo.server;


import java.io.*;
import java.net.*;
import java.util.*;


public class EchoServer {
    private ArrayList<Socket> connections = new ArrayList<>();

    public static void main(String[] args )
    {
        try (ServerSocket s = new ServerSocket(8189))
        {
            int i = 1;

            while (true)
            {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
                System.out.println(r.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}