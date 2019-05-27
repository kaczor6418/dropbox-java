package k.kaczynski;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private String path;

    public FileHandler(String path) {
        this.path = path;
    }

    public  ArrayList<String> loadDataFromFile() {
        ArrayList<String> fileData = new ArrayList<>();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = reader.readLine()) != null) {
                fileData.add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

}
