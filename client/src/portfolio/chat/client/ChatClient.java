package portfolio.chat.client;


import java.io.*;
import java.net.*;
import java.util.*;


public class ChatClient {
    public static void main(String args[]){
        try{
            Socket s = new Socket("localhost",8189);
            try{
                InputStream inStream = s.getInputStream();
                OutputStream outStream = s.getOutputStream();

                Scanner in = new Scanner(inStream);
                Scanner userin = new Scanner(System.in);
                PrintWriter out = new PrintWriter(outStream,true);
                while(in.hasNextLine()){
                    String line = in.nextLine();
                    System.out.println(line);
                    if (!line.equals("Echo: BYE")) out.println(userin.nextLine());
                }
            }
            finally{
                s.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
