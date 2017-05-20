package entity;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by andrii on 08.03.17.
 */
public class Utils {

    public static String mapToString(Map<String, Integer> map){
        StringBuilder stringBuilder = new StringBuilder("");

        for (Map.Entry<String, Integer> entry: map.entrySet()){
            stringBuilder.append("[ " + entry.getKey() + " ]" + "  -  " + entry.getValue() + "\n");
        }

        return stringBuilder.toString();
    }

    public static Map<String, Integer> sortByValueAsc(Map<String, Integer> map){
        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        return sortedMap;
    }

    public static Map<String, Integer> sortByValueDsc(Map<String, Integer> map){
        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        return sortedMap;
    }

    public static Map<String, Integer> sortByKeyDsc(Map<String, Integer> map){
        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        return sortedMap;
    }

    public static Map<String, Integer> deleteStringWithWhitespace(Map<String, Integer> map){
        Map<String, Integer> result = new HashMap<>();

        Iterator iterator = map.entrySet().iterator();
        Map.Entry<String, Integer> entry;

        while (iterator.hasNext()){
            entry = ((Map.Entry<String, Integer>)iterator.next());
            if (!entry.getKey().contains(" ")){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return  result;
    }

    public static String loadTextFromFile(String path) throws FileNotFoundException {
        File file = new File(path);
        StringBuilder text = new StringBuilder("");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()){
            text.append(scanner.nextLine());
        }

        return text.toString();
    }

    public static Map<String, Integer> deleteStringWithNewLine(Map<String, Integer> map){
        Map<String, Integer> result = new HashMap<>();

        Iterator iterator = map.entrySet().iterator();
        Map.Entry<String, Integer> entry;

        while (iterator.hasNext()){
            entry = ((Map.Entry<String, Integer>)iterator.next());
            if (!entry.getKey().contains("\n")){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return  result;
    }

    public static void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
