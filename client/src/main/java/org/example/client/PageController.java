package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

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
            // call to server utils

        }
    }
}
