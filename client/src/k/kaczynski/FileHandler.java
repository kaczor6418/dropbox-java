package k.kaczynski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class FileHandler implements Runnable{

    private String filePath;
    private ArrayList<String> fileData;

    public FileHandler(String path) {
        this.filePath = path;
    }

    public FileHandler(String filePath, ArrayList<String> fileData) {
        this.filePath = filePath;
        this.fileData = fileData;
    }

    public  ArrayList<String> loadDataFromFile() {
        ArrayList<String> fileData = new ArrayList<>();
        File file = new File(filePath);
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

    private void deleteFile() {
        File file = new File(filePath);
        file.delete();
    }

    private void createFillAndFile() {
        try {
            Files.write(Paths.get(filePath),
                    fileData,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException exception) {
            System.out.println (exception.toString());
            System.out.println(String.format("Could not modify file under following path -> %s", filePath));
        }
    }

    @Override public void run() {
        deleteFile();
        createFillAndFile();
    }
}
