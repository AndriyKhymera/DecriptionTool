package controllers;

import entity.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.*;

public class GraphicsWindowController implements Initializable{

    @FXML
    private CategoryAxis symbolsAxis;
    @FXML
    private NumberAxis frequencyAxis;
    @FXML
    private LineChart lineChart;

    private Map<String, Integer> dectyptedTextStatistic;
    private Map<String, Integer> encryptedTextStatistic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void display(){
        lineChart.getData().clear();

        symbolsAxis.setLabel("Symbols");
        frequencyAxis.setLabel("Frequency");

        dectyptedTextStatistic = Utils.sortByValueDsc(dectyptedTextStatistic);
        encryptedTextStatistic = Utils.sortByValueDsc(encryptedTextStatistic);


        XYChart.Series dectyptedTextFrequency = new XYChart.Series();
        dectyptedTextFrequency.setName("Статистика защифрованого тексту.");
        XYChart.Series enctyptedTextFrequency = new XYChart.Series();
        enctyptedTextFrequency.setName("Статистика розшифрованого тексту.");

        Iterator iterator = encryptedTextStatistic.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
            if(entry.getKey().equals("Z") || entry.getKey().equals("-") || entry.getKey().equals(";")){
                iterator.remove();
            }
        }

        List<String> decryptedTextKeys = new ArrayList<>();
        decryptedTextKeys.addAll(dectyptedTextStatistic.keySet());

        List<String> encryptedTextKeys = new ArrayList<>();
        encryptedTextKeys.addAll(encryptedTextStatistic.keySet());

        List<String> keys = new ArrayList<>();

        String key;

        int size = (decryptedTextKeys.size() >= encryptedTextKeys.size()) ? encryptedTextKeys.size() : decryptedTextKeys.size();
        for (int i = 0; i < size; i++){
            key = decryptedTextKeys.get(i) + " (" + encryptedTextKeys.get(i) + ")";
            keys.add(i, key);

            dectyptedTextFrequency.getData().add(new XYChart.Data<>(keys.get(i), dectyptedTextStatistic.get(decryptedTextKeys.get(i))));
            enctyptedTextFrequency.getData().add(new XYChart.Data<>(keys.get(i), encryptedTextStatistic.get(encryptedTextKeys.get(i))));
        }
        lineChart.getData().addAll(dectyptedTextFrequency, enctyptedTextFrequency);
    }

    public Map<String, Integer> getDectyptedTextStatistic() {
        return dectyptedTextStatistic;
    }

    public void setDectyptedTextStatistic(Map<String, Integer> dectyptedTextStatistic) {
        this.dectyptedTextStatistic = dectyptedTextStatistic;
    }

    public Map<String, Integer> getEncryptedTextStatistic() {
        return encryptedTextStatistic;
    }

    public void setEncryptedTextStatistic(Map<String, Integer> encryptedTextStatistic) {
        this.encryptedTextStatistic = encryptedTextStatistic;
    }
}
