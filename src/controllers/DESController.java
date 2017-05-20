package controllers;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64DecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;
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

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ResourceBundle;

/**
 * Created by andrii on 19.05.17.
 */
public class DESController implements Initializable{

    @FXML
    private TextArea tab8_inputTextArea;
    @FXML
    private TextArea tab8_outputTextArea;
    @FXML
    private TextField keyTextField;
    @FXML
    private Button lineChartsButton;

    private static Cipher ecipher;
    private static Cipher dcipher;

    private static final int iterationCount = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tab8_inputTextArea.setText(Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Caesar_decryptedText"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        keyTextField.setText("KHYMERA");
        keyFieldUpdated();
    }

    // 8-byte Salt
    private static byte[] salt = {

            (byte)0xB2, (byte)0x12, (byte)0xD5, (byte)0xB2,

            (byte)0x44, (byte)0x21, (byte)0xC3, (byte)0xC3
    };

    @FXML
    public void keyFieldUpdated(){
        try {

            String passPhrase = keyTextField.getText();

            // create a user-chosen password that can be used with password-based encryption (PBE)
            // provide password, salt, iteration count for generating PBEKey of fixed-key-size PBE ciphers
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);

            // create a secret (symmetric) key using PBE with MD5 and DES
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            // construct a parameter set for password-based encryption as defined in the PKCS #5 standard
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // initialize the ciphers with the given key

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        }
        catch (InvalidAlgorithmParameterException e) {
            Utils.showError("Invalid Alogorithm Parameter:" + e.getMessage());
            return;
        }
        catch (InvalidKeySpecException e) {
            Utils.showError("Invalid Key Spec:" + e.getMessage());
            return;
        }
        catch (NoSuchAlgorithmException e) {
            Utils.showError("No Such Algorithm:" + e.getMessage());
            return;
        }
        catch (NoSuchPaddingException e) {
            Utils.showError("No Such Padding:" + e.getMessage());
            return;
        }
        catch (InvalidKeyException e) {
            Utils.showError("Invalid Key:" + e.getMessage());
            return;
        }
    }

    @FXML
    public void decryptButtonHandler() {
        String decoded = decrypt(tab8_inputTextArea.getText());
        tab8_outputTextArea.setText(decoded);
    }

    @FXML
    public void encryptButtonHandler() {
        String encodedNumbers = encrypt(tab8_inputTextArea.getText());
        tab8_outputTextArea.setText(encodedNumbers);
    }

    public String encrypt(String str) {

        try {

            // encode the string into a sequence of bytes using the named charset

            // storing the result into a new byte array.

            byte[] utf8 = str.getBytes("UTF8");

            byte[] enc = ecipher.doFinal(utf8);

// encode to base64

            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public String decrypt(String str) {

        try {

            // decode with base64 to get bytes

            byte[] dec = BASE64DecoderStream.decode(str.getBytes());

            byte[] utf8 = dcipher.doFinal(dec);

        // create new string based on the specified charset

            return new String(utf8, "UTF8");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    @FXML
    public void lineChartsButtonHandler() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GraphicsWindowl.fxml"));
        Parent root = root = fxmlLoader.load();

        GraphicsWindowController newWindow = fxmlLoader.<GraphicsWindowController>getController();
        newWindow.setDectyptedTextStatistic(Statistic.findFrequency(tab8_inputTextArea.getText(), 1));
        newWindow.setEncryptedTextStatistic(Statistic.findFrequency(tab8_outputTextArea.getText(), 1));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
