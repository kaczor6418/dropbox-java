package k.kaczynski;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StringToMapConverter {

    public StringToMapConverter() {

    }

    public static HashMap<String , ArrayList<String>> convert(String flatMap) {
        flatMap = flatMap.substring(1, flatMap.length() - 1);
        String[] filesWithDataList = flatMap.split("], ");
        filesWithDataList[0] = filesWithDataList[0] + "]";
        HashMap<String, ArrayList<String>> convertedMap = new HashMap<>();
        for (String file : filesWithDataList) {
            String[] keysAndValue = file.split("=");
            String fileName = keysAndValue[0];
            String fileData = keysAndValue[1];
            fileData = fileData.substring(1, fileData.length() - 2);
            ArrayList<String> filesDataList = new ArrayList<>(Arrays.asList(fileData.split(",")));
            convertedMap.put(fileName, filesDataList);
        }
        return convertedMap;
    }

}
