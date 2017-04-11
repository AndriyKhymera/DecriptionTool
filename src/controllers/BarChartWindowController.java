package controllers;

import entity.Statistic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by andrii on 16.03.17.
 */
public class BarChartWindowController implements Initializable{

    private Statistic statistic;
    private RadioButton oneSymbolRadioButton;
    @FXML
    private RadioButton twoSymbolsRadioButton;
    @FXML
    private RadioButton threeSymbolsRadioButton;
    @FXML
    private CheckBox sortByFrequencyCheckBox;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private BarChart barChart;
    @FXML
    private CategoryAxis symbols;
    @FXML
    private NumberAxis frequency;
    @FXML
    private TextField frequencyBottomTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frequencyBottomTextField.setText("5");
    }

    @FXML
    public void radioButtonPressed(ActionEvent actionEvent){
        Toggle toggle = toggleGroup.getSelectedToggle();
        RadioButton selectedButton = (RadioButton) toggle;

        switch (selectedButton.getId()){
            case "oneSymbolRadioButton":
            {
                displayBarChart(statistic.getOneSymbolFrequency());
                break;
            }
            case "twoSymbolsRadioButton":
            {
                displayBarChart(statistic.getTwoSymbolsFrequency());
                break;
            }
            case "threeSymbolsRadioButton":
            {
                displayBarChart(statistic.getThreeSymbolFrequency());
                break;
            }
            case "searchButton":
            {
                break;
            }
        }
    }

    @FXML
    public void displayBarChart(Map<String, Integer> data){
        barChart.getData().clear();
        //Вісь X
        symbols.setLabel("Symbols");
        //Вісь Y
        frequency.setLabel("Frequency");

        //Створюємо графік
        //Добавляємо значення частота(їх може бути декілька)
        XYChart.Series frequencySeries = new XYChart.Series<>();

        int limit = Integer.parseInt(frequencyBottomTextField.getText());
        //Заповнюємо даними
        for (Map.Entry<String, Integer> entry: data.entrySet()){
            if (entry.getValue() > limit) {
                frequencySeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        }

        barChart.getData().add(frequencySeries);
    }

    @FXML
    public void sortByFrequencyCheckBoxStateChanged(){
        if (sortByFrequencyCheckBox.isSelected()){
            statistic.sortByValue();
        }else{
            statistic.sortByKey();
        }
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }
}
