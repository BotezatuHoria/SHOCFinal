package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InfopointController {

    @FXML
    TextArea textArea;

    public void initialize(){
        textArea.setStyle("-fx-background-color: #2a2828;-fx-control-inner-background: #2a2828");
        textArea.setEditable(false);
        textArea.setText("This tool is here to help you more profoundly understand confusing code! \n" +
                "\n It helps developers, especially reviewers, improve the quality of the production code.\n" +
                "\n" +
                "The key features of this tool are:\n" +
                "1. Explains what the code does\n" +
                "2. Checks for the complexity of the code\n" +
                "3. Checks how easy the code is to test\n" +
                "4. It can add the response as comments above your inputed code\n" +
                "\n" +
                "Input your code on the left box and select one of the 3 options available.\n" +
                    "To add the comments select the radio button and then click on 'Add.'");

    }
}
