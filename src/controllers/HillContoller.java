package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by andrii on 02.05.17.
 */
public class HillContoller {

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


    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,:;\"'-";
    private int[][] keyMatrix;

    @FXML
    public void checkTheKeyButtonHandler(){
        checkTheKey();
    }

    @FXML
    public void decryptButtonHandler(){
        boolean isKeyCorrect = checkTheKey();

        if (!isKeyCorrect){
            showError("Key isn't correct!");
        }else {
            tab5_outputTextArea.clear();
        }

        if (tab5_inputTextArea.getText() == ""){
            showError("Please enter text to decrypt");
        }

        String text = tab5_inputTextArea.getText();

        String dectyptedText = decrypt(text);

        tab5_outputTextArea.setText(dectyptedText);
    }

    @FXML
    public void encryptButtonHandler(){
        boolean isKeyCorrect = checkTheKey();

        if (!isKeyCorrect){
            showError("Key isn't correct!");
        }else {
            tab5_outputTextArea.clear();
        }

        if (tab5_inputTextArea.getText() == ""){
            showError("Please enter text to encrypt");
        }

        String text = tab5_inputTextArea.getText();

        String enctyptedText = encrypt(text);

        tab5_outputTextArea.setText(enctyptedText);
    }

    public String decrypt(String text){
        int s = keyMatrix.length;
        return devide(text, s);
    }

    private String devide(String text, int s) {
        StringBuilder stringBuilder = new StringBuilder();
        while (text.length() > s)
        {
            String sub = text.substring(0, s);
            text = text.substring(s, text.length());
            stringBuilder.append(perform(sub));
        }
        if (text.length() == s)
            stringBuilder.append(perform(text));
        else if (text.length() < s)
        {
            for (int i = text.length(); i < s; i++)
                text = text + 'x';
            stringBuilder.append(perform(text));
        }

        return stringBuilder.toString();
    }

    public String encrypt(String text){
        int s = keyMatrix.length;
        //TODO change keyMatrix to ^(-1)
        return devide(text, s);
    }
    public int[][] keyToMatrix(String key, int len)
    {
        int[][] keyMatrix = new int[len][len];
        int c = 0;
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                keyMatrix[i][j] = ((int) key.charAt(c)) - 97;
                c++;
            }
        }
        return keyMatrix;
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

        return true;
    }

    public int determinant(int A[][], int N)
    {
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

    public void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String perform(String temp)
    {
        int[] matrix = lineToMatrix(temp);
        int[] multiplyKey= lineMultiplyKey(matrix);
        return result(multiplyKey);
    }

    public int[] lineToMatrix(String line)
    {
        int[] lineMatrix = new int[line.length()];
        for (int i = 0; i < line.length(); i++)
        {
            lineMatrix[i] = ((int) line.charAt(i)) - 97;
        }
        return lineMatrix;
    }

    public int[] lineMultiplyKey(int[] text)
    {
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

    public String result(int[] resultmatrix)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < resultmatrix.length; i++)
        {
            result.append((char) (resultmatrix[i] + 97));
        }
        return result.toString();
    }

    public void cofact(int num[][], int f)
    {
        int b[][], fac[][];
        b = new int[f][f];
        fac = new int[f][f];
        int p, q, m, n, i, j;
        for (q = 0; q < f; q++)
        {
            for (p = 0; p < f; p++)
            {
                m = 0;
                n = 0;
                for (i = 0; i < f; i++)
                {
                    for (j = 0; j < f; j++)
                    {
                        b[i][j] = 0;
                        if (i != q && j != p)
                        {
                            b[m][n] = num[i][j];
                            if (n < (f - 2))
                                n++;
                            else
                            {
                                n = 0;
                                m++;
                            }
                        }
                    }
                }
                fac[q][p] = (int) Math.pow(-1, q + p) * determinant(b, f - 1);
            }
        }
        trans(fac, f);
    }

    void trans(int fac[][], int r)
    {
        int i, j;
        int b[][], inv[][];
        b = new int[r][r];
        inv = new int[r][r];
        int d = determinant(keyMatrix, r);
        int mi = mi(d % 26);
        mi %= 26;
        if (mi < 0)
            mi += 26;
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                b[i][j] = fac[j][i];
            }
        }
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                inv[i][j] = b[i][j] % 26;
                if (inv[i][j] < 0)
                    inv[i][j] += 26;
                inv[i][j] *= mi;
                inv[i][j] %= 26;
            }
        }
        System.out.println("\nInverse key:");
        matrixToInvKey(inv, r);
    }

    public int mi(int d)
    {
        int q, r1, r2, r, t1, t2, t;
        r1 = 26;
        r2 = d;
        t1 = 0;
        t2 = 1;
        while (r1 != 1 && r2 != 0)
        {
            q = r1 / r2;
            r = r1 % r2;
            t = t1 - (t2 * q);
            r1 = r2;
            r2 = r;
            t1 = t2;
            t2 = t;
        }
        return (t1 + t2);
    }

    public void matrixToInvKey(int inv[][], int n)
    {
        String invkey = "";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                invkey += (char) (inv[i][j] + 97);
            }
        }
        System.out.print(invkey);
    }
}
