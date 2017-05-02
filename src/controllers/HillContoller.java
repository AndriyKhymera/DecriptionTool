package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by andrii on 02.05.17.
 */
public class HillContoller {

    @FXML
    public TextField keyTextField;
    @FXML
    public TextArea inputTextArea;
    @FXML
    public TextArea ouputTextArea;
    @FXML
    public Button checkTheKeyButton;
    @FXML
    public Button encryptButton;
    @FXML
    public Button decryptButton;


    public int[][] keytomatrix(String key, int len)
    {
        int[][] keymatrix = new int[len][len];
        int c = 0;
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                keymatrix[i][j] = ((int) key.charAt(c)) - 97;
                c++;
            }
        }
        return keymatrix;
    }

}
