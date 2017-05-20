package controllers;

import entity.Statistic;
import entity.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by andrii on 02.05.17.
 */
public class HillContoller implements Initializable{

    @FXML
    public TextField tab5_keyTextField;
    @FXML
    public TextArea tab5_inputTextArea;
    @FXML
    public TextArea tab5_outputTextArea;
    @FXML
    public Button tab5_checkTheKeyButton;
    @FXML
    public Button tab5_encryptButton;
    @FXML
    public Button tab5_decryptButton;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,;'-";
    private int[][] keyMatrix;
    private int[][] invertedKeyMatrix;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab5_keyTextField.setText("ANDRIIKHY");
        try {
            tab5_inputTextArea.setText(Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Caesar_decryptedText"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void checkTheKeyButtonHandler(){
        checkTheKey();
    }

    @FXML
    public void lineChartsButtonHandler()throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphicsWindowl.fxml"));
        Parent root = root = fxmlLoader.load();

        GraphicsWindowController newWindow = fxmlLoader.<GraphicsWindowController>getController();
        newWindow.setDectyptedTextStatistic(Statistic.findFrequency(tab5_inputTextArea.getText(), 1));
        newWindow.setEncryptedTextStatistic(Statistic.findFrequency(tab5_outputTextArea.getText(), 1));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean checkTheKey(){
        tab5_outputTextArea.clear();

        String keyString = tab5_keyTextField.getText();
        double length = keyString.length();
        double arraySize = Math.floor(Math.sqrt(length));

        tab5_outputTextArea.appendText("Key: " + keyString + "\n\n");

        if (Math.sqrt(length)%10 != arraySize){
            tab5_outputTextArea.appendText("\n\nKey is incorrect \nCause: matrix isn't square!");
            return false;
        }

        keyMatrix = keyToMatrix(keyString, (int) arraySize);
        tab5_outputTextArea.appendText(prettyArray(keyMatrix));

        int determinant = determinant(keyMatrix, (int) arraySize);
        tab5_outputTextArea.appendText("\nDet = " + determinant);

        if (determinant == 0 || determinant%alphabet.length() == 0){
            tab5_outputTextArea.appendText("\n\nKey is incorrect. \nCause: Det == 0!");
            return false;
        }else {
            tab5_outputTextArea.appendText("\n\nKey is correct.");
        }

        invertedKeyMatrix = calcInvertedMatrix(keyMatrix);

        tab5_outputTextArea.appendText("\nInverted matrix:\n");
        tab5_outputTextArea.appendText(prettyArray(invertedKeyMatrix));

        return true;
    }

    @FXML
    public void decryptButtonHandler(){
        boolean isKeyCorrect = checkTheKey();

        if (!isKeyCorrect){
            Utils.showError("Key isn't correct!");
            return;
        }else {
            tab5_outputTextArea.clear();
        }

        if (tab5_inputTextArea.getText().equals("")){
            Utils.showError("Please enter text to decrypt");
            return;
        }

        String text = tab5_inputTextArea.getText();

        String dectyptedText = decrypt(text);

        tab5_outputTextArea.setText(dectyptedText);
    }

    @FXML
    public void encryptButtonHandler(){
        boolean isKeyCorrect = checkTheKey();

        if (!isKeyCorrect){
            Utils.showError("Key isn't correct!");
            return;
        }else {
            tab5_outputTextArea.clear();
        }

        if (tab5_inputTextArea.getText().equals("")){
            Utils.showError("Please enter text to encrypt");
            return ;
        }

        String text = tab5_inputTextArea.getText();

        String encryptedText = encrypt(text);

        tab5_outputTextArea.setText(encryptedText);
    }

    public String decrypt(String text){
        String[] subStrings = devide(text);
        String[] encryptedSubStrings = perform(subStrings, invertedKeyMatrix);

        String result = String.join("", encryptedSubStrings);
        return result;
    }

    public String encrypt(String text){
        String[] subStrings = devide(text);
        String[] decryptedSubStrings = perform(subStrings, keyMatrix);

        String result = String.join("", decryptedSubStrings);
        return result;
    }

    public String[] devide(String s){
        StringBuilder stringBuilder = new StringBuilder(s);
        int KeyMatrixlength = keyMatrix.length;

        //Обраховуємо скільки символів необхідно добавити
        int amountOfWhiteSpaceToAdd = (s.length() % KeyMatrixlength == 0)?(0):(KeyMatrixlength - s.length() % KeyMatrixlength);

        //Доповнюємо стрічку пробліами щоб розділити її на рівні стрічки
        stringBuilder.append(String.join("", Collections.nCopies(amountOfWhiteSpaceToAdd, " ")));

        //Саме розбиваємо на підстрічки
        String[] result = new String[stringBuilder.length()/KeyMatrixlength];
        for (int i = 0, j = 0; j < stringBuilder.length(); i++, j+=KeyMatrixlength){
            result[i] = stringBuilder.substring(j, j+KeyMatrixlength);
        }

        return result;
    }

    public int[][] keyToMatrix(String key, int len) {
        int[][] keyMatrix = new int[len][len];
        int c = 0;
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                keyMatrix[i][j] = (alphabet.indexOf(key.charAt(c)));
                c++;
            }
        }
        return keyMatrix;
    }

    public int determinant(int A[][], int N){
        int res;
        if (N == 1)
            res = A[0][0];
        else if (N == 2)
        {
            res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
        }
        else
        {
            res = 0;
            for (int j1 = 0; j1 < N; j1++)
            {
                int m[][] = new int[N - 1][N - 1];
                for (int i = 1; i < N; i++)
                {
                    int j2 = 0;
                    for (int j = 0; j < N; j++)
                    {
                        if (j == j1)
                            continue;
                        m[i - 1][j2] = A[i][j];
                        j2++;
                    }
                }
                res += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1]
                        * determinant(m, N - 1);
            }
        }
        return res;
    }

    public String prettyArray(int[][] array){
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array.length; j++){
                stringBuilder.append(String.format("%5s", array[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String[] perform(String subStrings[], int[][] keyMatrix){
        int[] matrix;
        int[] multiplyKey;
        String[] result = new String[subStrings.length];
        int i =0;

        for (String temp: subStrings){
            System.out.println("Sub: " + temp);
            matrix = lineToMatrix(temp);
            System.out.println("Matrix: " + Arrays.toString(matrix));
            multiplyKey= lineMultiplyKey(matrix, keyMatrix);
            System.out.println("MultiplyKEy: " + Arrays.toString(multiplyKey));
            result[i] = indexesToSymbols(multiplyKey);
            System.out.println("result: " + (result[i]));
            i++;
        }
        return result;
    }

    public int[] lineToMatrix(String line){
        int[] lineMatrix = new int[line.length()];
        for (int i = 0; i < line.length(); i++)
        {
            lineMatrix[i] = alphabet.indexOf(line.charAt(i));
        }
        return lineMatrix;
    }

    public int[] lineMultiplyKey(int[] text, int[][] keyMatrix) {
        int[] resultMatrix = new int[text.length];
        for (int i = 0; i < text.length; i++)
        {
            for (int j = 0; j < text.length; j++)
            {
                resultMatrix[i] += keyMatrix[i][j] * text[j];
            }
            resultMatrix[i] %= alphabet.length();
        }
        return resultMatrix;
    }

    public String indexesToSymbols(int[] resultMatrix){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < resultMatrix.length; i++)
        {
            result.append(alphabet.charAt(resultMatrix[i]));
        }
        return result.toString();
    }

    public int[][] calcInvertedMatrix(int[][] matrix){
        int x = findXO(determinant(matrix, matrix.length));
        int[][] result = FinMinor(Minor(matrix), x);
        return  result;
    }

    public int[][] Minor(int[][] matrix){
        int[][] matrMinor = new int[matrix.length][matrix.length];

        matrMinor[0][0] = matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1];
        matrMinor[0][1] = -(matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]);
        matrMinor[0][2] = matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0];
        matrMinor[1][0] = -(matrix[0][1] * matrix[2][2] - matrix[0][2] * matrix[2][1]);
        matrMinor[1][1] = matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0];
        matrMinor[1][2] = -(matrix[0][0] * matrix[2][1] - matrix[0][1] * matrix[2][0]);
        matrMinor[2][0] = matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1];
        matrMinor[2][1] = -(matrix[0][0] * matrix[1][2] - matrix[0][2] * matrix[1][0]);
        matrMinor[2][2] = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        if (determinant(matrix, matrix.length) < 0) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    matrMinor[i][j] *= -1;
                }
            }
        }
        System.out.println(Arrays.deepToString(matrMinor));
        return matrMinor;
    }

    public int findXO(int det) {
        int num = 0;
        for (int index = 2; index < alphabet.length(); ++index)
        {
            System.out.println(index * det % alphabet.length());
            if (Math.abs(index * det % alphabet.length()) == 1)
            {
                num = index;
                System.out.println(num);
                break;
            }
        }

        return num;
    }

    public int[][] FinMinor(int[][] minor, int x) {
        int[][] finMinor = new int[minor.length][minor.length];

        for (int i = 0; i < minor.length; i++) {
            for (int j = 0; j < minor.length; j++) {
                finMinor[i][j] = minor[j][i];
            }
        }

        for (int i = 0; i < minor.length; i++) {
            for (int j = 0; j < minor.length; j++) {
                if (finMinor[i][j] < 0) {
                    while (finMinor[i][j] < 0) {
                        finMinor[i][j] += alphabet.length();
                    }
                } else if (finMinor[i][j] > alphabet.length()) {
                    while (finMinor[i][j] > alphabet.length()) {
                        finMinor[i][j] -= alphabet.length();
                    }
                }
                finMinor[i][j] *= x;
                finMinor[i][j] = finMinor[i][j] % alphabet.length();
            }
        }
        return finMinor;
    }
}
