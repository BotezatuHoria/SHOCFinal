package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InfopointController {

    @FXML
    TextArea textAreaInfoPoint;

    public void initialize(){
        textAreaInfoPoint.setStyle("-fx-background-color: #2a2828;-fx-control-inner-background: #2a2828");
        textAreaInfoPoint.setEditable(false);
        textAreaInfoPoint.setText("This tool is here to help you more profoundly understand confusing code! \n" +
                "\n It helps developers, especially reviewers, improve the quality of the production code.\n" +
                "\n" +
                "The key features of this tool are:\n" +
                "1. Explain what the code does\n" +
                "2. Check the complexity of the code\n" +
                "3. Check how easy the code is to test\n" +
                "4. Add the response as comments above your inputed code\n" +
                "5. Translate your code to other languages (if you are curious)\n" +
                "6. Detect possible errors in your code\n" +
                "\n" +
                "Input your code on the left box and select one of the 5 options available.\n" +
                    "To add the comments select the radio button and then click on 'Add.'");

    }
}
