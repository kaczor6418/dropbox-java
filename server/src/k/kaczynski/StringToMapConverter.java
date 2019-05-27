package k.kaczynski;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StringToMapConverter {

    public StringToMapConverter() {

    }

    public static HashMap<String , ArrayList<String>> convert(String flatMap) {
        flatMap = flatMap.substring(1, flatMap.length()-1);
        String[] keyAndValue = flatMap.split("=");
        HashMap<String, ArrayList<String>> convertedMap = new HashMap<>();
        String[] data = keyAndValue[1].substring(1, keyAndValue[1].length()-1).split(",");
        ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data));
        convertedMap.put(keyAndValue[0], dataList);
        return convertedMap;
    }
}
