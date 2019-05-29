package k.kaczynski;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    private String userDiscPath;
    private String userDataDiscPath;
    private String userName;
    private String filePath;
    private ArrayList<String> dataInfo;

    private final String SERVER_DISC_PATH = "serverDisc//";
    private final String[] SERVER_DISCS = {"userOneDisc/", "userTwoDisc/", "userThreeDisc/", "userFourDisc/", "userFiveDisc/"};

    public FileHandler(String filePath, ArrayList<String> dataInfo, String userName) {
        this.filePath = filePath;
        this.dataInfo = dataInfo;
        this.userName = userName;
        this.userDiscPath = SERVER_DISC_PATH + userName + "Disc/";
        this.userDataDiscPath = this.userDiscPath + "discData.txt";
    }

    public FileHandler(String userName) {
        this.userName = userName;
    }

    public void saveChanges() {
        String operationType = "";
        switch (dataInfo.get(0)) {
            case "ENTRY_CREATE":
                createFile(filePath);
                operationType = "ENTRY_CREATE";
                break;
            case "ENTRY_DELETE":
                operationType = "ENTRY_DELETE";
                deleteFile(filePath);
                break;
            default:
                modifyFile(filePath, dataInfo);
        }
        this.updateUserDiscData(operationType);
    }

    public HashMap<String , ArrayList<String>> getAllUserFilesWithData() {
        HashMap<String , String> specifiedUserFileList = new HashMap<>();
        for (String discName : SERVER_DISCS) {
            String discInfoFilePath = SERVER_DISC_PATH + discName;
            FileLoader discInfo = new FileLoader(discInfoFilePath + "discData.txt");
            ArrayList<String> allUserFiles = discInfo.loadFileData();
            for (String ownerAnfFile : allUserFiles) {
                String[] userNameFileName = ownerAnfFile.split(": ");
                if (userNameFileName[0].equals(userName)) {
                    specifiedUserFileList.put(discInfoFilePath, userNameFileName[1]);
                }
            }
        }
        int usersFilesCount = specifiedUserFileList.size();
        FileLoader[] filesRunners = new FileLoader[usersFilesCount];
        Thread[] filesThreads = new Thread[usersFilesCount];
        int idx = 0;
        for (Map.Entry<String, String> file: specifiedUserFileList.entrySet()) {
            String userFilePath = file.getKey() + file.getValue();
            filesRunners[idx] = new FileLoader(userFilePath);
            filesThreads[idx] = new Thread(filesRunners[idx]);
            filesThreads[idx].start();
            idx++;
        }
        for (Thread fileThread : filesThreads) {
            try {
                fileThread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        HashMap<String , ArrayList<String>> userFiles = new HashMap<>();
        idx = 0;
        for (String fileName : specifiedUserFileList.values()) {
            userFiles.put(fileName, filesRunners[idx].fileData);
        }
        return userFiles;
    }

    private void createFile(String fileToCreatePath) {
        System.out.println(String.format("%s: Creating the file -> %s", userName, fileToCreatePath));
        try {
            File file = new File(fileToCreatePath);
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println(String.format("%s: Failed to delete the file -> %s", userName, fileToCreatePath));
        }
        System.out.println(String.format("%s: File has been created -> %s", userName, fileToCreatePath));
    }

    private void deleteFile(String fileToCreatePath) {
        File file = new File(fileToCreatePath);
        if(file.delete()) {
            System.out.println(String.format("%s: File deleted successfully -> %s", userName, fileToCreatePath));
        } else {
            System.out.println(String.format("%s: Failed to delete the file -> %s", userName, fileToCreatePath));
        }
    }

    private void modifyFile(String pathOfModifiedFile, ArrayList<String> fileData) {
        System.out.println(String.format("%s: Modifying file -> %s", userName, pathOfModifiedFile));
        try {
            Files.write(Paths.get(pathOfModifiedFile),
                    fileData,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException exception) {
            System.out.println (exception.toString());
            System.out.println(String.format("%s: Could not modify file under following path -> %s",userName, pathOfModifiedFile));
        }
        System.out.println(String.format("%s: File has been modified -> %s", userName, pathOfModifiedFile));
    }


    private void updateUserDiscData(String operationType) {
        if (!operationType.equals("")) {
            FileLoader fileLoader =  new FileLoader(userDataDiscPath);
            ArrayList<String> discData = fileLoader.loadFileData();
            String[] pathElements = filePath.split("/");
            String ownerAndFileName = userName + ": " + pathElements[pathElements.length - 1];
            if (operationType.equals("ENTRY_CREATE")) {
                discData.add(ownerAndFileName);
            } else {
                discData.remove(ownerAndFileName);
            }
            this.deleteFile(userDataDiscPath);
            this.createFile(userDataDiscPath);
            this.modifyFile(userDataDiscPath, discData);
        }
    }

}
