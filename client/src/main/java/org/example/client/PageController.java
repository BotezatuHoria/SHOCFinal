package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PageController {

    @FXML
    AnchorPane pane;

    @FXML
    TextField inputBox;

    @FXML
    TextField outputBox;

    @FXML
    Button infopoint;

    @FXML
    Button enterButton;

    @FXML
    RadioButton checkCode;

    @FXML
    RadioButton complexity;

    @FXML
    RadioButton testability;

    public void sendText(){
        String text=inputBox.getText();
        //send to gpt
    }
}
