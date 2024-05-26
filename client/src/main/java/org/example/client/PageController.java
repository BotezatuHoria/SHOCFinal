package org.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageController {

    @FXML
    private RadioButton checkCode;

    @FXML
    private RadioButton complexity;

    @FXML
    private Button enterButton;

    @FXML
    private Button infopoint;

    @FXML
    private ImageView infopointPicture;

    @FXML
    private TextArea inputBox;

    @FXML
    private TextArea outputBox;

    @FXML
    private Label size;

    @FXML
    private AnchorPane pane;

    @FXML
    private RadioButton testability;
    @FXML
    private RadioButton translate;

    @FXML
    private RadioButton addCommentary;

    private static String SERVER = "http://localhost:8080/";

    public void initialize(){
        ToggleGroup toggleGroup=new ToggleGroup();
        checkCode.setToggleGroup(toggleGroup);
        complexity.setToggleGroup(toggleGroup);
        testability.setToggleGroup(toggleGroup);
        translate.setToggleGroup(toggleGroup);
        //toggleGroup.getSelectedToggle().selectedProperty();
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
        if (inputBox.getText().length() > 2000) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nu merge boss");
            alert.showAndWait();
            return false;
        }
        if (!(checkCode.isSelected() || complexity.isSelected() || testability.isSelected() || translate.isSelected())){
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "api/gpt/translate"))
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

    public void addComment(){
        if(addCommentary.isSelected()) {
            String codeWithComments = "/*\n" + outputBox.getText() + "\n */ \n" + inputBox.getText();
            inputBox.setText(codeWithComments);
        }
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