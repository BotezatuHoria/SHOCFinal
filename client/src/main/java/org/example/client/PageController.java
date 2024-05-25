package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;

import java.io.IOException;

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

    private static String SERVER = "http://localhost:8080/";

    public void initialize(){
        ToggleGroup toggleGroup=new ToggleGroup();
        checkCode.setToggleGroup(toggleGroup);
        complexity.setToggleGroup(toggleGroup);
        testability.setToggleGroup(toggleGroup);
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
        if (!(checkCode.isSelected() || complexity.isSelected() || testability.isSelected())){
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
            outputBox.setText(checkTestability(inputBox.getText().trim()));
        }
    }


    public String checkTestability(String code) {
        OkHttpClient client = new OkHttpClient();

        // Properly format the JSON string
        String json = "{\"code\": \"" + code + "\"}";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(SERVER + "/api/gpt/translateRo")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Read and return the response body
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}
