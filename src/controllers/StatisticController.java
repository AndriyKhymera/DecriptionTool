package controllers;

import entity.Range;
import entity.Statistic;
import entity.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticController{

    private static Statistic statistic;
    private static List<Range> ranges;
    private static int index;

    @FXML
    private TextField searchTextField;
    @FXML
    private StyleClassedTextArea inputTextArea;
    @FXML
    private TextArea ouputTextArea;
    @FXML
    private Button openFileButton;
    @FXML
    private ToggleGroup numberOfSymbolsToggleGroup;
    @FXML
    private TextField alphabetTextField;
    @FXML
    private RadioButton oneSymbolRadioButton;
    @FXML
    private RadioButton twoSymbolsRadioButton;
    @FXML
    private RadioButton threeSymbolsRadioButton;
    @FXML
    private CheckBox sortByFrequencyCheckBox;
    @FXML
    private TextArea oneSymbolOutputTextArea;
    @FXML
    private TextArea twoSymbolsOutputTextArea;
    @FXML
    private TextArea threeSymbolsOutputTextArea;
    @FXML
    private Button calculateStatistic;
    @FXML
    private CategoryAxis symbols;
    @FXML
    private NumberAxis frequency;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private BarChart barChart;
    @FXML
    private Button searchButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button newBarChartWindowButton;

    @FXML
    private void initialize() throws FileNotFoundException {
        String text = Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/VT00");

        inputTextArea.setWrapText(true);
        inputTextArea.insertText(0,text);

        statistic = new Statistic();
    }

    @FXML
    public void showStatistic(){
        statistic = Statistic.calculateStatistic(inputTextArea.getText());
        updateTextFields(statistic);
    }

    public void updateTextFields(Statistic statistic){
        oneSymbolOutputTextArea.setText(Utils.mapToString(statistic.getOneSymbolFrequency()));
        twoSymbolsOutputTextArea.setText(Utils.mapToString(statistic.getTwoSymbolsFrequency()));
        threeSymbolsOutputTextArea.setText(Utils.mapToString(statistic.getThreeSymbolFrequency()));
        alphabetTextField.setText(statistic.getAlphabet());
    }

    @FXML
    private void openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть текстовий документ");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null){
            try {
                String text = Utils.loadTextFromFile(file.getPath().toString());
                inputTextArea.replaceText(text);
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                e.printStackTrace();
            }
        }
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
        barChart.setTitle("One symbol");

        //Добавляємо значення частота(їх може бути декілька)
        XYChart.Series frequencySeries = new XYChart.Series<>();

        //Заповнюємо даними
        for (Map.Entry<String, Integer> entry: data.entrySet()){
            if (entry.getValue() > 5) {
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

        updateTextFields(statistic);
    }

    @FXML
    public void searchButtonHandler(){
        ranges = findIndexes(inputTextArea.getText(), searchTextField.getText());
        index = 0;

        if (ranges.size()!=0) {
            inputTextArea.selectRange(ranges.get(index).getStart(), ranges.get(index).getEnd());
        }
    }

    @FXML
    private void changeSelection(ActionEvent actionEvent){
        Button pressedButton = (Button) actionEvent.getSource();

        switch (pressedButton.getId()){
            case "nextButton":
            {
                if (index == ranges.size() - 1){
                    index = 0;
                }
                ++index;
                inputTextArea.selectRange(ranges.get(index).getStart(), ranges.get(index).getEnd());
                break;
            }
            case "previousButton":
            {
                if(index == 0){
                    index = ranges.size();
                }
                --index;
                inputTextArea.selectRange(ranges.get(index).getStart(), ranges.get(index).getEnd());
                break;
            }
        }

    }

    public List<entity.Range> findIndexes(String text, String word){
        List<entity.Range> ranges = new ArrayList<>();
        String charSequence;
        int searchWordlength = word.length();
        for (int i =0 ; i < text.length() - searchWordlength; i++){
            charSequence = text.substring(i, i + searchWordlength);
            if (charSequence.equals(word)){
                ranges.add(new entity.Range(i, i + searchWordlength));
            }
        }
        return ranges;
    }

    @FXML
    private void barChartNewWindowHandler() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BarChartWindow.fxml"));
        Parent root = fxmlLoader.load();
        BarChartWindowController newWindow = fxmlLoader.<BarChartWindowController>getController();
        newWindow.setStatistic(statistic);

        Stage stage = new Stage();
        stage.setTitle("Гістограми");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
