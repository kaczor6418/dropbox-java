package k.kaczynski;

import java.io.IOException;;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 1997;
    private static final String[] SERVER_DISCS = {"discOne/", "discTwo/", "discThree/", "discFour/", "discFive/"};

    public static void main(String[] args) {
        int[] discCurrentUsers = {1, 0, 0, 0, 0};
        int nextDiscIndexWithLowestCountOfUsers;
        Object synchronizingObject = new Object();
        String assignedDisc =  SERVER_DISCS[0];
        Socket socket;
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
                socket = serverSocket.accept();
                System.out.println("connection Established");
                ServerThread serverThread = new ServerThread(socket, assignedDisc, synchronizingObject);
                nextDiscIndexWithLowestCountOfUsers = getMinValueIndex(discCurrentUsers);
                discCurrentUsers[nextDiscIndexWithLowestCountOfUsers] = discCurrentUsers[nextDiscIndexWithLowestCountOfUsers] + 1;
                assignedDisc = SERVER_DISCS[nextDiscIndexWithLowestCountOfUsers];
                serverThread.start();

            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }

    public static int getMinValueIndex(int[] array) {
        int minValue = array[0];
        int indexOfMinValue = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                indexOfMinValue = i;
            }
        }
        return indexOfMinValue;
    }
}
