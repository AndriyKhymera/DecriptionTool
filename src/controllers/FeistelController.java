package controllers;

import entity.Statistic;
import entity.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by andrii on 18.05.17.
 */
public class FeistelController implements Initializable{


    @FXML
    private TextArea tab6_inputTextArea;
    @FXML
    private TextArea tab6_outputTextArea;
    @FXML
    private TextField roundsAmountTextField;
    @FXML
    private TextField k0_TextField;
    @FXML
    private TextArea calculationTextArea;
    @FXML
    private RadioButton increaseRadioButton;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,;'-";
    private final int blockSize = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            tab6_inputTextArea.setText(Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Caesar_decryptedText"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        roundsAmountTextField.setText("9");
        k0_TextField.setText("11");
        increaseRadioButton.fire();
    }

    @FXML
    void encryptButtonHandler() {
        boolean incdec = increaseRadioButton.isSelected();
        int roundsVal = Integer.parseInt(roundsAmountTextField.getText());

        int k = Integer.parseInt(k0_TextField.getText());
        String encoded = process(roundsVal, k, incdec, tab6_inputTextArea.getText());

        tab6_outputTextArea.setText(encoded);
        calculationTextArea.setText(getGeneratedMatrix());
    }

    @FXML
    void decryptButtonHandler() {
        boolean incdec = increaseRadioButton.isSelected();
        int roundsVal = Integer.parseInt(roundsAmountTextField.getText());
        int k = Integer.parseInt(k0_TextField.getText());
//        if (incdec) {
//            k = k + roundsVal - 1;
//            incdec = false;
//        } else {
//            k = Math.abs(roundsVal - k) + 1;
//            incdec = true;
//        }
        String decoded = process(roundsVal, k, incdec, tab6_inputTextArea.getText());
        tab6_outputTextArea.setText(decoded);
        calculationTextArea.setText(getGeneratedMatrix());
    }


    private StringBuilder generatedMatrix = new StringBuilder();

    public String getGeneratedMatrix() {
        return generatedMatrix.toString();
    }

    public String process(int rounds, int k, boolean incdec, String text) {
        String[] strings = devide(text);
        StringBuilder sb = new StringBuilder("");
        for (String s: strings){
            if (s==null){
                break;
            }
            sb.append(processFeistil(s.charAt(0), s.charAt(1), incdec, rounds, k));
        }
        return sb.toString();
    }

    private String processFeistil(char l, char r, boolean incdec, int rounds, int k) {
        int left = alphabet.indexOf(l);
        int right = alphabet.indexOf(r);
        int leftForm = 7 - "left".length();
        int rightForm = 7 - "right".length();
        int kForm = 7 - "right".length();
        int nForm = 7 - "N".length();
        System.out.println(alphabet.length());
        generatedMatrix.append(String.format("%-" + leftForm + "s %-" + rightForm + "s %-" +
                kForm + "s %-" + nForm + "s \n", "L", "R", "K", "No"));
        for (int i = 0; i < rounds; i++) {
            int prevLeft = left;
            System.out.println("L: " + left);
            System.out.println("R: " + right);
            left = ((prevLeft + k)%(alphabet.length())) ^ right;
            right = prevLeft;
            k += incdec ? 1 : -1;
            if (i == rounds - 1) {
                right = left;
                left = prevLeft;
            }
            leftForm = 7 - String.valueOf(left).length();
            rightForm = 7 - String.valueOf(right).length();
            kForm = 7 - String.valueOf(kForm).length();
            nForm = 7 - String.valueOf(nForm).length();
            generatedMatrix.append(String.format("%-" + leftForm + "d %-" + rightForm + "d %-" +
                    kForm + "d %-" + nForm + "d \n", left, right, k, i));
        }
        generatedMatrix.append(String.format("\n"));
        return String.valueOf(alphabet.charAt(left)) + String.valueOf(alphabet.charAt(right));
    }

    @FXML
    public void lineChartsButtonHandler()throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphicsWindowl.fxml"));
        Parent root = root = fxmlLoader.load();

        GraphicsWindowController newWindow = fxmlLoader.<GraphicsWindowController>getController();
        newWindow.setDectyptedTextStatistic(Statistic.findFrequency(tab6_inputTextArea.getText(), 1));
        newWindow.setEncryptedTextStatistic(Statistic.findFrequency(tab6_outputTextArea.getText(), 1));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public String[] devide(String text) {
        StringBuilder stringBuilder = new StringBuilder(text);

        //Обраховуємо скільки символів необхідно добавити
        //Написано тільки для випадку коли довжина блоку 2
        int amountOfWhiteSpaceToAdd = (text.length()%blockSize == 0)? 0:1;

        //Доповнюємо стрічку пробліами щоб розділити її на рівні стрічки
        stringBuilder.append(String.join("", Collections.nCopies(amountOfWhiteSpaceToAdd, " ")));

        //Саме розбиваємо на підстрічки
        String[] result = new String[stringBuilder.length()];
        for (int i = 0, j = 0; j < stringBuilder.length(); i++, j+=blockSize){
            result[i] = stringBuilder.substring(j, j+blockSize);
        }

        return result;
    }
}
