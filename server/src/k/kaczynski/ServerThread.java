package k.kaczynski;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {

    private BufferedReader bufferedReader = null;
    private PrintWriter outputStream = null;
    private Socket socket;
    private String userName;
    private String assignedDisc;
    private Object synchronizingObject;

    ServerThread(Socket socket, String assignedDisc, Object synchronizingObject){
        this.socket = socket;
        this.assignedDisc = assignedDisc;
        this.synchronizingObject = synchronizingObject;
    }

    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);

        } catch(IOException e) {
            System.out.println("IO error in server thread");
        }
        String clientMessage;
        try {
            clientMessage = bufferedReader.readLine();
            userName = clientMessage;
            String userDiscPath = "serverDisc//" + assignedDisc;
            System.out.println(String.format("Server: %s is connected", userName));
            this.sendInitializeDataToClient();
            while(true) {
                if (!clientMessage.equals(userName)) {
                    HashMap<String, ArrayList<String>> fileData = new HashMap<>(StringToMapConverter.convert(clientMessage));
                    for (Map.Entry<String, ArrayList<String>> file : fileData.entrySet()) {
                        FileHandler fileHandler = new FileHandler(userDiscPath, file.getKey(), file.getValue(), userName, synchronizingObject);
                        fileHandler.saveChanges();
                    }
                }
                clientMessage = bufferedReader.readLine();
            }
        } catch (IOException e) {
            clientMessage = this.getName();
            System.out.println(String.format("IO Error/ Client %s terminated abruptly", clientMessage));
        } catch(NullPointerException e) {
            System.out.println(String.format("Client %s Closed", userName));
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (bufferedReader != null) {
                    bufferedReader.close();
                    System.out.println("Socket Input Stream Closed");
                }

                if(outputStream != null) {
                    outputStream.close();
                    System.out.println("Socket Out Closed");
                }
                if (socket != null) {
                    socket.close();
                    System.out.println("Socket Closed");
                }
            } catch(IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
    }

    private void sendInitializeDataToClient () {
        System.out.println(String.format("%s: Is synchronizing with server", userName));
        FileHandler initializeData = new FileHandler(userName);
        HashMap<String , ArrayList<String>> userServerDisc = initializeData.getAllUserFilesWithData();
        if (userServerDisc.size() > 0) {
            outputStream.println(userServerDisc);
        } else {
            outputStream.println("Nothing to initialize");
        }
        System.out.println(String.format("Server: Initialized data has been send to %s. User is synchronized with server", userName));
    }
}
