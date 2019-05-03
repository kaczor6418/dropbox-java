package k.kaczynski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    private static final int PORT = 1978;

    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        Socket socket = null;
        BufferedReader userBufferStream = null;
        BufferedReader serverOutput = null;
        PrintWriter outputStream = null;

        try {
            socket = new Socket(address, PORT);
            userBufferStream =  new BufferedReader(new InputStreamReader(System.in));
            serverOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream =  new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println(String.format("Client Address : %s", address));
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String userInput = null;
        try{
            userInput = userBufferStream.readLine();
            while(userInput.compareTo("QUIT") != 0){
                outputStream.println(userInput);
                outputStream.flush();
                System.out.println(String.format("Server Response : %s", serverOutput.readLine()));
                userInput = userBufferStream.readLine();

            }
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{
            System.out.println("Connection Closed");
        }
    }
}
