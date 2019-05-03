package k.kaczynski;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private String userInput = null;
    private BufferedReader bufferedReader = null;
    private PrintWriter outputStream = null;
    private Socket socket = null;

    ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

        try {
            userInput = bufferedReader.readLine();
            while(userInput.compareTo("QUIT") != 0){
                outputStream.println(userInput);
                outputStream.flush();
                System.out.println(String.format("Response to Client  :  %s", userInput));
                userInput = bufferedReader.readLine();
            }
        } catch (IOException e) {
            userInput =this.getName();
            System.out.println(String.format("IO Error/ Client %s terminated abruptly", userInput));
        } catch(NullPointerException e){
            userInput =this.getName();
            System.out.println(String.format("Client %s Closed", userInput));
        } finally{
            try{
                System.out.println("Connection Closing..");
                if (bufferedReader != null){
                    bufferedReader.close();
                    System.out.println("Socket Input Stream Closed");
                }

                if(outputStream != null){
                    outputStream.close();
                    System.out.println("Socket Out Closed");
                }
                if (socket != null){
                    socket.close();
                    System.out.println("Socket Closed");
                }

            } catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
}
