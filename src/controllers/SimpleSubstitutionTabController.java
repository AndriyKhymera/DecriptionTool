package controllers;

import entity.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andrii on 20.03.17.
 */
public class SimpleSubstitutionTabController implements Initializable {

    @FXML
    private TextField tab3_alphabetTextField;
    @FXML
    private TextField tab3_keyTextField;
    @FXML
    private Button tab3_decryptButton;
    @FXML
    private TextArea tab3_inputTextArea;
    @FXML
    private TextArea tab3_outputTextArea;

    private String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXY, ;-'.";
    private String key =      "'BTUXVOZ ACKEMJHGIDYFLN-Q,WSRP";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab3_alphabetTextField.setText(alphabet);
        tab3_keyTextField.setText(key);
        try {
            tab3_inputTextArea.setText(Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Z70"));
        } catch (FileNotFoundException e) {
            System.out.println("Initialize file not found!");
        }
    }

    @FXML
    public void decryptButtonHandler(){
        String text = tab3_inputTextArea.getText();
        key = tab3_keyTextField.getText();
        alphabet = tab3_alphabetTextField.getText();

        StringBuilder decryptedText = new StringBuilder();
        int newIndex;

        for (int i = 0; i < text.length();i++){
            newIndex = key.indexOf(text.charAt(i));
            System.out.println(text.charAt(i));
            decryptedText.append(alphabet.charAt(newIndex));
        }

        tab3_outputTextArea.setText(decryptedText.toString());
    }
}
