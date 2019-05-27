package k.kaczynski;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    private static final int PORT = 1997;

    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        Socket socket;
        BufferedReader serverOutput = null;
        PrintWriter userInputStream = null;
        UserInputValidator userInputValidator = new UserInputValidator();
        String userDirectoryPath;
        String userName;

        if (args.length == 2) {
            userName = userInputValidator.isValidUserName(args[0]);
            userDirectoryPath = userInputValidator.isValidDirectoryPath(args[1]);
        } else {
            System.out.println("Incorrect input arguments");
            userName = userInputValidator.getUserName();
            userDirectoryPath = userInputValidator.getDirectoryPath();
        }
        try {
            socket = new Socket(address, PORT);
            serverOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInputStream = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        userInputStream.println(userName);
        String watcherDirectoryPath = userDirectoryPath + userName + "Disc";
        System.out.println(String.format("Your files inside: %s, are synchronised with your server disc", watcherDirectoryPath));
        try {
            File userDir = new File(watcherDirectoryPath);
            FileWatcher fileWatcher = new FileWatcher(userDir.toPath(), userInputStream);
            fileWatcher.watchDirectoryPath();
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{
            System.out.println("Connection Closed");
        }
    }
}
