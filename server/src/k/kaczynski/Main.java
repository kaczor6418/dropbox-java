package k.kaczynski;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 1978;

    public static void main(String[] args) {

        Socket socket = null;
        ServerSocket serverSocket = null;
        System.out.println("Server Listening......");

        try{
            serverSocket = new ServerSocket(PORT);

        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true){
            try{
                socket= serverSocket.accept();
                System.out.println("connection Established");
                ServerThread st = new ServerThread(socket);
                st.start();

            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}
