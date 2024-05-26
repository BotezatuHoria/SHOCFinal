package org.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.awt.*;
import java.awt.datatransfer.*;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageController {

    @FXML
    private Button addButton;

    @FXML
    private RadioButton addCommentary;

    @FXML
    private RadioButton broadAnswer;

    @FXML
    private RadioButton checkCode;

    @FXML
    private RadioButton complexity;

    @FXML
    private Button enterButton;

    @FXML
    private RadioButton errorCorrection;

    @FXML
    private RadioButton explanation;

    @FXML
    private RadioButton hints;

    @FXML
    private Button infopoint;

    @FXML
    private ImageView infopointPicture;

    @FXML
    private TextArea inputBox;

    @FXML
    private TextArea outputBox;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField selectedLanguage;

    @FXML
    private Label size;

    @FXML
    private RadioButton testability;

    @FXML
    private RadioButton translate;

    @FXML
    private VBox correctionOpt;

    @FXML
    private Button copyButton;
    private TargetDataLine targetDataLine;
    private Thread audioRecordThread;

    private static String SERVER = "http://localhost:8080/";

    public void initialize(){
        ToggleGroup toggleGroup=new ToggleGroup();
        checkCode.setToggleGroup(toggleGroup);
        complexity.setToggleGroup(toggleGroup);
        testability.setToggleGroup(toggleGroup);
        translate.setToggleGroup(toggleGroup);
        errorCorrection.setToggleGroup(toggleGroup);
        correctionOpt.setVisible(false);
        selectedLanguage.setVisible(false);

        translate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selectedLanguage.setVisible(newValue); // Show or hide the text field based on RadioButton state
            selectedLanguage.setPromptText("Language...");
            selectedLanguage.setStyle("-fx-font-size: 14px; -fx-prompt-text-fill: #424040; -fx-translate-y: -1px");


        });
        errorCorrection.selectedProperty().addListener((observable, oldValue, newValue) -> {
            hints.selectedProperty().set(false);
            broadAnswer.selectedProperty().set(false);
            explanation.selectedProperty().set(false);
            correctionOpt.setVisible(newValue);
            ToggleGroup toggle = new ToggleGroup();
            hints.setToggleGroup(toggle);
            broadAnswer.setToggleGroup(toggle);
            explanation.setToggleGroup(toggle);
        });
    }

    public void sendText(){
        String text=inputBox.getText();
        //send to gpt
    }

    public void showSize() {
        String sizeLabel = "";
        sizeLabel += inputBox.getText().length();
        size.setText(sizeLabel + "/2000");

    }
    public boolean checkOptions() {
        if(inputBox.getText().length()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Input box is empty");
            alert.showAndWait();
            return false;
        }

        if (inputBox.getText().length() > 2000) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Character limit exceeded");
            alert.setContentText("Your input message has too many characters. Please limit to 2000 characters");
            alert.showAndWait();
            return false;
        }
        if (!(checkCode.isSelected() || complexity.isSelected() || testability.isSelected() || translate.isSelected()
                || errorCorrection.isSelected() || broadAnswer.isSelected() || explanation.isSelected() || hints.isSelected())){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No option selected!");
            alert.setContentText("You need to select one of the available options!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void sendRequest() {
        if (checkOptions()) {
     outputBox.setText("");
            if (testability.isSelected())
              outputBox.setText(checkTestability(inputBox.getText().trim()));
            if (complexity.isSelected())
              outputBox.setText(checkComplexity(inputBox.getText().trim()));
            if(translate.isSelected())
                outputBox.setText(translateCode(inputBox.getText().trim()));
            if(checkCode.isSelected())
                outputBox.setText(getCodeExplanation(inputBox.getText().trim()));
            if (errorCorrection.isSelected()) {
                if (hints.isSelected())
                    outputBox.setText(getErrors("hint", inputBox.getText().trim()));
                if (broadAnswer.isSelected())
                    outputBox.setText(getErrors("explanation", inputBox.getText().trim()));
                if (explanation.isSelected())
                    outputBox.setText(getErrors("complete", inputBox.getText().trim()));
            }
        }
    }

    public String checkTestability(String code) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/testability"))
                .POST(HttpRequest.BodyPublishers.ofString(code))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String checkComplexity(String code) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/complexity"))
                .POST(HttpRequest.BodyPublishers.ofString(code))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
    public String translateCode(String code)
    {
        String lang="english";
        if(selectedLanguage.getText()!=null)
            lang=selectedLanguage.getText();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/translate?lang="+lang))
                .POST(HttpRequest.BodyPublishers.ofString(code))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String getCodeExplanation(String code) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/codeExplanation"))
                .POST(HttpRequest.BodyPublishers.ofString(code))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String getErrors(String type, String code) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/errors?type=" + type))
                .POST(HttpRequest.BodyPublishers.ofString(code))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public void addComment(){
        if(addCommentary.isSelected()) {
            String codeWithComments = "/*\n" + outputBox.getText() + "\n */ \n" + inputBox.getText();
            inputBox.setText(codeWithComments);
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selection");
            alert.setContentText("Please select the radio button");
            alert.showAndWait();
        }
    }

    public void copyToClipboard() {
        StringSelection stringSelection = new StringSelection(inputBox.getText().trim());

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(stringSelection, null);
    }

    public void openInfopoint() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InfopointController.class.getResource("infopoint.fxml"));
        Stage stage=new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Infopoint");
        stage.setScene(scene);
        stage.show();
    }

//    public void captureAudio() throws LineUnavailableException {
//        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
//        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
//
//        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
//        targetDataLine.start();
//
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.show();
//
//        Thread audioRecord = new Thread(() -> {
//            AudioInputStream recordingStream = new AudioInputStream(targetDataLine);
//            File output = new File("question.wav"); // Use WAV format for better compatibility
//            try {
//                AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, output);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                targetDataLine.stop();
//                targetDataLine.close();
//            }
//        });
//
//        audioRecord.start();
//
//        // Use a simple delay to simulate the duration of recording for demonstration purposes
//        // In a real application, you should have a proper mechanism to stop the recording
//        try {
//            Thread.sleep(5000); // Record for 5 seconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Interrupt the recording thread to stop recording
//        audioRecord.interrupt();
//    }

    public void captureAudio() {
        try {
            captureAudioS();
            // Record for a specified duration
            Thread.sleep(5000); // Record for 5 seconds
            stopRecording();
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void captureAudioS() throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(audioFormat); // Open the line with the specified format
        targetDataLine.start(); // Start capturing audio

        audioRecordThread = new Thread(() -> {
            AudioInputStream recordingStream = new AudioInputStream(targetDataLine);
            File output = new File("question.wav");
            try {
                AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        audioRecordThread.start();
    }

    public void stopRecording() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
        if (audioRecordThread != null) {
            try {
                audioRecordThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}