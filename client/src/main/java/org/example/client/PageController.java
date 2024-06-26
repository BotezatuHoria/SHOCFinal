package org.example.client;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageController {

    @FXML
    private Button addButton;

    @FXML
    private RadioButton addComment;

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
    @FXML
    private Button copyButton2;
    @FXML
    private ImageView loadingAnim;
    @FXML
    private Label copiedText;
    @FXML
    private Label copiedText2;
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
        copiedText.setVisible(false);
        copiedText2.setVisible(false);
        translate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selectedLanguage.setVisible(newValue); // Show or hide the text field based on RadioButton state
            selectedLanguage.setPromptText("Language...");
            selectedLanguage.setStyle("-fx-font-size: 14px; -fx-prompt-text-fill: #2a2828; -fx-translate-y: -2px");


        });
        errorCorrection.selectedProperty().addListener((observable, oldValue, newValue) -> {
            hints.selectedProperty().set(false);
            broadAnswer.selectedProperty().set(false);
            correctionOpt.setVisible(newValue);
            ToggleGroup toggle = new ToggleGroup();
            hints.setToggleGroup(toggle);
            broadAnswer.setToggleGroup(toggle);
        });
        loadingAnim.setVisible(false);
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
                || errorCorrection.isSelected() || broadAnswer.isSelected()  || hints.isSelected())){
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
            //  loadingAnim.setVisible(true);

            Platform.runLater(() -> loadingAnim.setVisible(true));
            new Thread(() -> {
                String responseText = "";
                if (testability.isSelected())
                    responseText = checkTestability(inputBox.getText().trim());
                else if (complexity.isSelected())
                    responseText = checkComplexity(inputBox.getText().trim());
                else if (translate.isSelected())
                    responseText = translateCode(inputBox.getText().trim());
                else if (checkCode.isSelected())
                    responseText = getCodeExplanation(inputBox.getText().trim());
                else if (errorCorrection.isSelected()) {
                    if (hints.isSelected())
                        responseText = getErrors("hint", inputBox.getText().trim());
                    if (broadAnswer.isSelected())
                        responseText = getErrors("explanation", inputBox.getText().trim());
                }

                // Update UI with the response
                String finalResponseText = responseText;
                Platform.runLater(() -> {
                    outputBox.setText(finalResponseText);
                    loadingAnim.setVisible(false);
                });
            }).start();
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
        if(selectedLanguage.getText()!=null) {
            lang = selectedLanguage.getText();
            lang=lang.replace(" ","%20");
        }
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
        if(addComment.isSelected()) {
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
        copiedText.setVisible(true);
        copiedText.setOpacity(1.0);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
        visiblePause.setOnFinished(
                event -> {
                    FadeTransition fadeOut = new FadeTransition( Duration.seconds(1), copiedText);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> {
                        copiedText.setVisible(false);
                        copiedText.setOpacity(0.0);
                    });
                    fadeOut.play();
                }

        );
        visiblePause.play();

        clipboard.setContents(stringSelection, null);
    }
    public void copyToClipboardFromResult() {
        StringSelection stringSelection = new StringSelection(outputBox.getText().trim());

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        copiedText2.setVisible(true);
        copiedText2.setOpacity(1.0);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
        visiblePause.setOnFinished(
                event -> {
                    FadeTransition fadeOut = new FadeTransition( Duration.seconds(1), copiedText2);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> {
                        copiedText2.setVisible(false);
                        copiedText2.setOpacity(0.0);
                    });
                    fadeOut.play();
                }

        );
        visiblePause.play();

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
}