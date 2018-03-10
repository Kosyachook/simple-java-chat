package portfolio.echo.server;


import java.io.*;
import java.net.*;
import java.util.*;


class ThreadedEchoHandler implements Runnable {
    private Socket incoming;

    public ThreadedEchoHandler(Socket incomingSocket)
    {
        incoming = incomingSocket;
    }

    @Override
    public void run() {
        try (InputStream inStream = incoming.getInputStream();
             OutputStream outStream = incoming.getOutputStream())
        {
            Scanner in = new Scanner(inStream, "UTF-8");
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(outStream, "UTF-8"),
                    true /* autoFlush */);

            out.println( "Hello! Enter BYE to exit." );

            // echo client input
            boolean done = false;
            while (!done && in.hasNextLine())
            {
                String line = in.nextLine();
                out.println("Echo: " + line);
                if (line.trim().equals("BYE")) {
                    incoming.close();
                    done = true;
                }
            }
            onDisconnect(incoming);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
   public String toString(){
        return "Connected: " + incoming.getInetAddress();
   }
    public void onDisconnect(Socket income){
        System.out.println("Disconnected " + income.getInetAddress());
    }
}
