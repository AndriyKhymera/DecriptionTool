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
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andrii on 19.05.17.
 */
public class RSAConroller implements Initializable{

    @FXML
    private TextArea tab7_inputTextArea;
    @FXML
    private TextArea tab7_outputTextArea;
    @FXML
    private TextField p_TextField;
    @FXML
    private TextField q_TextField;
    @FXML
    private TextArea calculationTextArea;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,;'-";

    private final static BigInteger one = new BigInteger("1");
    private BigInteger p;
    private BigInteger q;
    private BigInteger exp = new BigInteger("19");
    private BigInteger privateKey;
    private BigInteger modulus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tab7_inputTextArea.setText(Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Caesar_decryptedText"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        p_TextField.setText("29");
        q_TextField.setText("59");
    }

    @FXML
    void decryptButtonHandler(ActionEvent event) {
        String decoded = decode(tab7_inputTextArea.getText());
        tab7_outputTextArea.setText(decoded);
    }

    @FXML
    void encryptButtonHandler(ActionEvent event) {
        String encodedNumbers = encode(tab7_inputTextArea.getText());
        tab7_outputTextArea.setText(encodedNumbers);
        calculationTextArea.setText(getEncodedText(encodedNumbers));
    }

    public String encode(String text) {
        p = new BigInteger(p_TextField.getText());
        q = new BigInteger(q_TextField.getText());
        modulus = p.multiply(q);
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        privateKey = exp.modInverse(phi);
        String[] txtSplittedBy3 = text.split("(?<=\\G.{3})");
        StringBuilder outputSb = new StringBuilder();
        for (String str : txtSplittedBy3) {
            int[] items = new int[3];
            for (int j = 0; j < items.length; j++) {
                if (str.length() > j) {
                    items[j] = alphabet.indexOf(str.charAt(j));
                } else {
                    items[j] = ' ';
                }
            }
            int[] words = formWords(items);
            BigInteger w1 = BigInteger.valueOf(words[0]),
                    w2 = BigInteger.valueOf(words[1]);
            int encodedW1 = w1.modPow(exp, modulus).intValue(),
                    encodedW2 = w2.modPow(exp, modulus).intValue();
            outputSb.append(String.valueOf(" " + encodedW1 + " " + encodedW2));
        }
        return outputSb.toString();
    }

    @FXML
    public String decode(String text) {
        p = new BigInteger(p_TextField.getText());
        q = new BigInteger(q_TextField.getText());
        StringBuilder outputSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        int[] numberBlock = new int[2];
        int nbIterator = 0;
        for (int i = 0; i < text.length(); i++) {
            char curChar = text.charAt(i);
            String stringed = sb.toString();
            if (curChar != ' ') {
                sb.append(curChar);
            }
            if (curChar == ' ' && !stringed.isEmpty() || i == text.length() - 1) {
                BigInteger currentVal = BigInteger.valueOf(Integer.parseInt(stringed));
                numberBlock[nbIterator++] = currentVal.modPow(privateKey, modulus).intValue();
                sb.setLength(0);
            }
            if (nbIterator == 2) {
                outputSb.append(extractStrFromNumber(numberBlock));
                nbIterator = 0;
            }
        }
        return outputSb.toString();
    }

    public String getEncodedText(String text) {
        StringBuilder outputSb = new StringBuilder();
        outputSb.append(String.valueOf("Вікдритий ключ {e, n} = {" + exp.intValue() + ", " + modulus.intValue() + "}\n"));
        outputSb.append(String.valueOf("Закритий ключ {d, n} = {" + privateKey.intValue() + ", " + modulus.intValue() + "}\n\n"));
        String[] strs = text.split(" ");
        int i = 0;
        for (String str : strs) {
            if(i++ != 0) {
                    outputSb.append(alphabet.charAt((int) BigInteger.valueOf((int) Double.parseDouble(str)).mod(BigInteger.valueOf(alphabet.length())).intValue()));
            }
        }
        return outputSb.toString();
    }

    private String extractStrFromNumber(int[] vals) {
        String[] w = new String[2];
        for (int i = 0; i < vals.length; i++) {
            w[i] = String.valueOf(vals[i]);
        }
        for (int i = 0; i < w.length; i++) {
            int len = w[i].length();
            for (int j = 0; j < 3 - len; j++) {
                w[i] = "0" + w[i];
            }
        }
        int w1 = parseWordFromStrs(w[0], w[0], 0, 1);
        int w2 = parseWordFromStrs(w[0], w[1], 2, 0);
        int w3 = parseWordFromStrs(w[1], w[1], 1, 2);

        return "" + alphabet.charAt(w1) + alphabet.charAt(w2) + alphabet.charAt(w3);
    }

    private String deleteSpaices(String str) {
        return str.replaceAll("\\s+", "");
    }

    private int parseWordFromStrs(String str1, String str2, int index1, int index2) {
        return (int) BigInteger.valueOf((int) (double) Integer.valueOf("" + str1.charAt(index1) + str2.charAt(index2))).mod(BigInteger.valueOf(alphabet.length())).intValue();
    }

    private int[] formWords(int[] items) {
        String[] str = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            str[i] = items[i] < 10 ? "0" + items[i] : String.valueOf(items[i]);
        }
        String w1 = str[0] + str[1].charAt(0);
        String w2 = str[1].charAt(1) + str[2];
        return new int[]{Integer.parseInt(w1), Integer.parseInt(w2)};
    }

    @FXML
    public void lineChartsButtonHandler() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphicsWindowl.fxml"));
        Parent root = root = fxmlLoader.load();

        GraphicsWindowController newWindow = fxmlLoader.<GraphicsWindowController>getController();
        newWindow.setDectyptedTextStatistic(Statistic.findFrequency(tab7_inputTextArea.getText(), 1));
        newWindow.setEncryptedTextStatistic(Statistic.findFrequency(tab7_outputTextArea.getText(), 1));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
