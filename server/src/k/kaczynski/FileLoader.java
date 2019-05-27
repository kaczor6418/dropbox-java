package k.kaczynski;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileLoader implements Runnable {

    private String filePath;
    public ArrayList<String> fileData;

    public FileLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override public void run() {
        fileData = this.loadFileData();
    }

    public ArrayList<String> loadFileData() {
        ArrayList<String> userDiscData = new ArrayList<>();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = reader.readLine()) != null) {
                userDiscData.add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userDiscData;
    }
}
