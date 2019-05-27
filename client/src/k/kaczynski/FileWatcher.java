package k.kaczynski;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {

    private Path path;
    private PrintWriter userInputStream;

    FileWatcher(Path path, PrintWriter userInputStream) {
        this.path = path;
        this.userInputStream = userInputStream;
    }

    void watchDirectoryPath() {
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException(String.format("Path: %s is not a folder", path));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println(String.format("Watching path: %s", path));
        FileSystem fileSystem = path.getFileSystem();
        try (WatchService service = fileSystem.newWatchService()) {
            path.register(service, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            WatchKey key;
            do {
                key = service.take();
                Kind<?> operationType;
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    operationType = watchEvent.kind();
                    ArrayList<String> fileData = new ArrayList<>();
                    Map<String, ArrayList<String>> dataToSend = new HashMap<>();
                    Path newFilePath = ((WatchEvent<Path>) watchEvent).context();
                    if (ENTRY_CREATE == operationType) {
                        if (!newFilePath.toString().contains("___jb_")) {
                            System.out.println("Creating the file: " + newFilePath);
                            fileData.add(operationType.toString());
                            dataToSend.put(newFilePath.toString(), fileData);
                            userInputStream.println(dataToSend);
                        }
                    }
                    if (ENTRY_MODIFY == operationType) {
                        if (!newFilePath.toString().contains("___jb_")) {
                            System.out.println("Modifying the file: " + newFilePath);
                            Path modifiedPath = Paths.get(path.toString(), newFilePath.toString());
                            FileHandler fileHandler = new FileHandler(modifiedPath.toString());
                            dataToSend.put(newFilePath.toString(), fileHandler.loadDataFromFile());
                            userInputStream.println(dataToSend);
                        }
                    }
                    if (ENTRY_DELETE == operationType) {
                        if (!newFilePath.toString().contains("___jb_")) {
                            System.out.println("Deleting the file: " + newFilePath);
                            fileData.add(operationType.toString());
                            dataToSend.put(newFilePath.toString(), fileData);
                            userInputStream.println(dataToSend);
                        }
                    }
                }
            }
            while (key.reset());
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
    }
}
