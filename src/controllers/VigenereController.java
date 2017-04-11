package controllers;

import entity.Statistic;
import entity.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by andrii on 14.03.17.
 */
public class VigenereController {

    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,;-'";

    @FXML
    private TextField tab4_keyTextField;
    @FXML
    private Button tab4_decryptButton;
    @FXML
    private Button tab4_encryptButton;
    @FXML
    private TextArea tab4_inputTextArea;
    @FXML
    private TextArea tab4_outputTextArea;
    @FXML
    private Button tab4_graphicsButton;

    @FXML
    public void initialize() throws FileNotFoundException {
        tab4_keyTextField.setText("ANDRIIKHYMERA");
        String text = Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Caesar_decryptedText");
        tab4_inputTextArea.setText(text);
    }

    @FXML
    public void decryptButtonHandler(){
        String text = decrypt(tab4_inputTextArea.getText(), tab4_keyTextField.getText());
        tab4_outputTextArea.setText(text);
    }

    @FXML
    public void encryptButtonHandler(){
        String text = encrypt(tab4_inputTextArea.getText(), tab4_keyTextField.getText());
        tab4_outputTextArea.setText(text);
    }

    private String encrypt(String text, String keyWord){
        StringBuilder result = new StringBuilder();
        String key = createKey(text, keyWord);

        int index;
        int newIndex;
        int shift;
        for (int i = 0; i < text.length(); i++){
            index = alphabet.indexOf(text.charAt(i));
            shift = alphabet.indexOf(key.charAt(i));

            if((index + shift) > alphabet.length()-1){
                newIndex = index + shift - alphabet.length();
            }

            else newIndex = index + shift;
            result.append(alphabet.charAt(newIndex));
        }

        return  result.toString();
    }

    private String decrypt(String text, String keyWord){
        StringBuilder result = new StringBuilder();
        String key = createKey(text, keyWord);

        int index;
        int newIndex;
        int shift;
        for (int i = 0; i < text.length(); i++){
            index = alphabet.indexOf(text.charAt(i));
            shift = alphabet.indexOf(key.charAt(i));

            if((index - shift) < 0){
                newIndex = index - shift + alphabet.length();
            }

            else newIndex = index - shift;
            result.append(alphabet.charAt(newIndex));
        }

        return  result.toString();
    }

    private String createKey(String text, String keyWord) {
        StringBuilder key = new StringBuilder();
        int holeWordRepeat = text.length()/keyWord.length();
        int sub = text.length()%keyWord.length();

        for (int i = 0 ; i < holeWordRepeat; i++){
            key.append(keyWord);
        }

        key.append(keyWord.substring(0, sub));
        return key.toString();
    }

    @FXML
    private void graphicsButtonHandler() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphicsWindowl.fxml"));
        System.out.println(fxmlLoader.getLocation());
        Parent root = root = fxmlLoader.load();

        GraphicsWindowController newWindow = fxmlLoader.<GraphicsWindowController>getController();
        newWindow.setDectyptedTextStatistic(Statistic.findFrequency(tab4_inputTextArea.getText(), 1));
        newWindow.setEncryptedTextStatistic(Statistic.findFrequency(tab4_outputTextArea.getText(), 1));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
