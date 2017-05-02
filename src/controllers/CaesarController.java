package controllers;

import entity.Statistic;
import entity.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;

/**
 * Created by andrii on 08.03.17.
 */
public class CaesarController {

    @FXML
    private TextField tab2_alphabetTextField;
    @FXML
    private TextField tab2_keyTextField;
    @FXML
    private TextArea tab2_inputTextArea;
    @FXML
    private TextArea tab2_outputTextArea;
    @FXML
    private Button decryptButton;

    private Statistic statistic;

    @FXML
    public void initialize() throws FileNotFoundException {
        String text = Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/С_70");
        tab2_inputTextArea.setText(text);
        statistic = Statistic.calculateStatistic(tab2_inputTextArea.getText());
        tab2_alphabetTextField.setText(statistic.getAlphabet());
    }

    @FXML
    private void decryptButtonHandler() {
        statistic = Statistic.calculateStatistic(tab2_inputTextArea.getText());
        tab2_alphabetTextField.setText(statistic.getAlphabet());

        String inputText = tab2_inputTextArea.getText();
        Integer shift = 0;
        try {
            shift = Integer.parseInt(tab2_keyTextField.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Eror");
            alert.setContentText("Ooops, you forgor to enter the key.");
            alert.showAndWait();
        }

        String outputText = decryptCaesar(inputText, shift);
        tab2_outputTextArea.setText(outputText);
    }

    public String decryptCaesar(String encryptText, int shift){
        StringBuilder decryptText = new StringBuilder("");
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,;-'";
        int index;
        int newIndex;

        for (int i = 0; i < encryptText.length(); i++){
            index = alphabet.indexOf(encryptText.charAt(i));
            if((index + shift) > alphabet.length()-1){
                newIndex = index + shift - alphabet.length();
            }
            else newIndex = index + shift;
            if (newIndex==-1){
                continue;
            }
            decryptText.append(alphabet.charAt(newIndex));
        }

        return decryptText.toString();
    }

}
